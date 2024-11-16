package com.cuv.web.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.util.Date;

@Slf4j
public class CustomRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

    private final OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

    public CustomRequestEntityConverter() {
        defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    }

    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest request) {
        RequestEntity<?> convertedEntity = defaultConverter.convert(request);
        String registrationId = request.getClientRegistration().getRegistrationId();
        MultiValueMap<String, String> params = (MultiValueMap<String, String>) convertedEntity.getBody();

        // Apple일 경우 secret key 동적으로 세팅
        if (registrationId.contains("apple")) {
            params.set("client_secret", createAppleClientSecret(params.get("client_id").get(0), params.get("client_secret").get(0)));
        }

        return new RequestEntity<>(params, convertedEntity.getHeaders(), convertedEntity.getMethod(), convertedEntity.getUrl());

    }

    /**
     * 로그인 > Apple Secret Key 생성
     * @param clientId
     * @param secretKeyResource
     * @return
     */
    private String createAppleClientSecret(String clientId, String secretKeyResource) {
        String clientSecret = "";
        // application-oauth.yml에 설정해놓은 apple secret Key를 /를 기준으로 split
        String[] secretKeyResourceArr = secretKeyResource.split("/");
        try {
            InputStream inputStream = new ClassPathResource("{.p8 파일 경로}" + secretKeyResourceArr[0]).getInputStream();
            File file = File.createTempFile("appleKeyFile", ".p8");
            try {
                FileUtils.copyInputStreamToFile(inputStream, file);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }

            String appleKeyId = secretKeyResourceArr[1];
            String appleTeamId = secretKeyResourceArr[2];

            PEMParser pemParser = new PEMParser(new FileReader(file));
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();

            PrivateKey privateKey = converter.getPrivateKey(privateKeyInfo);

            // Create the JWT header
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                    .keyID(appleKeyId)
                    .type(JOSEObjectType.JWT)
                    .build();

            // Set the JWT Claims
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .issuer(appleTeamId)
                    .audience("https://appleid.apple.com")
                    .subject(clientId)
                    .expirationTime(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))
                    .issueTime(new Date(System.currentTimeMillis()))
                    .build();

            // Create the signed JWT
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);

            // Sign the JWT
            JWSSigner signer = new ECDSASigner((ECPrivateKey) privateKey);
            signedJWT.sign(signer);

            // Serialize the JWT to a compact form
            clientSecret = signedJWT.serialize();

        } catch (IOException | JOSEException e) {
            log.error("Error_createAppleClientSecret : {}-{}", e.getMessage(), e.getCause());
        }
        log.info("createAppleClientSecret : {}", clientSecret);
        return clientSecret;
    }

}
