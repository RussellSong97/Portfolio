package com.cuv.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(
        name="MemberGoogleInfoDto",
        description = "앱 > 구글 로그인 시 호출되는 정보"
)
public class MemberGoogleInfoDto {

    private String idToken;

    private String email;

}
