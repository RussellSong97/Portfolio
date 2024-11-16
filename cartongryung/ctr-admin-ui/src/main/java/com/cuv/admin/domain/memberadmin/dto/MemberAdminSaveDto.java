package com.cuv.admin.domain.memberadmin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(
        name = "MemberAdminSaveDto",
        description = "관리자 회원 테이블 저장 정보"
)
public class MemberAdminSaveDto {

    private Long id;

    private String loginId;

    private String password;

    private String confirmPassword;

    private String role;

    private String realName;

    private String mobileNumber;

    private String employeeNumber;

    private String fileUUID;

    private String intro;

    private List<Long> authorityMenus = new ArrayList<>();

    private String useYn;
}
