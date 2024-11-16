package com.cuv.admin.domain.inquirydetail;

import com.cuv.admin.domain.inquirydetail.dto.InquiryDetailListDto;

import java.util.List;

public interface InquiryDetailRepositoryCustom {
    List<InquiryDetailListDto> searchAllInquiryDetailListsByTradeType(Long id, String tradeType);

}
