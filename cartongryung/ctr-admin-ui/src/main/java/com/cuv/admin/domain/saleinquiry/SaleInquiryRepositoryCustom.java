package com.cuv.admin.domain.saleinquiry;

import com.cuv.admin.domain.inquirydetail.dto.InquiryDetailCountDto;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquiryDetailDto;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquiryListDto;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquirySearchDto;
import com.cuv.admin.domain.salevehicle.dto.SaleVehicleDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SaleInquiryRepositoryCustom {

    Page<SaleInquiryListDto> searchAllSaleInquiryLists(SaleInquirySearchDto condition, Pageable pageable);

    SaleInquiryDetailDto searchSaleInquiryById(Long id);

    SaleVehicleDetailDto searchSaleVehicleById(Long id);

    Long searchAllSaleInquiryCountByMain();

    List<InquiryDetailCountDto> searchAllSaleInquiryDetailCountByMain();

    List<SaleInquiryListDto> searchAllSaleInquiryByMain();

    List<SaleInquiryListDto> searchAllSaleInquiryExcelLists(SaleInquirySearchDto condition);

}
