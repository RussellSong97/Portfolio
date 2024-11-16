package com.cuv.domain.wishlist;

import com.cuv.domain.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface WishlistRepository extends
        JpaRepository<Wishlist, Long>,
        QuerydslPredicateExecutor<Wishlist> {

}
