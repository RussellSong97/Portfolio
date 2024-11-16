package com.cuv.web.controller.sale;

import com.cuv.common.JSONResponse;
import com.cuv.domain.linkinfo.dto.LinkInfoDetailDto;
import com.cuv.domain.linklisting.LinkListingService;
import com.cuv.domain.saleinquiry.SaleInquiryService;
import com.cuv.domain.saleinquiry.dto.SaleInquiryDetailDto;
import com.cuv.domain.saleinquiry.dto.SaleInquiryListDto;
import com.cuv.domain.saleinquiry.dto.SaleInquirySaveDto;
import com.cuv.domain.terms.TermsService;
import com.cuv.domain.terms.enumset.TermsType;
import com.cuv.util.JwtUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SaleController {

    private final LinkListingService linkListingService;
    private final SaleInquiryService saleInquiryService;
    private final TermsService termsService;

    private final JwtUtils jwtUtils;

    /**
     * 내 차 팔기 > 내 차 목록
     *
     * @param session 토큰 정보가 담긴 세션
     * @author SungHa
     */
    @GetMapping("/sale/vehicle")
    public String saleVehicleList(HttpSession session, Model model) {
        session.removeAttribute("jwtToken");

        List<SaleInquiryListDto> saleInquiryLists = saleInquiryService.searchAllSaleInquiryVehicleLists();

        model.addAttribute("saleInquiryLists", saleInquiryLists);

        return "sell/vehicle_list";
    }

    /**
     * 내 차 팔기 > 상세
     *
     * @param id 판매 문의 시퀀스
     * @author SungHa
     */
    @GetMapping("/sale/vehicle/{id}")
    public String saleVehicleList(@PathVariable("id") Long id, Model model) {
        SaleInquiryDetailDto saleInquiry = saleInquiryService.searchSaleInquiryById(id);

        if (saleInquiry == null)
            return "redirect:/sale/vehicle";

        model.addAttribute("saleInquiry", saleInquiry);

        return "sell/vehicle_view";
    }

    /**
     * 내 차 팔기 > 메인
     *
     * @author SungHa
     */
    @GetMapping("/sale/intro")
    public String saleVehicleIntro() {
        return "sell/intro";
    }

    /**
     * 내 차 팔기 > 차량정보
     *
     * @param session 토큰 정보가 담긴 세션
     * @author SungHa
     */
    @GetMapping("/sale/vehicle/first")
    public String saleVehicleFirstStep(HttpSession session) {
        session.removeAttribute("jwtToken");

        return "sell/step01";
    }

    /**
     * 내 차 팔기 > 차량 정보 조회
     * 카이즈유 중고차 정보 조회 (s1)
     *
     * @param requestDto 차량 정보를 조회할 데이터를 담은 DTO
     * @param session    토큰 정보가 담긴 세션
     * @author SungHa
     */
    @PostMapping("/api/get/carInfo")
    @ResponseBody
    public JSONResponse<?> getCarInfo(SaleInquirySaveDto requestDto, HttpSession session) {
        LinkInfoDetailDto linkInfo;
        String jwtToken;
        try {
            linkInfo = linkListingService.getCarInfo(requestDto);
            jwtToken = jwtUtils.builder()
                    .claim("vhrno", linkInfo.getVhrno())
                    .claim("brandNm", linkInfo.getBrandNm())
                    .claim("carClassNm", linkInfo.getCarClassNm())
                    .claim("frstRegistDe", linkInfo.getFrstRegistDe())
                    .claim("trvlDstnc", linkInfo.getTrvlDstnc())
                    .claim("tireSizeFront", linkInfo.getTireSizeFront())
                    .claim("ownerName", linkInfo.getOwnerName())
                    .claim("linkInfoId", linkInfo.getLinkInfoId())
                    .build();

            session.setAttribute("jwtToken", jwtToken);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", linkInfo, jwtToken);
    }

    /**
     * 내 차 팔기 > 사진업로드
     *
     * @param session 토큰 정보가 담긴 세션
     * @author SungHa
     */
    @GetMapping("/sale/vehicle/second")
    public String saleVehicleSecondStep(HttpSession session, Model model) {
        String jwtToken = (String) session.getAttribute("jwtToken");

        Map<String, Object> payload = jwtUtils.getPayload(jwtToken);
        boolean isVerified = jwtUtils.verifyToken(jwtToken);

        if (payload == null || !isVerified) {
            return "redirect:/sale/vehicle";
        }

        model.addAttribute("payload", payload);
        model.addAttribute("jwtToken", jwtToken);

        return "sell/step02";
    }

    /**
     * 내 차 팔기 > 사진업로드
     *
     * @param requestDto 등록할 정보가 담긴 DTO
     * @param session    토큰 정보가 담긴 세션
     * @author SungHa
     */
    @PostMapping("/sale/vehicle/second")
    @ResponseBody
    public JSONResponse<?> saleVehicleSecondStep(SaleInquirySaveDto requestDto, HttpSession session) {
        try {
            Map<String, Object> payload = jwtUtils.getPayload(requestDto.getJwtToken());
            boolean isVerified = jwtUtils.verifyToken(requestDto.getJwtToken());

            if (payload == null || !isVerified) {
                return new JSONResponse<>(400, "Invalid Token");
            }

            payload = saleInquiryService.saleVehicleSecondStep(requestDto, payload);
            String jwtToken = jwtUtils.createToken(payload);
            session.setAttribute("jwtToken", jwtToken);
            session.setAttribute("step", "second");

            return new JSONResponse<>(200, "SUCCESS", jwtToken);

        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
    }

    /**
     * 내 차 팔기 > 약관동의
     *
     * @param session 토큰 정보가 담긴 세션
     * @author SungHa
     */
    @GetMapping("/sale/vehicle/third")
    public String saleVehicleThirdStep(HttpSession session, Model model) {
        String jwtToken = (String) session.getAttribute("jwtToken");
        Map<String, Object> payload = jwtUtils.getPayload(jwtToken);
        boolean isVerified = jwtUtils.verifyToken(jwtToken);

        if (payload == null || !isVerified || !"second".equals(session.getAttribute("step"))) {
            return "redirect:/sale/vehicle";
        }

        // 개인정보처리방침
        String privacy = termsService.searchAllTerms(TermsType.PRIVACY);

        model.addAttribute("payload", payload);
        model.addAttribute("jwtToken", jwtToken);
        model.addAttribute("privacy", privacy);

        return "sell/step03";
    }

    /**
     * 내 차 팔기 > 약관동의
     *
     * @param requestDto 등록할 정보가 담긴 DTO
     * @param session    토큰 정보가 담긴 세션
     * @author SungHa
     */
    @PostMapping("/sale/vehicle/third")
    @ResponseBody
    public JSONResponse<?> saleVehicleThirdStep(SaleInquirySaveDto requestDto, HttpSession session) {
        try {
            Map<String, Object> payload = jwtUtils.getPayload(requestDto.getJwtToken());
            boolean isVerified = jwtUtils.verifyToken(requestDto.getJwtToken());

            if (payload == null || !isVerified) {
                return new JSONResponse<>(400, "Invalid Token");
            }

            payload = saleInquiryService.saleVehicleThirdStep(requestDto, payload);
            String jwtToken = jwtUtils.createToken(payload);
            session.setAttribute("jwtToken", jwtToken);
            session.setAttribute("step", "third");

            return new JSONResponse<>(200, "SUCCESS", jwtToken);

        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
    }

    /**
     * 내 차 팔기 > 신청완료
     *
     * @param session 토큰 정보가 담긴 세션
     * @author SungHa
     */
    @GetMapping("/sale/vehicle/last")
    public String saleVehicleLastStep(HttpSession session, Model model) {
        String jwtToken = (String) session.getAttribute("jwtToken");
        Map<String, Object> payload = jwtUtils.getPayload(jwtToken);
        boolean isVerified = jwtUtils.verifyToken(jwtToken);

        if (payload == null || !isVerified || !"third".equals(session.getAttribute("step"))) {
            return "redirect:/sale/vehicle";
        }

        return "sell/step04";
    }
}
