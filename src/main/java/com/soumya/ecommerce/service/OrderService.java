package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.OrderDTO;
import com.soumya.ecommerce.dto.PlaceOrderRequest;
import com.soumya.ecommerce.dto.UpdateOrderStatusRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {

    OrderDTO placeOrder(PlaceOrderRequest request);

    Page<OrderDTO> getMyOrders(Pageable pageable);

    OrderDTO getMyOrderById(UUID orderId);

    OrderDTO cancelMyOrder(UUID orderId);

    Page<OrderDTO> getAllOrders(Pageable pageable);

    OrderDTO updateOrderStatus(UUID orderId, UpdateOrderStatusRequest request);
}
