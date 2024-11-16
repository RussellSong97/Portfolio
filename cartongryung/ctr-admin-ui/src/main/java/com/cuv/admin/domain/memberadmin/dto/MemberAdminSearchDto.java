package com.cuv.admin.domain.memberadmin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "MemberAdminSearchDto",
        description = "관리자 회원 검색 조건"
)
public class MemberAdminSearchDto {

    private String role;

    private String page;
    private String size;
}
