package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReviewService {

    ReviewDTO addReview(ReviewDTO reviewDTO);

    Page<ReviewDTO> getReviewsForProduct(UUID productId, Pageable pageable);

    void deleteReview(UUID reviewId);
}
