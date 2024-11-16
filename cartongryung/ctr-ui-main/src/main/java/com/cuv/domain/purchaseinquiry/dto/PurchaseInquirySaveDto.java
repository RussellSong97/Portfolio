package com.cuv.domain.purchaseinquiry.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PurchaseInquirySaveDto {

    private Long productId;

    private String calendar;

    private String day;

    private String hour;

    private String minute;

    private String realName;

    private String mobileNumber;

    private String useYn;

    private String provideYn;

    private String connectionIp;

    private String inquiryNumber;
}
