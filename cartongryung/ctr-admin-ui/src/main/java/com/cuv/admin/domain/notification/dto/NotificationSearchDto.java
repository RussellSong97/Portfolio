package com.cuv.admin.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "NotificationSearchDto",
        description = "알림 검색 정보"
)
public class NotificationSearchDto {
    private String s;
    private String startDate;
    private String endDate;

    private String page;
    private String size;
}
