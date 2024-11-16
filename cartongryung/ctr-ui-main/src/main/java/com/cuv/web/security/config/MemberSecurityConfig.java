package com.cuv.web.security.config;

import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.RegType;
import com.cuv.web.security.auth.MemberAuthenticationProvider;
import com.cuv.web.security.auth.PrincipalOauth2UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class MemberSecurityConfig {

    private final MemberAuthenticationProvider memberAuthenticationProvider;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final MemberRepository memberRepository;

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
                        .requestMatchers("/", "/user/css/**", "/user/fonts/**", "/user/images/**", "/user/js/**", "/join/**", "search/**").permitAll()
                        .requestMatchers("/login/**", "/logout", "/api/**", "/purchase/**", "/find/**", "/uploads/**").permitAll()
                        .requestMatchers("/product/**", "/oauth2/**").permitAll()
                        .requestMatchers("/board/**", "/event/**", "/service**", "/map**", "/sell/intro", "/review/**").permitAll()
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
//                        .tokenEndpoint(token -> token
//                                .accessTokenResponseClient(accessTokenResponseClient()))
                        .userInfoEndpoint(userInfo -> userInfo
                        .userService(principalOauth2UserService))
                        .successHandler(memberAuthenticationSuccessHandler())
                        .failureHandler(memberAuthenticationFailureHandler()))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                );
        return http.build();
    }

//    @Bean
//    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
//        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
//        accessTokenResponseClient.setRequestEntityConverter(new CustomRequestEntityConverter());
//
//        return accessTokenResponseClient;
//    }

    /**
     * 회원 | 성공 핸들러
     *
     * @author 송다운
     */
   public AuthenticationSuccessHandler memberAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            System.out.println("authentication get name : " + authentication.getName());
            // 로그인 오류 메시지 제거
            HttpSession session = request.getSession();
            session.removeAttribute("errorMessage");

            // 로그인 날짜
            Member member = memberRepository.findByEmail(authentication.getName());
            member.setLastLoginAt(LocalDateTime.now());

            if ("N".equals(member.getAuthYn().getYn()) && member.getRegCode() != RegType.EMAIL) {
                response.sendRedirect( "/join/sns");
            } else {
                System.out.println("리다이렉트 조건 불만족: " + member.getRegCode());
                response.sendRedirect("/");
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

            session.setAttribute("errorMessage", exception.getMessage());
            response.sendRedirect("/login/email");
        };
    }
}
