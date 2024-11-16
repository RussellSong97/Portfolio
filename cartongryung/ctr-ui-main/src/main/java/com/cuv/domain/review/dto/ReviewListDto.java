package com.cuv.domain.review.dto;

import com.cuv.common.YN;
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
public class ReviewListDto {

    /*
    * 리뷰에서 가져오는 것
    */
    @Column(name = "board_review_id")
    private Long id;

    /** 관리자 회원 일련번호 */
    private Long memberAdminId; // 사진(딜러의 프사- profile_image_json- 끌어당기기 용)

    /** 상품 일련번호 */
    private Long productId; // 차 번호판, 차 명 끌어당기기

    private String title;

    private String content;

    /** 첨부파일 JSON */
    @Column(name = "attachments_json")
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto attachment;


    /** 생성 시간 */
    private LocalDateTime createdAt;


    /*
     * memberAdmin에서 가져오는 것
     */

    /** 딜러 프로필 사진 */
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto profileImageJson;



    /*
     * productId에서 가져오는 것
     */


    /** 시간 포맷터 */
    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        return createdAt.format(formatter);
    }
}
