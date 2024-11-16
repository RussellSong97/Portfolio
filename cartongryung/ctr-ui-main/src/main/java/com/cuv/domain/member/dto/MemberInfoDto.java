package com.cuv.domain.member.dto;

import com.cuv.domain.member.enumset.RegType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfoDto {
    private String email;
    private String mobileNumber;
    private String realName;
    private RegType regCode;
}
