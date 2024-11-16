package com.cuv.admin.web.scheduler;

import com.cuv.admin.domain.linklisting.LinkListingService;
import com.cuv.admin.domain.notification.NotificationService;
import com.cuv.admin.domain.notification.dto.NotificationSaveDto;
import com.cuv.admin.domain.notification.entity.Notification;
import com.cuv.admin.domain.notificationIds.NotificationIdsService;
import com.cuv.admin.domain.notificationIds.entity.NotificationIds;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Tag(
        name = "관리자 -> 회원 -> 푸시알림 스케줄러",
        description = "관리자 -> 회원 -> 푸시알림 스케줄러"
)
@Component
@RequiredArgsConstructor
@EnableAsync
@Slf4j
public class NotificationScheduler {

    private final NotificationService notificationService;
    private final NotificationIdsService notificationIdsService;

    @Operation(
            summary = "관리자 | 회원 > 예약한 푸시알림 보내기",
            description = "관리자 예약 발송에 해당하는 알림 및 알림 보내야하는 사람 정보 가져오기"
    )
//    todo: 실제로 운영시엔 크론으로 넣기
    @Scheduled(cron = "0 0/5 * * * ?")
//    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void getReservedNotificationList() throws InterruptedException {
        log.info("getReservedNotificationList starting......");

        // 알림 전부 가져오기
        // 개인인지 전부인지 확인

        List<NotificationSaveDto> notificationList = notificationService.getScheduledNotification();

        // 개인에 대한 ids 값을 가져와야함
        for (NotificationSaveDto notification : notificationList) {
            // 개인에게 보내는 메서드 사용하기
            if (notification.getTarget().equals("personal")) {
                List<Long> memberId = notificationIdsService.getScheduledNotification(notification.getNotificationId());
                notification.setMemberId(memberId);
                // 예약 상태 right로 바꾸기
                notification.setSendStatus("right");
                notificationService.writeAndSendNotification(notification);
            } else {
                notification.setSendStatus("right");
                notificationService.writeAndSendNotification(notification);
            }
        }


        log.info("getReservedNotificationList End ***************************");
    }


}
