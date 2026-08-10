package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.AddToCartRequest;
import com.soumya.ecommerce.dto.CartDTO;
import com.soumya.ecommerce.dto.UpdateCartItemRequest;
import com.soumya.ecommerce.entity.Cart;
import com.soumya.ecommerce.entity.CartItem;
import com.soumya.ecommerce.entity.ProductVariant;
import com.soumya.ecommerce.entity.User;
import com.soumya.ecommerce.exception.InsufficientStockException;
import com.soumya.ecommerce.exception.ResourceNotFoundException;
import com.soumya.ecommerce.mapper.CartMapper;
import com.soumya.ecommerce.repository.CartRepository;
import com.soumya.ecommerce.repository.ProductVariantRepository;
import com.soumya.ecommerce.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional(readOnly = true)
    public CartDTO getMyCart() {

        User user = SecurityUtils.getCurrentUser();

        Cart cart = getOrCreateCart(user);

        return cartMapper.toDto(cart);
    }

    @Override
    public CartDTO addItem(AddToCartRequest request) {

        User user = SecurityUtils.getCurrentUser();

        Cart cart = getOrCreateCart(user);

        ProductVariant variant = productVariantRepository.findById(request.getProductVariantId())
                .orElseThrow(() -> ResourceNotFoundException.of("ProductVariant", request.getProductVariantId()));

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProductVariant().getId().equals(variant.getId()))
                .findFirst()
                .orElse(null);

        int requestedQuantity = request.getQuantity() + (existingItem != null ? existingItem.getQuantity() : 0);

        if (variant.getStockQuantity() < requestedQuantity) {
            throw new InsufficientStockException(
                    "Only " + variant.getStockQuantity() + " unit(s) available for this variant"
            );
        }

        if (existingItem != null) {
            existingItem.setQuantity(requestedQuantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductVariant(variant);
            newItem.setQuantity(request.getQuantity());
            newItem.setUnitPrice(variant.getProduct().getPrice());
            cart.getItems().add(newItem);
        }

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toDto(savedCart);
    }

    @Override
    public CartDTO updateItemQuantity(UUID cartItemId, UpdateCartItemRequest request) {

        User user = SecurityUtils.getCurrentUser();

        Cart cart = getOrCreateCart(user);

        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> ResourceNotFoundException.of("CartItem", cartItemId));

        if (item.getProductVariant().getStockQuantity() < request.getQuantity()) {
            throw new InsufficientStockException(
                    "Only " + item.getProductVariant().getStockQuantity() + " unit(s) available for this variant"
            );
        }

        item.setQuantity(request.getQuantity());

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toDto(savedCart);
    }

    @Override
    public CartDTO removeItem(UUID cartItemId) {

        User user = SecurityUtils.getCurrentUser();

        Cart cart = getOrCreateCart(user);

        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(cartItemId));

        if (!removed) {
            throw ResourceNotFoundException.of("CartItem", cartItemId);
        }

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toDto(savedCart);
    }

    @Override
    public void clearCart() {

        User user = SecurityUtils.getCurrentUser();

        Cart cart = getOrCreateCart(user);

        cart.getItems().clear();

        cartRepository.save(cart);
    }

    private Cart getOrCreateCart(User user) {

        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }
}
