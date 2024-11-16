package com.cuv.web.controller.sale;

import com.cuv.common.JSONResponse;
import com.cuv.common.YN;
import com.cuv.domain.bizgo.AlimTalkService;
import com.cuv.domain.linkinfo.dto.LinkInfoDetailDto;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.saleinquiry.SaleInquiryService;
import com.cuv.domain.saleinquiry.dto.SaleInquiryDetailDto;
import com.cuv.domain.saleinquiry.dto.SaleInquiryListDto;
import com.cuv.domain.saleinquiry.dto.SaleInquirySaveDto;
import com.cuv.domain.terms.TermsService;
import com.cuv.domain.terms.enumset.TermsType;
import com.cuv.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(
        name = "사용자 - 내 차 팔기",
        description = "사용자 - 내 차 팔기"
)
@Controller
@RequiredArgsConstructor
public class SaleController {

    private final SaleInquiryService saleInquiryService;
    private final TermsService termsService;
    private final AlimTalkService alimTalkService;
    private final MemberRepository memberRepository;

    private final JwtUtils jwtUtils;

    /**
     * 내 차 팔기 > 내 차 목록
     *
     * @param session 토큰 정보가 담긴 세션
     * @author SungHa
     */
    @Operation(
            summary = "내 차 팔기 > 내 차 목록",
            description = "내 차 팔기에 등록한 내 차 목록"
    )
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
    @Operation(
            summary = "내 차 팔기 > 상세",
            description = "내 차 팔기에 등록한 차량 상세"
    )
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
    @Operation(
            summary = "내 차 팔기 > 메인",
            description = "내 차 팔기 메인"
    )
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
    @Operation(
            summary = "내 차 팔기 > 차량 정보",
            description = "내 차 팔기 등록 > 1단계"
    )
    @GetMapping("/sale/vehicle/first")
    public String saleVehicleFirstStep(HttpSession session ,@AuthenticationPrincipal Member member) {
        session.removeAttribute("jwtToken");

        String originalUrl = "/sale/vehicle/first";

        String encodeUrl = URLEncoder.encode(originalUrl, StandardCharsets.UTF_8)
                .replaceAll("%2F", "/");
        if (member.getAuthYn() == YN.N) return "redirect:/join/sns?redirectUrl=" + encodeUrl;

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
    @Operation(
            summary = "내 차 팔기 > 차량 정보 조회",
            description = "내 차 팔기 등록 > 1단계"
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
    @PostMapping("/api/get/carInfo")
    @ResponseBody
    public JSONResponse<?> getCarInfo(SaleInquirySaveDto requestDto, HttpSession session) {
        LinkInfoDetailDto linkInfo = new LinkInfoDetailDto();
        linkInfo.setVhrno(requestDto.getCarPlateNumber());
        linkInfo.setOwnerName(requestDto.getOwnerName());
        String jwtToken;
        try {
            if (!requestDto.getCarPlateNumber().matches("^[0-9]{2,3}[가-힣][0-9]{4}$"))
                throw new IllegalArgumentException("올바르지 않은 차량 번호입니다. 다시 입력해주세요.");

            jwtToken = jwtUtils.builder()
                    .claim("vhrno", requestDto.getCarPlateNumber())
                    .claim("brandNm", "")
                    .claim("carClassNm", "")
                    .claim("frstRegistDe", "")
                    .claim("trvlDstnc", "")
                    .claim("tireSizeFront", "")
                    .claim("ownerName", requestDto.getOwnerName())
                    .claim("linkInfoId", 0)
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
    @Operation(
            summary = "내 차 팔기 > 사진업로드",
            description = "내 차 팔기 등록 > 2단계"
    )
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
    @Operation(
            summary = "내 차 팔기 > 사진업로드",
            description = "내 차 팔기 등록 > 2단계"
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
    @Operation(
            summary = "내 차 팔기 > 약관 동의",
            description = "내 차 팔기 등록 > 3단계"
    )
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
    @Operation(
            summary = "내 차 팔기 > 약관 동의",
            description = "내 차 팔기 등록 > 3단계"
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
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid Token",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

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
    @Operation(
            summary = "내 차 팔기 > 신청완료",
            description = "내 차 팔기 등록 > 4단계"
    )
    @GetMapping("/sale/vehicle/last")
    public String saleVehicleLastStep(HttpSession session, Model model) {
        String jwtToken = (String) session.getAttribute("jwtToken");
        Map<String, Object> payload = jwtUtils.getPayload(jwtToken);
        boolean isVerified = jwtUtils.verifyToken(jwtToken);

        if (payload == null || !isVerified || !"third".equals(session.getAttribute("step"))) {
            return "redirect:/sale/vehicle";
        }

        // 알림톡 전송 부분 추가
        String realName = (String) payload.get("ownerName");
        String carPlateNumber = (String) payload.get("vhrno");

        // 회원 정보에서 연락처 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(), MemberStatus.NORMAL);
        String mobileNumber = member.getMobileNumber();

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("#{realName}", realName);
        placeholders.put("#{carPlateNumber}", carPlateNumber);
        placeholders.put("#{day}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        if (YN.Y.equals(member.getAgreeNoticeYn())) {
            alimTalkService.sendAlimTalk("code15", placeholders, mobileNumber);
        }
        return "sell/step04";
    }
}
