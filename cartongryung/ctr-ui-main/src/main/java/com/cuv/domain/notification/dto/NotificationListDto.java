package com.cuv.domain.notification.dto;

import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@ToString
public class NotificationListDto {
    @Column(name = "notification_id")
    private Long notificationId;

    private String target;

    @Column(name = "member_id")
    @Comment("회원 일련번호")
    private Long memberId;

    private String linkUrl;

    private String content;

    @Comment("첨부파일 JSON")
    @Column(name = "attachment_json")
    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> attachment;

    @Column(name="read_yn")
    @Comment("읽음 여부")
    private YN readYn;

    private LocalDateTime createdAt;

    // 지우지 말 것
    // 시간 포맷터 
    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
        return createdAt.format(formatter);
    }
}
