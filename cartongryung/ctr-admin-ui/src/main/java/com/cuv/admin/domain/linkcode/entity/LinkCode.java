package com.cuv.admin.domain.linkcode.entity;

import com.cuv.admin.common.BaseEntity;
import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * 카이즈유 차량 코드 정보
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "link_code")
public class LinkCode extends BaseEntity {

    /** 카이즈유 차량 코드 정보 일련번호 */
    @Id
    @Column(name = "link_nbr_id")
    private Long id;

    /** 상위 연동 코드 번호 */
    private Long parentLinkNbrId;

    /** 연동 이름 */
    private String linkDataNm;

    /** 차수 */
    private Integer depth;

    /** 노출 순서 */
    private Integer viewOrder;

    /** 첨부파일 JSON */
    @Column(name = "attachment_json")
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto attachment;

    /** A/S 기한 날짜 */
    private LocalDate afterServiceDate;

    /** 노출 여부 */
    private YN viewYn;

}
