package com.cuv.admin.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(
        name = "MemberWithdrawDto",
        description = "회원 탈퇴 정보 출력"
)
public class MemberWithdrawDto {
    private String withdrawReason;
    private LocalDateTime withdrawAt;
}
