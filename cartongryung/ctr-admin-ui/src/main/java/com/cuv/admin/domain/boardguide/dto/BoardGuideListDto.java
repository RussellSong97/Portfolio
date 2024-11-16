package com.cuv.admin.domain.boardguide.dto;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.boardguide.enumset.BoardGuideType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "BoardGuideListDto",
        description = "중고차 가이드 테이블 목록 출력"
)
public class BoardGuideListDto {

    private Long id;

    private String title;

    @Convert(converter = BoardGuideType.BoardGuideTypeConverter.class)
    private BoardGuideType boardGuideType;

    private LocalDateTime createdAt;

    private YN viewYn;

}
