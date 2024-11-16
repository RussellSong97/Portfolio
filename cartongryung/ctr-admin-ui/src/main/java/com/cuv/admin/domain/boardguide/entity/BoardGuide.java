package com.cuv.admin.domain.boardguide.entity;

import com.cuv.admin.common.BaseEntity;
import com.cuv.admin.common.YN;
import com.cuv.admin.domain.boardguide.enumset.BoardGuideType;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 게시판 중고차 가이드
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "board_guide")
public class BoardGuide extends BaseEntity {

    /** 중고차 가이드 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_guide_id")
    private Long id;

    /** 제목 */
    private String title;

    /** 중고차 가이드 분류 코드 (안내, 판매, 구매) */
    @Column(name = "category_code")
    @Convert(converter = BoardGuideType.BoardGuideTypeConverter.class)
    private BoardGuideType boardGuideType;

    /** 내용 */
    private String content;

    /** 첨부파일 JSON */
    @Column(name = "attachment_json")
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto attachment;

    /** 노출 여부 */
    private YN viewYn;

}
