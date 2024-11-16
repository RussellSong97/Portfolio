package com.cuv.web.controller.notification;

import com.cuv.common.JSONResponse;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.notification.NotificationService;
import com.cuv.domain.notification.dto.NotificationListDto;
import com.cuv.domain.notification.entity.Notification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(
        name = "사용자 - 알림",
        description = "사용자 - 알림"
)
@RequiredArgsConstructor
@Controller
public class NotificationController {
    private final NotificationService notificationService;

    /**
     * 사용자 알림 리스트
     * @author 박성민
     */
    @Operation(
            summary = "사용자 - 알림",
            description = "사용자 알림 리스트"
    )
    @GetMapping("/mypage/notification")
    public String getNotificationList(Model model) {
        // 가져오기 - entity
        List<NotificationListDto> notificationList = notificationService.getNotificationsById();

        // 반복문으로 읽음 처리로 바꾸기
        notificationService.updateReadYn();

        model.addAttribute("notificationList", notificationList);

        return "sub/notification_list";
    }

    /**
     * 알림 지우기
     *
     * @author 박성민
     */
    @Operation(
            summary = "사용자 - 알림",
            description = "알림 지우기"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

    @PostMapping("/api/sub/delete/notification")
    @ResponseBody

    public JSONResponse<?> deleteNotification(@RequestBody Map<String, Object> map) {
        // 안될 시 리스트로 받아서 반복문 돌려 제거 할 것
        String notificationIdStr = map.get("notificationId").toString();

        Long notificationId = Long.parseLong(notificationIdStr);

        notificationService.updateDelYn(notificationId);

        return new JSONResponse<>(200, "SUCCESS");
    }
}
