package com.cuv.admin.domain.notificatonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(
        name = "NotificationMemberDetailDto",
        description = "알림 맴버 테이블 상세정보 출력"
)

public class NotificationMemberDetailDto {

    private Long memberId;

    private String realName;
}
