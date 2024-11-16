package com.cuv.domain.notification.entity;

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
@Table(name = "notification")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String target;

    @Column(name = "member_id")
    @Comment("회원 일련번호")
    private Long memberId;

    private String pushStatus;

    private String sendStatus;

    private String linkUrl;

    private String content;

    @Comment("첨부파일 JSON")
    @Column(name = "attachment_json")
    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> attachment;

    @Comment("실패 이유")
    private String failReason;

    @Column(name="read_yn")
    @Comment("읽음 여부")
    private YN readYn;
}
