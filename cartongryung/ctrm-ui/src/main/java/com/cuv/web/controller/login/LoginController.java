package com.cuv.web.controller.login;

import com.cuv.common.JSONResponse;
import com.cuv.domain.exhibition.ExhibitionService;
import com.cuv.domain.exhibition.dto.ExhibitionListDto;
import com.cuv.domain.exhibition.enumset.ExhibitionType;
import com.cuv.domain.member.MemberService;
import com.cuv.domain.member.dto.MemberGoogleInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Tag(
        name = "사용자 - 로그인",
        description = "사용자 - 로그인"
)
@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final ExhibitionService exhibitionService;
    private final MemberService memberService;

    /**
     * 로그인
     * @param request 보험 이력 조회시 리턴 url
     * @see com.cuv.domain.linkhistory.LinkHistoryService - searchApiCarHistory
     * @author 송다운
     */
    @Operation(
            summary = "사용자 - 로그인",
            description = "로그인"
    )
    @GetMapping("/login")
    public String login(Model model, HttpSession session , HttpServletRequest request) {
        // 로그인 배너
        String errorMessage = (String) session.getAttribute("errorMessage");
        String errorType = (String) session.getAttribute("errorType");
        List<ExhibitionListDto> loginBanner = exhibitionService.searchAllExhibition(ExhibitionType.LOGIN.getCode());

        model.addAttribute("loginBanner", loginBanner);

        String redirectUrl = request.getParameter("redirectUrl");

        if (redirectUrl != null && !redirectUrl.contains("/login")) {
            session.setAttribute("redirectUrl", redirectUrl);
        }

        if(errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("errorType", errorType);
            session.removeAttribute("errorMessage");
            session.removeAttribute("errorType");
        }
        return "member/login";
    }

    @GetMapping("/join")
    public String join(Model model, HttpSession session , HttpServletRequest request) {
        // 로그인 배너
        String errorMessage = (String) session.getAttribute("errorMessage");
        String errorType = (String) session.getAttribute("errorType");
        List<ExhibitionListDto> loginBanner = exhibitionService.searchAllExhibition(ExhibitionType.LOGIN.getCode());

        model.addAttribute("loginBanner", loginBanner);

        String redirectUrl = request.getParameter("redirectUrl");

        if (redirectUrl != null && !redirectUrl.contains("/join")) {
            session.setAttribute("redirectUrl", redirectUrl);
        }

        if(errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("errorType", errorType);
            session.removeAttribute("errorMessage");
            session.removeAttribute("errorType");
        }
        return "member/join";
    }

    /**
     * 이메일 로그인
     * @author 송다운
     */
    @Operation(
            summary = "사용자 - 이메일 로그인",
            description = "이메일 로그인"
    )
    @GetMapping("/login/email")
    public String loginEmail(HttpSession session, Model model) {
        String errorMessage = (String) session.getAttribute("errorMessage");
        if(errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }
        return "member/login_email";
    }

    /**
     * 모바일 앱 구글 로그인
     * @param googleInfoDto 앱 구글 로그인 이후 떨어진 정보
     * @param request 로그인 정보를 저장한 요청
     * @author SungHa
     */
    @Operation(
            summary = "사용자 - 로그인",
            description = "모바일 앱 구글 로그인"
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
    @PostMapping("/login/app/google")
    @ResponseBody
    public JSONResponse<?> loginAppGoogle(@RequestBody MemberGoogleInfoDto googleInfoDto, HttpServletRequest request) {
        try {
            memberService.loginAppGoogle(googleInfoDto, request);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", googleInfoDto);
    }

}
