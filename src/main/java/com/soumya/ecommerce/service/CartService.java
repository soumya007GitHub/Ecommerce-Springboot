package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.AddToCartRequest;
import com.soumya.ecommerce.dto.CartDTO;
import com.soumya.ecommerce.dto.UpdateCartItemRequest;

import java.util.UUID;

public interface CartService {

    CartDTO getMyCart();

    CartDTO addItem(AddToCartRequest request);

    CartDTO updateItemQuantity(UUID cartItemId, UpdateCartItemRequest request);

    CartDTO removeItem(UUID cartItemId);

    void clearCart();
}
