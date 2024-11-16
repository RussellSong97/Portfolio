package com.cuv.web.security;

import com.cuv.common.YN;
import com.cuv.domain.member.entity.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 리다이렉트
 */
@RequiredArgsConstructor
public class CustomRedirectFilter extends OncePerRequestFilter {


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/user/css") ||
                requestURI.startsWith("/user/fonts") ||
                requestURI.startsWith("/user/images") ||
                requestURI.startsWith("/user/js") ||
                requestURI.startsWith("/api") ||
                requestURI.startsWith("/join") || requestURI.startsWith("/join/sns/save")) {  // 필요한 다른 경로 추가
            filterChain.doFilter(request, response);
            return;
        }

        // 본인 인증 필요한 경로 추가 할 것
        if (requestURI.startsWith("/sale/vehicle/")) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.getPrincipal() instanceof Member member && member.getAuthYn() == YN.N) {
                    response.sendRedirect("/join/sns?redirectUrl=" + requestURI);
                    return;
                }

        }
        filterChain.doFilter(request, response);
    }
}
