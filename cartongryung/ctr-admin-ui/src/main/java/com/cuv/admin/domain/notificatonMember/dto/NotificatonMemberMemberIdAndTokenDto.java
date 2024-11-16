package com.cuv.admin.domain.notificatonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "NotificatonMemberMemberIdAndTokenDto",
        description = "알림 맴버의 맴버아이디와 맴버의 토큰정보"
)
public class NotificatonMemberMemberIdAndTokenDto {
    private Long memberId;
    private String token;

}
