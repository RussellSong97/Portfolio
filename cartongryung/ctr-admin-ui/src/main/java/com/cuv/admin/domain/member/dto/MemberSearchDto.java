package com.cuv.admin.domain.member.dto;

import com.cuv.admin.common.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "MemberSearchDto",
        description = "회원 검색 조건"
)
public class MemberSearchDto {
    private String type;
    private String s;
    private String startDate;
    private String endDate;

    private String page;
    private String size;
    /**
     * 푸시알림 | 맴버 검색 팝업
     */
    private YN isHaveToken;
}
