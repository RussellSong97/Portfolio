package com.cuv.admin.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "NotificationSaveDto",
        description = "알림 테이블 목록 출력"
)
public class NotificationListDto {
    private Long notificationId;
     private String sendStatus;
    private LocalDateTime createdAt;
    private LocalDateTime sendAt;
    private String content;
}
