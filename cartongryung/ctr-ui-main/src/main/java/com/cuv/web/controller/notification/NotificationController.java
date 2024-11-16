package com.cuv.web.controller.notification;

import com.cuv.common.JSONResponse;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.notification.NotificationService;
import com.cuv.domain.notification.dto.NotificationListDto;
import com.cuv.domain.notification.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class NotificationController {
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    /**
     * 알림 가져오기
     *
     * @author 박성민
     */
    @GetMapping("/mypage/notification")
    public String getNotificationList(Model model) {
        // 가져오기 - entity
        List<NotificationListDto> notificationList = notificationService.getNotificationsById();

        // 반복문으로 읽음 처리로 바꾸기
        notificationService.updateReadYn(notificationList);

        model.addAttribute("notificationList", notificationList);

        return "sub/notification_list";
    }

    /**
     * 알림 지우기
     *
     * @author 박성민
     */
    @PostMapping("/api/sub/delete/notification")
    @ResponseBody
    public JSONResponse<?> deleteNotification(@RequestBody Map<String, Object> map) {
        String notificationIdStr = map.get("notificationId").toString();

        Long notificationId = Long.parseLong(notificationIdStr);

        notificationService.updateDelYn(notificationId);

        return new JSONResponse<>(200, "SUCCESS");
    }
}
