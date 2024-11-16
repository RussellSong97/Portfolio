package com.cuv.domain.product_copy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiProductDto {
    // 제휴사코드
    private String joinCode;
    // 요청자타입
    private String sType;
    // 제휴사회원ID
    private String memberID;
    // 차량번호
    private String carnum;
    // 차량번호 구분
    private String carNumType;
    // 기준일자
    private String stdDate;
    //회신양식
    private String rType;
}
