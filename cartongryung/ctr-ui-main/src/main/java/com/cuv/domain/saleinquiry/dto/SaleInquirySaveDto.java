package com.cuv.domain.saleinquiry.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SaleInquirySaveDto {

    private String carPlateNumber;

    private String ownerName;

    private String jwtToken;

    private List<String> fileUUIDs = new ArrayList<>();

    private String agreeUseYn;

}
