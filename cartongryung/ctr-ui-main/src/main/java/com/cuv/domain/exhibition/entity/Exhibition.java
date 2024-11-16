package com.cuv.domain.exhibition.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.exhibition.enumset.BackgroundColorType;
import com.cuv.domain.exhibition.enumset.ExhibitionType;
import com.cuv.domain.exhibition.enumset.LinkType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Table(name = "exhibition")
public class Exhibition extends BaseEntity {

    /** 전시 관리 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exhibition_id")
    private Long id;

    /** 전시 분류 코드 (EXH: 이벤트 | 팝업 | 상단 띠 배너 | 메인 상단배너 | 메인 서브배너 | 로그인 배너) */
    @Column(name = "category_code")
    @Convert(converter = ExhibitionType.ExhibitionTypeConverter.class)
    private ExhibitionType exhibitionType;

    /** 제목 */
    private String title;

    /** 내용 */
    private String content;

    /** 링크 URL */
    private String linkUrl;

    /** 링크 URL 방식 코드 (Lnk : 현재 창 | 새창 | 링크 없음) */
    @Column(name = "link_type_code")
    @Convert(converter = LinkType.LinkTypeConverter.class)
    private LinkType linkType;

    /** pc 첨부파일 JSON */
    @Column(name = "pc_attachment_json")
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto pcAttachment;

    /** 모바일 첨부파일 JSON */
    @Column(name = "mobile_attachment_json")
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto mobileAttachment;

    /** 노출 시작일 */
    private LocalDate exhibitionStartDate;

    /** 노출 종료일 */
    private LocalDate exhibitionEndDate;

    /** 배경색 코드 */
    @Column(name = "background_color_code")
    @Convert(converter = BackgroundColorType.BackgroundColorTypeConverter.class)
    private BackgroundColorType backgroundColorType;

    /** 조회 수 */
    private Long hits;

    /** 노출 여부 */
    private YN viewYn;

    // 조회수 증가
    public void addHits() {
        this.hits++;
    }
}
