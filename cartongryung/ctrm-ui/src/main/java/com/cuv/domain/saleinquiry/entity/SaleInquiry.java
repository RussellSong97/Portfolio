package com.cuv.domain.saleinquiry.entity;

import com.cuv.common.BaseEntity;
import com.cuv.domain.attachment.dto.AttachmentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 판매 문의
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "sale_inquiry")
public class SaleInquiry extends BaseEntity {

    /** 판매 문의 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_inquiry_id")
    private Long id;

    /** 회원 일련번호 */
    private Long memberId;

    /** 판매 차량 일련번호 */
    private Long saleVehicleId;

    /** 딜러 회원 일련번호 */
    private Long memberDealerId;

    /** 문의 번호 (Syymmdd-000) */
    private String inquiryNumber;

    /** 첨부파일 JSON [다중] */
    @Column(name = "attachments_json")
    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> attachments;

    /** 방문 예약 일시 */
    private LocalDateTime visitReservationAt;

}
