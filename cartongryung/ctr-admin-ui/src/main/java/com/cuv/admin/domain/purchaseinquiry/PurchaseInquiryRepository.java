package com.cuv.admin.domain.purchaseinquiry;

import com.cuv.admin.domain.purchaseinquiry.entity.PurchaseInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PurchaseInquiryRepository extends
        JpaRepository<PurchaseInquiry, Long>,
        QuerydslPredicateExecutor<PurchaseInquiry>,
        PurchaseInquiryRepositoryCustom {
}
