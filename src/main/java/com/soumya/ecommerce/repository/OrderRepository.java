package com.soumya.ecommerce.repository;

import com.soumya.ecommerce.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findByUserId(UUID userId, Pageable pageable);

    Optional<Order> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT COUNT(oi) > 0 FROM Order o JOIN o.items oi WHERE o.user.id = :userId AND oi.productId = :productId")
    boolean existsPurchaseByUserAndProduct(@Param("userId") UUID userId, @Param("productId") UUID productId);
}
