package com.cuv.admin.domain.boardfaq.dto;

import com.cuv.admin.common.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "BoardFaqDetailDto",
        description = "자주 묻는 질문 테이블 상세 출력"
)
public class BoardFaqDetailDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private YN viewYn;
}
