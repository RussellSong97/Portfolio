package com.cuv.admin.domain.product;

import com.cuv.admin.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProductRepository extends
        JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product>,
        ProductRepositoryCustom {
}
