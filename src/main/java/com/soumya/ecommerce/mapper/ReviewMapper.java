package com.soumya.ecommerce.mapper;

import com.soumya.ecommerce.dto.ReviewDTO;
import com.soumya.ecommerce.entity.Product;
import com.soumya.ecommerce.entity.Review;
import com.soumya.ecommerce.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewDTO dto, Product product, User user) {

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        return review;
    }

    public ReviewDTO toDto(Review review) {

        return ReviewDTO.builder()
                .id(review.getId())
                .productId(review.getProduct().getId())
                .userId(review.getUser().getId())
                .userFullName(review.getUser().getFullName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
