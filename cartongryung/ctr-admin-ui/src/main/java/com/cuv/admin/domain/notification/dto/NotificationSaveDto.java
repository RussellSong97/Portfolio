package com.cuv.admin.domain.notification.dto;

import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Getter
@Setter

@Schema(
        name = "NotificationSaveDto",
        description = "알림 저장 정보"
)
public class NotificationSaveDto {

    private Long notificationId;

    private String target;
    /**
     * FcmRequestDto의 targetToken 해당
     * memberId로 이름 및 targetToken 가져오기
     */
    private List<Long> memberId;

    private String pushStatus;

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

    private String fileUUID;

    /**
     * FcmRequestDto의 linkUrl에 해당
     */
    private String linkUrl;

    private String failReason;

    /**
     * 연월일시분 따로 받은 거 합치기
     */
    // 발송 시간
    private LocalDateTime sendAt;

    private String date;

    private String hour;

    private String minute;


    /**
     * 예약 시간
     */
    private LocalDateTime reserveAt;

    public LocalDateTime getLocalDateTime() {
        if (this.target.equals("reserve")){
          String str = this.date + this.hour +this.minute;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return  LocalDateTime.parse(str, formatter);
        }
        return LocalDateTime.now();
    }

    public LocalDateTime createSendAt(String date, String hour, String minute) {
        // date, hour, minute이 null 또는 비어있는지 확인
        if (hasText(date) && hasText(hour)  && hasText(minute)) {
            // LocalDateTime을 생성하기 위해 date, hour, minute을 결합
            String dateTimeString = date + " " + hour + ":" + minute;

            // DateTimeFormatter를 사용하여 문자열을 LocalDateTime으로 변환
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            this.sendAt = LocalDateTime.parse(dateTimeString, formatter);
            return this.sendAt;
        } else {
            this.sendAt = LocalDateTime.now();
            return this.sendAt;
        }
    }


}
