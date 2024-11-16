package com.cuv.admin.domain.calendar.dto.consult;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(
        name = "ConsultDto",
        description = "날짜, 상담신청, 방문예약 상담 신청, 승인불가 건수 정보"
)

public class ConsultDto {
    private LocalDate date;
    private Long consultCountAll;
    private Long reservationConsultCount;
    private Long rejectedConsultCount;

    public String changeLocalDateToString(LocalDate date) {
        return date.toString();
    }
}
