package com.cuv.domain.saleinquiry;

import com.cuv.domain.saleinquiry.dto.SaleInquiryDetailDto;
import com.cuv.domain.saleinquiry.dto.SaleInquiryListDto;

import java.util.List;

public interface SaleInquiryRepositoryCustom {

    List<SaleInquiryListDto> searchAllSaleInquiryVehicleLists(Long memberId);

    SaleInquiryDetailDto searchSaleInquiryById(Long id);

    Long searchSaleInquiryCountByMemberId(Long memberId);

    List<SaleInquiryListDto> searchAllSaleInquiryLists(Long memberId);
}
