package com.cuv.admin.domain.notification.dto;

import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@Schema(
        name = "NotificationDetail",
        description = "알림 테이블 상세 정보"
)
public class NotificationDetail {

    private Long notificationId;

    private String target;
    /**
     * FcmRequestDto의 targetToken 해당
     * memberId로 이름 및 targetToken 가져오기
     */
    private List<Long> memberId;

    private String sendStatus;

    /**
     * FcmRequestDto의 title에 해당
     */
    private String title;

    /**
     * FcmRequestDto의 body에 해당
     */
    private String content;

    /**
     * FcmRequestDto의 imageUrl에 해당
     */

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto attachment;

    /**
     * FcmRequestDto의 linkUrl에 해당
     */
    private String linkUrl;

    private LocalDateTime sendAt;

    private String date;

    private String hour;

    private String minute;


    public void changeDateTimeToString(LocalDateTime sendAt) {
        if (this.target.equals("reserve")) {
            this.date = sendAt.toLocalDate().toString(); // Extract the date part as a string
            this.hour = String.format("%02d", sendAt.getHour()); // Extract and format the hour part as a string
            this.minute = String.format("%02d", sendAt.getMinute()); // Extract and format the minute part as a string
        } else {
            // Optionally, you could clear the date, hour, and minute fields if not reserved
            this.date = null;
            this.hour = null;
            this.minute = null;
        }
    }

}








