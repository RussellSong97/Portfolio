package com.cuv.web.controller.member;

import com.cuv.common.JSONResponse;
import com.cuv.domain.member.MemberService;
import com.cuv.domain.member.dto.MemberSaveDto;
import com.cuv.domain.terms.TermsService;
import com.cuv.domain.terms.enumset.TermsType;
import com.cuv.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final TermsService termsService;

    private final JwtUtils jwtUtils;
    /**
     * 회원가입 | Step1
     * @return
     * @author 송다운
     */
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
     * @param dto
     * @return
     * @author 송다운
     */
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


    @PostMapping("/api/join/check/phone")
    @ResponseBody
    public JSONResponse<?> checkPhone(@RequestBody Map<String, Object> map, @AuthenticationPrincipal UserDetails userDetails) {
        JSONResponse<?> response;
        try {
            response = memberService.checkPhone(map, userDetails);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }

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
    @GetMapping("/join/complete")
    public String joinComplete() {
        return "member/join_completed";
    }


    /**
     * 비밀번호 찾기 페이지
     * @return
     */
    @GetMapping("/find/password")
    public String findPassword() {
        return "member/find_password";
    }

    /**
     * 비밀번호 찾기 메일 쏘기
     * @param map
     * @return
     */
    @PostMapping("/find/password")
    @ResponseBody
    public JSONResponse<?> searchFindPassword(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = memberService.searchFindPassword(map);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }

    /**
     * 비밀번호 찾기 > 이메일 전송 완료
     *
     * @author SungHa
     */
    @GetMapping("/find/password/complete")
    public String findPasswordComplete() {
        return "member/find_completed";
    }

    /**
     * 비밀번호 찾기 | 비밀번호 재설정 페이지
     * @param jwtToken
     * @param model
     * @return
     */
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
     * @param map
     * @return
     */
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
    @GetMapping("/join/sns")
    public String joinSns() {
        return "member/join_sns";
    }

    /**
     * 본인 인증 저장 (mobileNumber 업데이트)
     * @param map
     * @param userDetails
     * @return
     */
    @PostMapping("/join/sns/save")
    @ResponseBody
    public  JSONResponse<?> joinSnsSave(@RequestBody Map<String, Object> map, @AuthenticationPrincipal UserDetails userDetails) {
        JSONResponse<?> response;
        try {
            response = memberService.saveJoinSns(map,userDetails);
        } catch (Exception e) {
            return new JSONResponse<>(500, "회원가입에 실패하였습니다.");
        }
        return response;
    }

    @PostMapping("/api/join/number/check/info")
    @ResponseBody
    public JSONResponse<?> checkNumberInfo(@RequestBody Map<String, Object> map, @AuthenticationPrincipal UserDetails userDetails) {
        JSONResponse<?> response;
        try {
            response = memberService.checkNumberInfo(map,userDetails);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }
}
