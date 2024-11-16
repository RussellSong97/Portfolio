package com.cuv.admin.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "MemberAdminMemoListDto",
        description = "회원 상세 메모 목록 출력"
)
public class MemberAdminMemoListDto {
    private Long id;
    private Long memberId;
    private String content;
    private LocalDateTime createdAt;
}
