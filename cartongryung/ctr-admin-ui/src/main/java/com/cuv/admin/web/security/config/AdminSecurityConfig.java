package com.cuv.admin.web.security.config;

import com.cuv.admin.domain.member.enumset.MemberRole;
import com.cuv.admin.web.security.auth.AdminAuthenticationProvider;
import com.cuv.admin.web.security.auth.AdminAuthorizationChecker;
import com.cuv.admin.web.security.auth.PrincipalDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.List;

/**
 * 시큐리티 로그인
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class AdminSecurityConfig {

    private final AdminAuthenticationProvider adminAuthenticationProvider;
    private final AdminAuthorizationChecker adminAuthorizationChecker;

    /**
     * 관리자 | 로그인 Filter Chain 설정
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception 예외가 발생한 경우
     * @author SungHa
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http
                .authenticationProvider(adminAuthenticationProvider)
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/css/**", "/admin/images/**", "/admin/js/**").permitAll()
                        .requestMatchers( "/", "/admin", "/admin/", "/admin/main", "/admin/login", "/admin/loginProc", "/admin/logout", "/admin/api/fcm/**").permitAll()
//                        .requestMatchers("/admin/uploads/**").permitAll()
                        .anyRequest().access(adminAuthorizationChecker)
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/loginProc")
                        .successHandler(this.adminAuthenticationSuccessHandler())
                        .failureHandler(this.adminAuthenticationFailureHandler())
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login")
                );

        return http.build();
    }

    /**
     * 관리자 | 성공 핸들러
     *
     * @author SungHa
     */
    public AuthenticationSuccessHandler adminAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // 로그인 일시 업데이트
            adminAuthenticationProvider.getAdminPrincipalDetailService().updateLastLoginAt();

            // 로그인 오류 메시지 제거
            HttpSession session = request.getSession();
            session.removeAttribute("errorMessage");

            // 권한별 리다이렉트
            for(GrantedAuthority authority : authentication.getAuthorities()) {
                if (MemberRole.ADMIN.getRole().equals(authority.getAuthority())) {
                    response.sendRedirect("/admin/main");
                } else {
                    PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
                    List<String> allowUrlPatterns = principalDetails.getAllowUrlPatterns();

                    switch (allowUrlPatterns.get(0)) {
                        case "/admin/main/**":
                            response.sendRedirect("/admin/main");
                            break;
                        case "/admin/member/**":
                            response.sendRedirect("/admin/member/member");
                            break;
                        case "/admin/purchase/**":
                            response.sendRedirect("/admin/purchase/inquiry");
                            break;
                        case "/admin/sale/**":
                            response.sendRedirect("/admin/sale/inquiry");
                            break;
                        case "/admin/management/**":
                            response.sendRedirect("/admin/management/product");
                            break;
                        case "/admin/board/**":
                            response.sendRedirect("/admin/board/notice");
                            break;
                        case "/admin/promotion/**":
                            response.sendRedirect("/admin/promotion/exhibition");
                            break;
                        case "/admin/stats/**":
                            response.sendRedirect("/admin/stats/stats");
                            break;
                        case "/admin/etc/**":
                            response.sendRedirect("/admin/etc/management");
                            break;
                    }
                }
            }
        };
    }

    /**
     * 관리자 | 실패 핸들러
     *
     * @author SungHa
     */
    public AuthenticationFailureHandler adminAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            // 로그인 오류 메시지 생성
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", exception.getMessage());

            // 로그인 페이지로 이동
            response.sendRedirect("/admin/login");
        };
    }
}
