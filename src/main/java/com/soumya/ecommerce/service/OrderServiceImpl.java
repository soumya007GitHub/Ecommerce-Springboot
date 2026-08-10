package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.OrderDTO;
import com.soumya.ecommerce.dto.PlaceOrderRequest;
import com.soumya.ecommerce.dto.UpdateOrderStatusRequest;
import com.soumya.ecommerce.entity.*;
import com.soumya.ecommerce.exception.BadRequestException;
import com.soumya.ecommerce.exception.InsufficientStockException;
import com.soumya.ecommerce.exception.ResourceNotFoundException;
import com.soumya.ecommerce.mapper.OrderMapper;
import com.soumya.ecommerce.repository.AddressRepository;
import com.soumya.ecommerce.repository.CartRepository;
import com.soumya.ecommerce.repository.OrderRepository;
import com.soumya.ecommerce.repository.ProductVariantRepository;
import com.soumya.ecommerce.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Set<OrderStatus> CANCELLABLE_STATUSES = Set.of(OrderStatus.PENDING, OrderStatus.CONFIRMED);

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDTO placeOrder(PlaceOrderRequest request) {

        User user = SecurityUtils.getCurrentUser();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BadRequestException("Your cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Your cart is empty");
        }

        Address address = addressRepository.findByIdAndUserId(request.getAddressId(), user.getId())
                .orElseThrow(() -> ResourceNotFoundException.of("Address", request.getAddressId()));

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {

                    ProductVariant variant = cartItem.getProductVariant();

                    if (variant.getStockQuantity() < cartItem.getQuantity()) {
                        throw new InsufficientStockException(
                                "Only " + variant.getStockQuantity() + " unit(s) available for "
                                        + variant.getProduct().getName() + " (" + variant.getColor() + ", " + variant.getSize() + ")"
                        );
                    }

                    variant.setStockQuantity(variant.getStockQuantity() - cartItem.getQuantity());
                    productVariantRepository.save(variant);

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(variant.getProduct().getId());
                    orderItem.setProductName(variant.getProduct().getName());
                    orderItem.setProductVariantId(variant.getId());
                    orderItem.setColor(variant.getColor());
                    orderItem.setSize(variant.getSize());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setUnitPrice(cartItem.getUnitPrice());

                    return orderItem;
                })
                .toList();

        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setShippingAddressLine1(address.getAddressLine1());
        order.setShippingAddressLine2(address.getAddressLine2());
        order.setShippingCity(address.getCity());
        order.setShippingState(address.getState());
        order.setShippingPostalCode(address.getPostalCode());
        order.setShippingCountry(address.getCountry());
        order.setShippingContactPhone(address.getContactPhone());

        orderItems.forEach(item -> item.setOrder(order));
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getMyOrders(Pageable pageable) {

        User user = SecurityUtils.getCurrentUser();

        return orderRepository.findByUserId(user.getId(), pageable).map(orderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getMyOrderById(UUID orderId) {

        User user = SecurityUtils.getCurrentUser();

        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> ResourceNotFoundException.of("Order", orderId));

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDTO cancelMyOrder(UUID orderId) {

        User user = SecurityUtils.getCurrentUser();

        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> ResourceNotFoundException.of("Order", orderId));

        if (!CANCELLABLE_STATUSES.contains(order.getStatus())) {
            throw new BadRequestException("Order cannot be cancelled once it is " + order.getStatus());
        }

        order.getItems().forEach(item -> productVariantRepository.findById(item.getProductVariantId())
                .ifPresent(variant -> {
                    variant.setStockQuantity(variant.getStockQuantity() + item.getQuantity());
                    productVariantRepository.save(variant);
                }));

        order.setStatus(OrderStatus.CANCELLED);

        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    @Override
    public OrderDTO updateOrderStatus(UUID orderId, UpdateOrderStatusRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> ResourceNotFoundException.of("Order", orderId));

        order.setStatus(request.getStatus());

        if (request.getStatus() == OrderStatus.DELIVERED) {
            order.setPaymentStatus(PaymentStatus.PAID);
        }

        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }
}
