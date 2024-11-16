package com.cuv.admin.domain.calendar.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "MemberStatDto",
        description = "월, 년별 회원 통계 정보"
)
public class MemberStatMonthAndYearDto {

    /** 시간 */
    @Column(name = "date")
    private String date;

    /** 맴버 숫자 */
    @Column(name = "count")
    private Long count;
    }
