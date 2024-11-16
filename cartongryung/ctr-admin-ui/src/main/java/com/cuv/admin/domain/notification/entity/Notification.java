package com.cuv.admin.domain.notification.entity;

import com.cuv.admin.common.BaseEntity;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.List;
/**
 * 알림
 */
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "notification")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    /**
     * 알림 받는 사람
     * all: 전부
     * personal: 개인
     */
    private String target;

    private Long memberId;

    /**
     * 발송상태
     */
    private String pushStatus;

    /**
     * 언제 발송할지 여부
     */
    private String sendStatus;

    private String linkUrl;

    private String title;

    private String content;

    @Column(name = "attachment_json")
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto attachment;

    /**
     * 발송 여부
     */
    @Column(name="read_yn")
    private YN readYn;

    /**
     * 발송 시간
     */
    private LocalDateTime sendAt;

    /**
     * 예약 시간
     */

    private LocalDateTime reserveAt;
}
