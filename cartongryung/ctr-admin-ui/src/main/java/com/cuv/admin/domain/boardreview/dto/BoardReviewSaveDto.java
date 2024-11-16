package com.cuv.admin.domain.boardreview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "BoardReviewSaveDto",
        description = "이용후기 테이블 저장 정보"
)
public class BoardReviewSaveDto {
    @Column(name = "board_review_id")
    private Long id;
    private String title;
    private Long productId; // 아이디로 상품 번호판 가져오기
    private String viewYn;
    private String content;
    private String fileUUID;
}
