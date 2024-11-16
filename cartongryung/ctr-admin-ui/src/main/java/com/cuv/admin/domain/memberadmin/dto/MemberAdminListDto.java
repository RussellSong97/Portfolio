package com.cuv.admin.domain.memberadmin.dto;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.member.enumset.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "MemberAdminListDto",
        description = "관리자 회원 테이블 목록 출력"
)
public class MemberAdminListDto {

    private Long id;

    @Convert(converter = MemberRole.MemberRoleConverter.class)
    private MemberRole role;

    private String loginId;

    private String realName;

    private String mobileNumber;

    private YN useYn;

    private LocalDateTime lastLoginAt;

}
