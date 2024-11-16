package com.cuv.admin.domain.calendar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Schema(
        name = "MemberStatDto",
        description = "상담원, 딜러 통계 검색 시 사용하는 정보"
)
public class StatSearchDto {
    private List<String> status;
    private List<Long> counselor;
    private List<Long> dealer;

    private String startDate;
    private String endDate;

}
