package com.cuv.admin.domain.notificatonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "NotificationMemberSaveDto",
        description = "알림 맴버 테이블 저장 정보"
)
public class NotificationMemberSaveDto {

    private Long notificationMemberId;

    private Long memberId;

    private Long notificationId;

    private String failReason;
}
