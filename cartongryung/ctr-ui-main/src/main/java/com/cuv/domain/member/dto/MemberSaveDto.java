package com.cuv.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSaveDto {

    private String realName;
    private String emailId;
    private String emailDomain;
    private String phoneNumber;
    private String domainSelect;
    private String password;
    private String marketingYn;
}
