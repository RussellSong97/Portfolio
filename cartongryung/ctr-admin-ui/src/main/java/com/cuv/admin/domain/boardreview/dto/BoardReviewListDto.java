package com.cuv.admin.domain.boardreview.dto;

import com.cuv.admin.common.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Schema(
        name = "BoardReviewListDto",
        description = "이용후기 테이블 목록 출력"
)
public class BoardReviewListDto {
    /*
     * 리뷰에서 가져오는 것
     */
    private Long id;

    /** 상품 일련번호 */
    private Long productId; // 차 번호판, 차 명 끌어당기기

    private String title;

    /** 노출 여부 */
    private YN viewYn;

    /** 생성 시간 */
    private LocalDateTime createdAt;

    /*
     * productId에서 가져오는 것
     */
    private String makerName;

    private String modelName;

    private String detailGradeName;


    /** 시간 포맷터 */
    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
        return createdAt.format(formatter);
    }

}
