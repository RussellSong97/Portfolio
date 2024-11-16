package com.cuv.admin.domain.wishlist;

import com.cuv.admin.domain.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends
        JpaRepository<Wishlist, Long>,
        QuerydslPredicateExecutor<Wishlist>,
        WishlistRepositoryCustom {

    @Query("SELECT w FROM Wishlist w " +
            "WHERE w.purchaseInquiryId = :id " +
            "and w.delYn = 'N'")
    Optional<List<Wishlist>> findByPurchaseInquiryId(@Param("id") Long id);
}
