package com.cuv.admin.domain.member.dto;

import com.cuv.admin.domain.member.enumset.MemberStatus;
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
    private Long id;
    private String phone;
    private String password;
    private String marketingYn;
    private String memberStatus;
}
