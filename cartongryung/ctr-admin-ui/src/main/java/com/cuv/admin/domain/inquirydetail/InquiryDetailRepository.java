package com.cuv.admin.domain.inquirydetail;

import com.cuv.admin.domain.inquirydetail.entity.InquiryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface InquiryDetailRepository extends
        JpaRepository<InquiryDetail, Long>,
        QuerydslPredicateExecutor<InquiryDetail>,
        InquiryDetailRepositoryCustom {
}
