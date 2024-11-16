package com.cuv.admin.domain.calendar.dto.consult;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "ConsultSummaryDto",
        description = "상담과 관련된 총 개수 정보"
)
public class ConsultSummaryDto {
    private long totalConsultations;
    private long totalVisitReservations;
    private long totalRejections;

    public ConsultSummaryDto(long totalConsultations, long totalVisitReservations, long totalRejections) {
        this.totalConsultations = totalConsultations;
        this.totalVisitReservations = totalVisitReservations;
        this.totalRejections = totalRejections;
    }
}
