package com.cuv.domain.quickinquiry;

import com.cuv.domain.quickinquiry.entity.QuickInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuickInquiryRepository extends JpaRepository<QuickInquiry, Long> {
}
