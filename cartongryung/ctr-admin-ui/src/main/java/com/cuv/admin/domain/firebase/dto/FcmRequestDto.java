package com.cuv.admin.domain.firebase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(
        name = "FcmRequestDto",
        description = "fcm에서 단일 메시지 생성할 때 사용하는 정보"
)
public class FcmRequestDto {
    private String title;
    private String body;
    private String imageUrl;
    private String targetToken; // 보낼 대상
    private String linkUrl;

}
