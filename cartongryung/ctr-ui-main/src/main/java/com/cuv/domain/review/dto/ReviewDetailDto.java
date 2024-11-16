package com.cuv.domain.review.dto;

import com.cuv.domain.attachment.dto.AttachmentDto;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@ToString
public class ReviewDetailDto {
    /*
     * 리뷰에서 가져오는 것
     */
    @Column(name = "board_review_id")
    private Long id;

    private String title;

    private String content;

    /** 첨부파일 JSON */
    @Column(name = "attachments_json")
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
