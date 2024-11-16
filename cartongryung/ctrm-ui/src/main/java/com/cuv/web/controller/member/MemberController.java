package com.cuv.web.controller.member;

import com.cuv.common.JSONResponse;
import com.cuv.common.YN;
import com.cuv.domain.member.MemberService;
import com.cuv.domain.member.dto.MemberSaveDto;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.terms.TermsService;
import com.cuv.domain.terms.enumset.TermsType;
import com.cuv.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(
        name = "회원 -> 회원",
        description = "회원 -> 회원"
)
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final TermsService termsService;

    private final JwtUtils jwtUtils;
    /**
     * 회원가입 페이지
     * @return
     * @author 송다운
     */
    @Operation(
            summary = "사용자 - 회원가입",
            description = "회원가입 페이지"
    )
    @GetMapping("/join/step1")
    public String joinStep1(Model model) {
        // 사용자 이용약관
        String service = termsService.searchAllTerms(TermsType.SERVICE);

        // 개인정보처리방침
        String privacy = termsService.searchAllTerms(TermsType.PRIVACY);

        model.addAttribute("service", service);
        model.addAttribute("privacy", privacy);

        return "member/join_write";
    }


    /**
     * 회원가입 | 회원 가입 디비 저장
     * @param dto 회원가입 정보
     * @return
     * @author 송다운
     */
    @Operation(
            summary = "사용자 - 회원가입",
            description = "회원가입 | 회원 가입 디비 저장"
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
    @PostMapping("/join/save")
    @ResponseBody
    public JSONResponse<?> joinSave(MemberSaveDto dto) {
        JSONResponse<?> response;

        try {
            response = memberService.saveJoin(dto);
        } catch (Exception e) {
            return new JSONResponse<>(500, "회원가입에 실패하였습니다.");
        }
        return response;
    }


    /**
     * 회원가입 | 휴대폰 인증 번호 전송
     * @param map 휴대폰 인증 번호 전송을 위한 전화번호 정보
     * @param authentication 로그인 정보
     * @return
     */

    @Operation(
            summary = "사용자 - 회원가입",
            description = "회원가입 | 휴대폰 인증 번호 전송"
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
    @PostMapping("/api/join/check/phone")
    @ResponseBody
    public JSONResponse<?> checkPhone(@RequestBody Map<String, Object> map, Authentication authentication) {
        JSONResponse<?> response;
        try {
            response = memberService.checkPhone(map, authentication);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }

    /**
     * 회원 가입 인증 번호 확인
     * @param map 인증번호 확인을 위한 인증 번호
     * @return
     */
    @Operation(
            summary = "사용자 - 회원가입",
            description = "회원 가입 인증 번호 확인"
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

    @PostMapping("/api/join/number/check")
    @ResponseBody
    public JSONResponse<?> checkNumber(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = memberService.checkNumber(map);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }

    /**
     * 회원가입 | 완표 페이지
     * @return
     */
    @Operation(
            summary = "사용자 - 회원가입 완료 페이지",
            description = "회원가입 | 완표 페이지"
    )
    @GetMapping("/join/complete")
    public String joinComplete() {
        return "member/join_completed";
    }


    /**
     * 비밀번호 찾기 페이지
     * @return
     */
    @Operation(
            summary = "사용자 - 비밀번호 찾기",
            description = "비밀번호 찾기 페이지"
    )
    @GetMapping("/find/password")
    public String findPassword() {
        return "member/find_password";
    }

    /**
     * 비밀번호 찾기 메일 쏘기
     * @param map 비밀번호 찾기 메일 쏘기 위해서 메일 정보
     * @return
     */
    @Operation(
            summary = "사용자 - 비밀번호 찾기",
            description = "비밀번호 찾기 메일 쏘기"
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

    @PostMapping("/find/password/email")
    @ResponseBody
    public JSONResponse<?> searchFindPassword(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = memberService.searchFindPasswordEmail(map);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }

    /**
     * 비밀번호 찾기 > 이메일 전송 완료
     * @author SungHa
     */
    @Operation(
            summary = "사용자 - 비밀번호 찾기",
            description = "비밀번호 찾기 > 이메일 전송 완료"
    )
    @GetMapping("/find/password/complete")
    public String findPasswordComplete() {
        return "member/find_completed";
    }

    /**
     * 비밀번호 찾기 | 비밀번호 재설정 페이지
     * @param jwtToken 비밀번호 재설정을 위한 jwtToken
     * @return
     */
    @Operation(
            summary = "사용자 - 비밀번호 찾기 - 재설정 페이지",
            description = "비밀번호 찾기 | 비밀번호 재설정 페이지"
    )
    @GetMapping("/find/password/reset")
    public String findPasswordReset(@RequestParam("token") String jwtToken, Model model) {

        Map<String, Object> payload = jwtUtils.getPayload(jwtToken);
        if (payload == null || Boolean.TRUE.equals(payload.get("checkData"))) {
            return "redirect:/login";
        }

        model.addAttribute("jwtToken", jwtToken);

        return "member/password_reset";
    }

    /**
     * 비밀번호 재설정 저장
     * @param map 비밀번호 재설정을 위한 정보들
     * @return
     */
    @Operation(
            summary = "사용자 - 비밀번호 찾기 - 재설정 페이지",
            description = "비밀번호 재설정 저장"
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

    @PostMapping("/find/password/reset")
    @ResponseBody
    public JSONResponse<?> searchFindPasswordReset(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = memberService.searchFindPasswordReset(map);
        } catch (Exception e) {
            return new JSONResponse<>(500, "FALSE");
        }
        return response;
    }

    /**
     * sns로 로그인 했을때 본인인증 페이지
     * @return
     */
    @Operation(
            summary = "사용자 - sns 본인인증",
            description = "sns로 로그인 했을때 본인인증 페이지"
    )
    @GetMapping("/join/sns")
    public String joinSns(@AuthenticationPrincipal Member member ,
                          @RequestParam(required = false) String redirectUrl , Model model) {

        if (member == null) return "redirect:/login";

        if (member.getAuthYn() == YN.Y)  return  "redirect:/";

        // 사용자 이용약관
        String service = termsService.searchAllTerms(TermsType.SERVICE);
        // 개인정보처리방침
        String privacy = termsService.searchAllTerms(TermsType.PRIVACY);

        model.addAttribute("service", service);
        model.addAttribute("privacy", privacy);
        model.addAttribute("redirectUrl", redirectUrl);
        return "member/join_sns";
    }

    /**
     * 본인 인증 저장 (mobileNumber 업데이트)
     * @param map sns 회원가입 본인인증 정보
     * @return
     */
    @Operation(
            summary = "사용자 - sns 본인인증",
            description = "본인 인증 저장 (mobileNumber 업데이트)"
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
    @PostMapping("/join/sns/save")
    @ResponseBody
    public  JSONResponse<?> joinSnsSave(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = memberService.saveJoinSns(map);
        } catch (Exception e) {
            return new JSONResponse<>(500, "회원가입에 실패하였습니다.");
        }
        return response;
    }

    /**
     * 마이페이지 인증 번호 확인(회원 휴대폰 번호 업데이트, 인증 여부 업데이트)
     * @param map 휴대폰 번호 업데이트를 위한 정보 (휴대폰 번호)
     * @param authentication 로그인 정보
     */
    @Operation(
            summary = "사용자 - 마이페이지",
            description = "마이페이지 인증 번호 확인(회원 휴대폰 번호 업데이트, 인증 여부 업데이트)"
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
    @PostMapping("/api/join/number/check/info")
    @ResponseBody
    public JSONResponse<?> checkNumberInfo(@RequestBody Map<String, Object> map, Authentication authentication) {
        JSONResponse<?> response;
        try {
            response = memberService.checkNumberInfo(map, authentication);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }

}
