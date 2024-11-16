package com.cuv.admin.domain.purchaseinquiry.dto;

import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "PurchaseInquiryMemberDto",
        description = "연락가능문의 등록 팝업 > 회원 정보 출력"
)
public class PurchaseInquiryMemberDto {

    private Long id;

    private Long memberId;

    private String realName;

    private String mobileNumber;

    private String email;

    private String memberRealName;

    private String memberMobileNumber;

    private String memberEmail;

    @Convert(converter = ConsultationType.ConsultationTypeConverter.class)
    private ConsultationType consultationTypeCode;

    private String saveConsultationTypeCode;

}
