package com.cuv.admin.domain.terms.dto;

import com.cuv.admin.domain.terms.enumset.TermsType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "TermsSaveDto",
        description = "약관 관리 테이블 저장 정보"
)
public class TermsSaveDto {

    private Long id;

    private String type;

    private String content;

    @Convert(converter = TermsType.TermsTypeConverter.class)
    private TermsType termsType;

}
