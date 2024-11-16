package com.cuv.admin.domain.calendar.dto.consult;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(
        name = "ConsultResponseDto",
        description = "상담과 관련된 개수정보와 그것들의 총 개수 정보"
)
public class ConsultResponseDto {
    private List<ConsultDto> consultData;
    private ConsultSummaryDto summary;
}
