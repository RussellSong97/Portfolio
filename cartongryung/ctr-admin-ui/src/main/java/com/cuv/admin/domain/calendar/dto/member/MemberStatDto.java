package com.cuv.admin.domain.calendar.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(
        name = "MemberStatDto",
        description = "일별 회원 통계 정보"
)
public class MemberStatDto {

    /** 시간 */
    @Column(name = "date")
    private LocalDate date;

    /** 맴버 숫자 */
    @Column(name = "count")
    private Long count;

    /**
     * 엑셀 다운에 적용할 메소드
     */
    public String changeLocalDateToString(LocalDate date) {
        return date.toString();
    }
}
