package com.cuv.web.controller.login;

import com.cuv.domain.exhibition.ExhibitionService;
import com.cuv.domain.exhibition.dto.ExhibitionListDto;
import com.cuv.domain.exhibition.enumset.ExhibitionType;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final ExhibitionService exhibitionService;

//    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
//    private String APPLE_CLIENT_ID;
//
//    @Value("${spring.security.oauth2.client.registration.apple.redirect-uri}")
//    private String APPLE_REDIRECT_URL;

    private final static String APPLE_AUTH_URL = "https://appleid.apple.com";

    /**
     * 로그인 메인
     *
     * @author 송다운
     */
    @GetMapping("/login")
    public String login(Model model) {
        // 로그인 배너
        List<ExhibitionListDto> loginBanner = exhibitionService.searchAllExhibition(ExhibitionType.LOGIN.getCode());
//        String appleUrl =  APPLE_AUTH_URL + "/auth/authorize"
//                + "?client_id=" + APPLE_CLIENT_ID
//                + "&redirect_uri=" + APPLE_REDIRECT_URL
//                + "&response_type=code%20id_token&scope=name%20email&response_mode=form_post";
        model.addAttribute("loginBanner", loginBanner);
//        model.addAttribute("appleUrl", appleUrl);

        return "member/login";
    }

    /**
     * 이메일 로그인
     *
     * @author 송다운
     */
    @GetMapping("/login/email")
    public String loginEmail(HttpSession session, Model model) {
        String errorMessage = (String) session.getAttribute("errorMessage");
        if(errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }
        return "member/login_email";
    }

}
