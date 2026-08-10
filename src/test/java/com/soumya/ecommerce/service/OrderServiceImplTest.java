package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.OrderDTO;
import com.soumya.ecommerce.dto.PlaceOrderRequest;
import com.soumya.ecommerce.entity.*;
import com.soumya.ecommerce.exception.BadRequestException;
import com.soumya.ecommerce.mapper.OrderMapper;
import com.soumya.ecommerce.repository.AddressRepository;
import com.soumya.ecommerce.repository.CartRepository;
import com.soumya.ecommerce.repository.OrderRepository;
import com.soumya.ecommerce.repository.ProductVariantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ProductVariantRepository productVariantRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("customer@example.com");
        user.setRole(Role.CUSTOMER);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void placeOrder_throwsWhenCartIsEmpty() {

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        PlaceOrderRequest request = new PlaceOrderRequest(UUID.randomUUID(), PaymentMethod.COD);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        assertThatThrownBy(() -> orderService.placeOrder(request))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void placeOrder_decrementsVariantStockAndClearsCart() {

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Running Shoe");
        product.setPrice(BigDecimal.valueOf(2999));

        ProductVariant variant = new ProductVariant();
        variant.setId(UUID.randomUUID());
        variant.setColor("Black");
        variant.setSize("M");
        variant.setStockQuantity(5);
        variant.setProduct(product);

        CartItem cartItem = new CartItem();
        cartItem.setProductVariant(variant);
        cartItem.setQuantity(2);
        cartItem.setUnitPrice(product.getPrice());

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>(java.util.List.of(cartItem)));

        Address address = new Address();
        address.setId(UUID.randomUUID());
        address.setAddressLine1("123 Main St");
        address.setCity("Bengaluru");
        address.setState("KA");
        address.setPostalCode("560001");
        address.setCountry("India");
        address.setContactPhone("9999999999");

        PlaceOrderRequest request = new PlaceOrderRequest(address.getId(), PaymentMethod.COD);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(addressRepository.findByIdAndUserId(address.getId(), user.getId())).thenReturn(Optional.of(address));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toDto(any(Order.class))).thenReturn(OrderDTO.builder().totalAmount(BigDecimal.valueOf(5998)).build());

        OrderDTO result = orderService.placeOrder(request);

        assertThat(variant.getStockQuantity()).isEqualTo(3);
        assertThat(cart.getItems()).isEmpty();
        assertThat(result.getTotalAmount()).isEqualByComparingTo(BigDecimal.valueOf(5998));
    }
}
