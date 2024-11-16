package com.cuv.web.security.auth;

import com.cuv.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * 스웨거 권한 체크
 */
@Component
@RequiredArgsConstructor
public class MemberAuthorizationChecker implements AuthorizationManager<RequestAuthorizationContext> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Object principal = authentication.get().getPrincipal();
        if (!(principal instanceof Member principalDetails))
            return new AuthorizationDecision(false);

        boolean isGranted = "swaggerCheck".equals(principalDetails.getLoginId());

        return new AuthorizationDecision(isGranted);
    }

}
