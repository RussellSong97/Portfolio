package com.cuv.admin.domain.purchaseinquiry;

import com.cuv.admin.domain.inquirydetail.dto.InquiryDetailCountDto;
import com.cuv.admin.domain.purchaseinquiry.dto.PurchaseInquiryDetailDto;
import com.cuv.admin.domain.purchaseinquiry.dto.PurchaseInquiryListDto;
import com.cuv.admin.domain.purchaseinquiry.dto.PurchaseInquiryMemberDto;
import com.cuv.admin.domain.purchaseinquiry.dto.PurchaseInquirySearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PurchaseInquiryRepositoryCustom {
    Page<PurchaseInquiryListDto> searchAllPurchaseInquiryLists(PurchaseInquirySearchDto condition, Pageable pageable);

    Page<PurchaseInquiryListDto> searchAllWithoutContactInquiryLists(PurchaseInquirySearchDto condition, Pageable pageable);

    List<Long> searchAllPurchaseInquiryCount();

    PurchaseInquiryMemberDto searchMemberInfoByPurchaseInquiryId(Long inquiryId);

    PurchaseInquiryDetailDto searchPurchaseInquiryById(Long id);

    Long searchAllPurchaseInquiryCountByMain();

    List<InquiryDetailCountDto> searchAllPurchaseInquiryDetailCountByMain();

    List<PurchaseInquiryListDto> searchAllPurchaseInquiryByMain(PurchaseInquirySearchDto condition);

    List<PurchaseInquiryListDto> searchAllContactableInquiryExcelLists(PurchaseInquirySearchDto condition);

    List<PurchaseInquiryListDto> searchAllWithoutContactInquiryExcelLists(PurchaseInquirySearchDto condition);

    List<PurchaseInquiryListDto> searchAllVisitInquiryExcelLists(PurchaseInquirySearchDto condition);
}
