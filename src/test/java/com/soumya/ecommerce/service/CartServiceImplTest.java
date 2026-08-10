package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.AddToCartRequest;
import com.soumya.ecommerce.entity.*;
import com.soumya.ecommerce.exception.InsufficientStockException;
import com.soumya.ecommerce.exception.ResourceNotFoundException;
import com.soumya.ecommerce.mapper.CartMapper;
import com.soumya.ecommerce.repository.CartRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductVariantRepository productVariantRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Cart cart;
    private ProductVariant variant;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("customer@example.com");
        user.setRole(Role.CUSTOMER);

        cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUser(user);

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Running Shoe");
        product.setPrice(BigDecimal.valueOf(2999));

        variant = new ProductVariant();
        variant.setId(UUID.randomUUID());
        variant.setColor("Black");
        variant.setSize("M");
        variant.setStockQuantity(2);
        variant.setProduct(product);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void addItem_throwsWhenRequestedQuantityExceedsStock() {

        AddToCartRequest request = new AddToCartRequest(variant.getId(), 5);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(productVariantRepository.findById(variant.getId())).thenReturn(Optional.of(variant));

        assertThatThrownBy(() -> cartService.addItem(request))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    void addItem_addsItemWhenStockIsSufficient() {

        AddToCartRequest request = new AddToCartRequest(variant.getId(), 1);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(productVariantRepository.findById(variant.getId())).thenReturn(Optional.of(variant));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toDto(cart)).thenReturn(com.soumya.ecommerce.dto.CartDTO.builder().id(cart.getId()).build());

        cartService.addItem(request);

        org.assertj.core.api.Assertions.assertThat(cart.getItems()).hasSize(1);
        org.assertj.core.api.Assertions.assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(1);
    }

    @Test
    void removeItem_throwsWhenItemNotInCart() {

        UUID missingItemId = UUID.randomUUID();

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        assertThatThrownBy(() -> cartService.removeItem(missingItemId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
