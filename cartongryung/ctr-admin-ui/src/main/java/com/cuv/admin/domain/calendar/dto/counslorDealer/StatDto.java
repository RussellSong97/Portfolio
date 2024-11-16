package com.cuv.admin.domain.calendar.dto.counslorDealer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
@Getter
@Setter

@Schema(
        name = "StatDto",
        description = "딜러, 상담사의 상담완료, 거래완려 건수"
)
public class StatDto {
    private LocalDate calendarDate;
    private int completedCounsel;
    private int completedTrade;
    private Long id;
    private String counselorName;

    public StatDto(LocalDate calendarDate, Long id, String realName, int completedCounselCount, int completedTradeCount) {
        this.calendarDate = calendarDate;
        this.completedCounsel = completedCounselCount;
        this.completedTrade = completedTradeCount;
        this.id = id;
        this.counselorName = realName;

    }
}
