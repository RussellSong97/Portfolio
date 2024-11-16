package com.cuv.web.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * 애플 로그인 시 secret key 생성
 */
@Slf4j
public class CustomRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

    private final OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

    public CustomRequestEntityConverter() {
        this.defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    }

    /**
     * 로그인 > Apple Secret Key 생성
     *
     * @param request OAuth2 요청
     * @author SungHa
     */
    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest request) {
        RequestEntity<?> convertedEntity = this.defaultConverter.convert(request);
        ClientRegistration clientRegistration = request.getClientRegistration();
        MultiValueMap<String, String> params = (MultiValueMap<String, String>) convertedEntity.getBody();

        // Apple일 경우 secret key 동적으로 세팅
        if ("apple".equals(clientRegistration.getRegistrationId())) {
            params.set(OAuth2ParameterNames.CLIENT_SECRET, this.createAppleClientSecret(
                    clientRegistration.getClientId(),
                    clientRegistration.getClientSecret()));

            return new RequestEntity<>(convertedEntity.getBody(), convertedEntity.getMethod(), convertedEntity.getUrl());
        }

        return convertedEntity;
    }

    /**
     * 로그인 > Apple Secret Key 생성
     *
     * @param clientId 클라이언트 ID
     * @param secretKeyResource 시크릿키
     * @author SungHa
     */
    private String createAppleClientSecret(String clientId, String secretKeyResource) {
        String clientSecret = "";

        // application-oauth.yml에 설정해놓은 apple secret Key를 /를 기준으로 split
        String[] secretKeyResourceArr = secretKeyResource.split("/");

        // JWT 토큰 생성
        SignedJWT jwt = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.ES256)
                        .keyID(secretKeyResourceArr[1])
                        .build(),
                new JWTClaimsSet.Builder()
                        .issuer(secretKeyResourceArr[2])
                        .audience("https://appleid.apple.com")
                        .subject(clientId)
                        .expirationTime(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))
                        .issueTime(new Date(System.currentTimeMillis()))
                        .build());

        try {
            ClassPathResource authKey = new ClassPathResource("key/" + secretKeyResourceArr[0]);
            String keyString = authKey.getContentAsString(StandardCharsets.UTF_8)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] encoded = Base64.getDecoder().decode(keyString);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            jwt.sign(new ECDSASigner(privateKey, Curve.P_256)); // 서명하기

            clientSecret = jwt.serialize();
        } catch (IOException | GeneralSecurityException | JOSEException e) {
            log.error("Error_createAppleClientSecret : {}-{}", e.getMessage(), e.getCause());
        }
        log.info("createAppleClientSecret : {}", clientSecret);
        return clientSecret;
    }

}
