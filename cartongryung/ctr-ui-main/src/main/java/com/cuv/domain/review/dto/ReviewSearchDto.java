package com.cuv.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * 페이지 관련된 것을 담을 객체
 */
public class ReviewSearchDto {
    private String page;
    private String size;
}
