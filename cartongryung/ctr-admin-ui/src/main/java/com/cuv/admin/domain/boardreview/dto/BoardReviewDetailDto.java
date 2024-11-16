package com.cuv.admin.domain.boardreview.dto;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Schema(
        name = "BoardReviewDetailDto",
        description = "이용후기 테이블 상세 출력"
)
public class BoardReviewDetailDto {

    private Long id;

    /** 상품 일련번호 */
    private Long productId; // 차 번호판, 차 명 끌어당기기

    private String title;

    /** 노출 여부 */
    private YN viewYn;

    private String content;

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto reviewAttachment;

    /** 생성 시간 */
    private LocalDateTime createdAt;

    /**
     * productId로 가져오는 것
     */

    /** 차 번호판 */
    private String carPlateNumber;

    /** 시간 포맷터 */
    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
        return createdAt.format(formatter);
    }

}
