package com.cuv.admin.web.security.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * 딜러 및 상담사 회원 접근 권한 체크
 */
@Component
@RequiredArgsConstructor
public class AdminAuthorizationChecker implements AuthorizationManager<RequestAuthorizationContext> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        HttpServletRequest request = object.getRequest();
        String requestURI = request.getRequestURI();

        Object principal = authentication.get().getPrincipal();
        if (!(principal instanceof PrincipalDetails principalDetails))
            return new AuthorizationDecision(false);

        List<String> allowUrlPatterns = principalDetails.getAllowUrlPatterns();
        boolean isGranted = false;

        // 메뉴별 권한 체크
        for (String pattern : allowUrlPatterns) {
            String regex = pattern.replace("/**", ".*");

            if (Pattern.matches(regex, requestURI)) {
                isGranted = true;
                break;
            }
        }

        return new AuthorizationDecision(isGranted);
    }

    /**
     * 관리자 | 헤더 > 접근 URL 판별
     *
     * @param url 주소
     * @author SungHa
     */
    public static Boolean hasUrlPermission(String url) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof PrincipalDetails principal))
            return false;

        for (String pattern : principal.getAllowUrlPatterns()) {
            String regex = pattern.replace("/**", ".*");

            if (Pattern.matches(regex, url)) {
                return true;
            }
        }

        return false;
    }

}
