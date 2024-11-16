package com.cuv.web.security.config;

import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.web.security.CustomRequestEntityConverter;
import com.cuv.web.security.auth.MemberAuthenticationProvider;
import com.cuv.web.security.auth.MemberAuthorizationChecker;
import com.cuv.web.security.auth.PrincipalOauth2UserService;
import com.cuv.web.security.auth.PrincipalOidcUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.time.LocalDateTime;

/**
 * 시큐리티 로그인
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class MemberSecurityConfig {

    private final MemberAuthenticationProvider memberAuthenticationProvider;
    private final MemberAuthorizationChecker memberAuthorizationChecker;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final PrincipalOidcUserService principalOidcUserService;
    private final MemberRepository memberRepository;
    private final RequestCache requestCache = new HttpSessionRequestCache();


    /**
     * 회원 | 로그인 Filter Chain 설정
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception 예외가 발생한 경우
     * @author 송다운
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(memberAuthenticationProvider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/user/css/**", "/user/fonts/**", "/user/images/**", "/user/js/**", "/join/**", "/search/**").permitAll()
                        .requestMatchers("/login/**", "/logout", "/api/**", "/purchase/**", "/find/**", "/uploads/**", "/token/save").permitAll()
                        .requestMatchers("/product/**", "/oauth2/**", "/test/**").permitAll()
                        .requestMatchers("/board/**", "/event/**", "/service**", "/map**", "/sell/intro", "/review/**", "/rent/**").permitAll()
                        .requestMatchers("/warranty**", "/lease**", "/installments**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/v3/**").access(new MemberAuthorizationChecker())
                        .anyRequest().hasRole("USER")
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureHandler(memberAuthenticationFailureHandler())
                        .successHandler(memberAuthenticationSuccessHandler())
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .tokenEndpoint(token -> token
                                .accessTokenResponseClient(accessTokenResponseClient()))
                        .redirectionEndpoint(redirect -> redirect
                                .baseUri("/login/oauth2/code/*")
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(principalOauth2UserService)
                                .oidcUserService(principalOidcUserService))
                        .successHandler(memberAuthenticationSuccessHandler())
                        .failureHandler(memberAuthenticationFailureHandler()))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                );
//                .addFilterBefore(new CustomRedirectFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    /**
     * 애플 로그인 > 토큰 검증
     *
     * @author SungHa
     */
    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        accessTokenResponseClient.setRequestEntityConverter(new CustomRequestEntityConverter());

        return accessTokenResponseClient;
    }

    /**
     * 회원 | 성공 핸들러
     *
     * @author 송다운
     */
    public AuthenticationSuccessHandler memberAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // 로그인 오류 메시지 제거
            HttpSession session = request.getSession();
            session.removeAttribute("errorMessage");

            // 로그인 날짜
            Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(), MemberStatus.NORMAL);
            member.setLastLoginAt(LocalDateTime.now());
            memberRepository.save(member);

            // 보험 이력 조회 로그인 여부
            String redirectUrl = (String) session.getAttribute("redirectUrl");
            if(redirectUrl != null && !redirectUrl.isEmpty()) {
                response.sendRedirect(redirectUrl);
            } else {
                // 로그인 전 페이지로 이동
                SavedRequest savedRequest = requestCache.getRequest(request, response);
                String targetUrl = (savedRequest != null) ? savedRequest.getRedirectUrl() : "/";
                response.sendRedirect(targetUrl);
            }

        };
    }

    /**
     * 회원 | 실패 핸들러
     *
     * @author 송다운
     */
    public AuthenticationFailureHandler memberAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            HttpSession session = request.getSession();
            String requestURI = request.getRequestURI();
            String[] splitRequestURI = requestURI.split("/");

            // sns 로그인 중복 체크
            if (splitRequestURI.length == 5) {
                if(exception.getMessage().contains(MemberStatus.PAUSE.getDetail()) || exception.getMessage().contains(MemberStatus.SECESSION.getDetail())) {
                    session.setAttribute("errorType", "notNormal");
                }

                session.setAttribute("errorMessage", exception.getMessage());

                if (exception.getMessage().contains("denied")) {
                    session.removeAttribute("errorMessage");
                }

                response.sendRedirect("/login");
            } else {
                session.setAttribute("errorMessage", exception.getMessage());
                response.sendRedirect("/login/email");
            }
        };
    }
}
