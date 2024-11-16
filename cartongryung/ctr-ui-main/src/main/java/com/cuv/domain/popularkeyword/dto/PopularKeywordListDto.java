package com.cuv.domain.popularkeyword.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class PopularKeywordListDto {

    private Long makerCodeId;

    private Long modelCodeId;

    private Long detailModelCodeId;

    private String makerName;

    private String modelName;

    private String detailModelName;

    private LocalDateTime createdAt;
}
