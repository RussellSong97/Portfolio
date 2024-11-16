package com.cuv.domain.review.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "board_review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_review_id")
    private Long id;

    /** 관리자 회원 일련번호 */
    private Long memberAdminId; // 사진(딜러의 프사- profile_image_json- 끌어당기기 용)

    /** 상품 일련번호 */
    private Long productId; // 차 번호판, 차 명 끌어당기기 

    /** 제목 */
    private String title;

    /** 상품 일련번호 */
    private String content;

    /** 첨부파일 JSON */
    @Column(name = "attachments_json")
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto attachment;

    /** 노출 여부 */
    private YN viewYn;

}
