package com.cuv.admin.domain.saleinquiry;

import com.cuv.admin.domain.saleinquiry.entity.SaleInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SaleInquiryRepository extends
        JpaRepository<SaleInquiry, Long>,
        QuerydslPredicateExecutor<SaleInquiry>,
        SaleInquiryRepositoryCustom {
}
