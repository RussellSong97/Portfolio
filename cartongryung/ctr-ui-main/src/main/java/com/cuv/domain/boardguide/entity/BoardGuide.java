package com.cuv.domain.boardguide.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.boardguide.enumset.BoardGuideType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
