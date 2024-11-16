package com.cuv.web.security.auth;

import java.util.Map;

/**
 * 이메일 계정 정보
 */
public interface OAuth2UserInfo {
    Map<String, Object> getAttributes();

    String getProviderId();

    String getProvider();

    String getEmail();

    String getName();
}
