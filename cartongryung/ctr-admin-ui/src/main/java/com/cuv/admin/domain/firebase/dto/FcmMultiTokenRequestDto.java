package com.cuv.admin.domain.firebase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@Schema(
        name = "FcmMultiTokenRequestDto",
        description = "fcm에서 다중 메시지 생성할 때 사용하는 정보"
)
public class FcmMultiTokenRequestDto {
    private String title;
    private String body;
    private String imageUrl;
    private String targetToken; // 보낼 대상
    private String linkUrl;
}
