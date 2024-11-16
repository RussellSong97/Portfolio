package com.cuv.admin.web.controller.management;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.linklisting.LinkListingService;
import com.cuv.admin.domain.linklisting.dto.LinkListingSearchDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 관리자 | 차량 관리 > 매물 연동 관리 Controller
 */
@Tag(
        name = "관리자 -> 매물 연동 관리",
        description = "관리자 -> 매물 연동 관리"
)
@Controller
@RequiredArgsConstructor
public class WebClientController {

    private final LinkListingService linkListingService;

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카매니저 가져오기
     * 매물정보 가져오기
     *
     * @param condition 날짜 정보를 담은 DTO
     * @param session   진행 상태 및 추가된 매물 건수를 담을 세션
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 >  매물 연동 관리 > 카매니저 가져오기",
            description = "관리자 매물 연동 관리 카매니저 연동"
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
    @PostMapping("/admin/management/get/linkage")
    @ResponseBody
    public JSONResponse<?> adminManagementGetLinkage(LinkListingSearchDto condition, HttpSession session) {
        try {
            linkListingService.adminManagementGetLinkage(condition, session);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS");
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카매니저 가져오기 > 상태 확인
     * 매물정보 가져오기
     *
     * @param session 진행 상태 및 추가된 매물 건수를 담을 세션
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 >  매물 연동 관리 > 카매니저 가져오기 > 상태 확인",
            description = "관리자 매물 연동 관리 카매니저 연동 시 연결 끊김 방지용 상태 값 확인"
    )
    @GetMapping("/admin/management/get/status")
    @ResponseBody
    public JSONResponse<?> adminManagementGetStatus(HttpSession session) {
        HttpSession resultSession;
        try {
            resultSession = linkListingService.adminManagementGetStatus(session);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", resultSession != null ? resultSession.getAttribute("count") : 0,
                resultSession != null ? resultSession.getAttribute("status") : "running");
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기
     *
     * @param idList  차량 정보를 조회할 시퀀스 값의 배열
     * @param session 진행 상태를 담을 세션
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 >  매물 연동 관리 > 카이즈유 가져오기",
            description = "관리자 매물 연동 관리 카이즈유 연동"
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
    @PostMapping("/admin/management/get/carInfo")
    @ResponseBody
    public JSONResponse<?> adminManagementGetCarInfo(@RequestParam("id[]") List<Long> idList, HttpSession session) throws JsonProcessingException {

        // 중고차 정보를 얻기 위한 치량번호 및 소유주 저장 (s1)
        try {
            linkListingService.adminManagementGetCarInfo(idList, session);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "성공: " + session.getAttribute("successCount") + "실패: " + session.getAttribute("failCount"));
    }
}
