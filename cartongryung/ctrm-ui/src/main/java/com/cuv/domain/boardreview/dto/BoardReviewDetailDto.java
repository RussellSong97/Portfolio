package com.cuv.domain.boardreview.dto;

import com.cuv.domain.attachment.dto.AttachmentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@Schema(
        name = "BoardReviewDetailDto",
        description = "이용후기 테이블 상세 출력"
)
public class BoardReviewDetailDto {
    /*
     * 리뷰에서 가져오는 것
     */
    private Long id;

    private String title;

    private String content;

    /** 첨부파일 JSON */
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto attachment;

    /** 생성 시간 */
    private LocalDateTime createdAt;

    /** 시간 포맷터 */
    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        return createdAt.format(formatter);
    }
}
