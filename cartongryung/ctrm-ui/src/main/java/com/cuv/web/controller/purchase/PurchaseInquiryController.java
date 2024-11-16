package com.cuv.web.controller.purchase;

import com.cuv.common.JSONResponse;
import com.cuv.domain.purchaseinquiry.PurchaseInquiryService;
import com.cuv.domain.purchaseinquiry.dto.PurchaseInquirySaveDto;
import com.cuv.domain.purchaseinquiry.enumset.ConsultationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Tag(
        name = "사용자 - 방문 상담",
        description = "사용자 - 방문 상담"
)
@Controller
@RequiredArgsConstructor
public class PurchaseInquiryController {

    private final PurchaseInquiryService purchaseInquiryService;

    /**
     * 상품 상세 > 방문 상담 예약
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "사용자 - 상품 상세 - 방문 상담",
            description = "상품 상세 > 방문 상담 예약"
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
    @PostMapping("/purchase/inquiry/write")
    @ResponseBody
    public JSONResponse<?> purchaseInquiryWriteProc(PurchaseInquirySaveDto requestDto) {
        try {
            purchaseInquiryService.purchaseInquiryWriteProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    /**
     * 마케팅 수신 정보 동의 회원
     *
     * @author jooree
     */
    @GetMapping("/purchase/inquiry/checkAgree")
    @ResponseBody
    public JSONResponse<?> searchMemberMarketingAgree() {
        try {
            purchaseInquiryService.searchMemberMarketingAgree();
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage(), true);
        }
        return new JSONResponse<>(200, "SUCCESS", false);
    }

    /**
     * 마케팅 수신 정보 동의 회원
     *
     * @author jooree
     */
    @PostMapping("/purchase/inquiry/updateAgree")
    @ResponseBody
    public JSONResponse<?> updateMemberMarketingAgree() {
        try {
            purchaseInquiryService.updateMemberMarketingAgree();
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage(), true);
        }
        return new JSONResponse<>(200, "SUCCESS", false);
    }

    /**
     * aside - 전화상담 - 연락처 없는 문의 등록
     *
     * @param requestDto 판매 문의 정보를 담은 DTO
     * @param request 요청 정보
     * @author Sangbin
     */

    @Operation(
            summary = "aside - 전화상담 - 연락처 없는 문의 등록",
            description = "연락처 없는 문의 등록"
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
    @PostMapping("/purchase/inquiry/call")
    @ResponseBody
    public JSONResponse<?> createCallInquiry(@RequestBody PurchaseInquirySaveDto requestDto, HttpServletRequest request) {
        try {
            String clientIp = getClientIp(request);
            requestDto.setConnectionIp(clientIp);
            purchaseInquiryService.saveCallPurchaseInquiry(requestDto, ConsultationType.CALL);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    /**
     * aside - 채팅상담 - 연락처 없는 문의 등록
     *
     * @param requestDto 판매 문의 정보를 담은 DTO
     * @param request 요청 정보
     * @author Sangbin
     */
    @Operation(
            summary = "aside - 채팅상담 - 연락처 없는 문의 등록",
            description = "연락처 없는 문의 등록"
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
    @PostMapping("/purchase/inquiry/chat")
    @ResponseBody
    public JSONResponse<?> createChatInquiry(@RequestBody PurchaseInquirySaveDto requestDto, HttpServletRequest request) {
        try {
            String clientIp = getClientIp(request);
            requestDto.setConnectionIp(clientIp);
            purchaseInquiryService.saveCallPurchaseInquiry(requestDto, ConsultationType.CHATTING);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
