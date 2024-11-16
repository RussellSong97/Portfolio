package com.cuv.domain.purchaseinquiry;

import com.cuv.domain.purchaseinquiry.dto.PurchaseInquiryListDto;

import java.util.List;

public interface PurchaseInquiryRepositoryCustom {
    Long searchPurchaseInquiryCountByMemberId(Long memberId);

    Long searchVisitReservationCountByMemberId(Long memberId);

    List<PurchaseInquiryListDto> searchPurchaseInquiryByMemberId(Long memberId);

    List<PurchaseInquiryListDto> searchVisitReservationByMemberId(Long memberId);
}
