package com.cuv.admin.domain.linkcode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "LinkCodeSaveDto",
        description = "카이즈유 연동 코드 테이블 저장 정보"
)
public class LinkCodeSaveDto {

    private Long id;

    private Long parentLinkNbrId;

    private String linkDataNm;

    private Integer depth;

    private Integer viewOrder;

    private String fileUUID;

    private String afterServiceDate;

    private String viewYn;

}
