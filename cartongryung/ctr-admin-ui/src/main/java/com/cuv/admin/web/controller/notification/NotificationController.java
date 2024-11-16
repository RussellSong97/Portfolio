package com.cuv.admin.web.controller.notification;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.common.YN;
import com.cuv.admin.domain.member.MemberService;
import com.cuv.admin.domain.member.dto.MemberListDto;
import com.cuv.admin.domain.member.dto.MemberSearchDto;
import com.cuv.admin.domain.notification.NotificationService;
import com.cuv.admin.domain.notification.dto.NotificationDetail;
import com.cuv.admin.domain.notification.dto.NotificationListDto;
import com.cuv.admin.domain.notification.dto.NotificationSaveDto;
import com.cuv.admin.domain.notification.dto.NotificationSearchDto;
import com.cuv.admin.domain.notificatonMember.NotificationMemberService;
import com.cuv.admin.domain.notificatonMember.dto.NotificationMemberDetailDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Tag(
        name = "관리자 -> 회원 -> 푸시알림",
        description = "관리자 -> 회원 -> 푸시알림 경로"
)
@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final MemberService memberService;
    private final NotificationMemberService notificationMemberService;

    /**
     * 관리자 | 푸시 -> 푸시 목록
     *
     * @author 박성민
     */


    @Operation(
            summary = "관리자 | 회원 > 푸시알림 > 푸시알림 리스트",
            description = "푸시알림 리스트"
    )

    @GetMapping("/admin/member/push")
    public String selectNotificationListAll(NotificationSearchDto condition, Model model) {
        int setPage = 1;
        int setSize = 50;

        String page = condition.getPage();
        String size = condition.getSize();

        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);

        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("notificationId").descending());

        Page<NotificationListDto> notificationList = notificationService.searchAllNotificationForListing(condition, request);

        model.addAttribute("notificationList", notificationList);
        model.addAttribute("condition", condition);

        return "member/push_list";
    }

    /**
     * 관리자 | 푸시 -> 푸시 등록 폼
     *
     * @author 박성민
     */

    @Operation(
            summary = "관리자 | 회원 > 푸시알림 > 푸시알림 등록 폼",
            description = "푸시 등록 양식"
    )
    @GetMapping("/admin/member/push/write")
    public String writeNotificationForm() {
        return "member/push_write";
    }

    /**
     * 관리자 | 푸시 -> 등록하기
     * @author 박성민
     */

    @Operation(
            summary = "관리자 | 회원 > 푸시알림 > 푸시알림 등록",
            description = "푸시알림 등록하기"
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
    @PostMapping("/admin/member/push/write")
    @ResponseBody
    public JSONResponse<?> writeNotification(NotificationSaveDto notificationSaveDto) {

        JSONResponse<?> response;

        try {
            response = notificationService.writeAndSendNotification(notificationSaveDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }

    /**
     * 관리자 | 푸시 -> 푸시 디테일/수정 폼
     *
     * @param id 알림 시퀀스
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 회원 > 푸시알림 > 푸시알림 상세보기",
            description = "등록한 푸시알림 상세보기"
    )
    @GetMapping("/admin/member/push/view/{id}")
    public String selectNotificationById(@PathVariable("id") Long id, Model model) {

        NotificationDetail notificationDetail = notificationService.searchNotificationById(id);
        List<NotificationMemberDetailDto> notificationMemberDetailDtoList = notificationMemberService.findRealNameById(id);


        // notification ID 로 memberId 불러오고 그것을 이름으로 변환하기
        model.addAttribute("notificationDetail", notificationDetail);
        model.addAttribute("notificationMemberDetailDtoList", notificationMemberDetailDtoList);

        return "member/push_view";
    }

    /**
     * 관리자 | 회원 > 알림 > 수정
     * @author 박성민
     */

    @Operation(
            summary = "관리자 | 회원 > 푸시알림 > 푸시알림 수정하기",
            description = "등록한 푸시알림 수정하기"
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
    @PostMapping("/admin/member/push/edit")
    @ResponseBody
    public JSONResponse<?> editNotification(NotificationSaveDto notificationSaveDto) {
        JSONResponse<?> response;
        try {
            response = notificationService.editAndSendNotification(notificationSaveDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", response);
    }

    /**
     * 관리자 | 푸시 -> 푸시알림 작성 -> 활동회원 조회 팝업
     *
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 회원 > 푸시알림 > 푸시등록 > 토큰지닌 회원 조회하기",
            description = "등록 폼에서 회원조회하기"
    )
    @GetMapping("/admin/member/push/write/findMember")
    public String adminMember(MemberSearchDto condition, Model model) {
        int setPage = 1;
        int setSize = 50;

        String page = condition.getPage();
        String size = condition.getSize();

        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);

        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());

        condition.setIsHaveToken(YN.Y);
        Page<MemberListDto> memberList = memberService.searchGetFcmTokenMember(condition, request);


        model.addAttribute("memberList", memberList);
        model.addAttribute("condition", condition);

        return "popup/member_search";
    }
}
