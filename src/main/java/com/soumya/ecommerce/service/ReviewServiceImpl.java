package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.ReviewDTO;
import com.soumya.ecommerce.entity.Product;
import com.soumya.ecommerce.entity.Review;
import com.soumya.ecommerce.entity.Role;
import com.soumya.ecommerce.entity.User;
import com.soumya.ecommerce.exception.BadRequestException;
import com.soumya.ecommerce.exception.DuplicateResourceException;
import com.soumya.ecommerce.exception.ResourceNotFoundException;
import com.soumya.ecommerce.mapper.ReviewMapper;
import com.soumya.ecommerce.repository.OrderRepository;
import com.soumya.ecommerce.repository.ProductRepository;
import com.soumya.ecommerce.repository.ReviewRepository;
import com.soumya.ecommerce.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewDTO addReview(ReviewDTO reviewDTO) {

        User user = SecurityUtils.getCurrentUser();

        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> ResourceNotFoundException.of("Product", reviewDTO.getProductId()));

        if (!orderRepository.existsPurchaseByUserAndProduct(user.getId(), product.getId())) {
            throw new BadRequestException("You can only review products you have purchased");
        }

        if (reviewRepository.existsByProductIdAndUserId(product.getId(), user.getId())) {
            throw new DuplicateResourceException("You have already reviewed this product");
        }

        Review review = reviewMapper.toEntity(reviewDTO, product, user);

        Review savedReview = reviewRepository.save(review);

        recalculateProductRating(product.getId());

        return reviewMapper.toDto(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> getReviewsForProduct(UUID productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable).map(reviewMapper::toDto);
    }

    @Override
    public void deleteReview(UUID reviewId) {

        User user = SecurityUtils.getCurrentUser();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> ResourceNotFoundException.of("Review", reviewId));

        boolean isOwner = review.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You can only delete your own reviews");
        }

        UUID productId = review.getProduct().getId();

        reviewRepository.delete(review);

        recalculateProductRating(productId);
    }

    private void recalculateProductRating(UUID productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> ResourceNotFoundException.of("Product", productId));

        Double averageRating = reviewRepository.findAverageRatingByProductId(productId);

        product.setRating(averageRating != null ? averageRating.floatValue() : 0f);

        productRepository.save(product);
    }
}
