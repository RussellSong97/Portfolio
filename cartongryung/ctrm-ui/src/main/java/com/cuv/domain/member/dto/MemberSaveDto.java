package com.cuv.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "MemberSaveDto",
        description = "회원 저장 정보"
)
public class MemberSaveDto {

    private String realName;
    private String emailId;
    private String emailDomain;
    private String phoneNumber;
    private String domainSelect;
    private String password;
    private String marketingYn;
}
