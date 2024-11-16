package com.cuv.domain.pick.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(
        name = "PickCountDto",
        description = "픽 카운트 출력"
)
public class PickCountDto {

    private List<Long> productIds;

    private Long pickCount;

    private Long unreadCount;

}
