package com.cuv.domain.boardnotice.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "board_notice")
public class BoardNotice extends BaseEntity {

    /** 공지사항 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_notice_id")
    private Long id;

    /** 제목 */
    private String title;

    /** 내용 */
    private String content;

    /** 첨부파일 JSON */
    @Column(name = "attachments_json")
    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> attachment;

    /** 조회수 */
    private Long hits;

    /** 공지 여부 */
    private YN noticeYn;

    /** 노출 여부 */
    private YN viewYn;

    // 조회수 증가
    public void addHits() {
        this.hits++;
    }
}
