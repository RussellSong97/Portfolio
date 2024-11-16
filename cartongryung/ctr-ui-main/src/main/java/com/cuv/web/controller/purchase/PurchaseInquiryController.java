package com.cuv.web.controller.purchase;

import com.cuv.common.JSONResponse;
import com.cuv.domain.purchaseinquiry.PurchaseInquiryService;
import com.cuv.domain.purchaseinquiry.dto.PurchaseInquirySaveDto;
import com.cuv.domain.purchaseinquiry.enumset.ConsultationType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * 전화 아이콘 > 연락처 없는 문의
     *
     * @param requestDto 판매 문의 정보를 담은 DTO
     * @param request 요청 정보
     * @author Sangbin
     */
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
     * 채팅 아이콘 > 연락처 없는 문의
     *
     * @param requestDto 판매 문의 정보를 담은 DTO
     * @param request 요청 정보
     * @author Sangbin
     */
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
