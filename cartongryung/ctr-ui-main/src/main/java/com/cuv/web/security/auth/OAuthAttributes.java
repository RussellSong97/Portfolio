//package com.cuv.web.security.auth;
//
//import lombok.Builder;
//import lombok.Getter;
//
//import java.util.Map;
//
//@Getter
//public class OAuthAttributes {
//
//    private final Map<String, Object> attributes;
//    private final String nameAttributeKey;
//
//    @Builder
//    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey) {
//        this.attributes = attributes;
//        this.nameAttributeKey = nameAttributeKey;
//    }
//
//    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
//        // registrationId에 따라 적절한 nameAttributeKey를 설정합니다.
//        if ("naver".equals(registrationId)) {
//            return new OAuthAttributes(attributes, "id");
//        } else if ("kakao".equals(registrationId)) {
//            return new OAuthAttributes(attributes, "id");
//        } else if ("google".equals(registrationId)) {
//            return new OAuthAttributes(attributes, "sub");
//        } else if (registrationId.contains("apple")) {
//            return new OAuthAttributes(attributes, "sub");
//        } else {
//            throw new IllegalArgumentException("Unsupported registrationId: " + registrationId);
//        }
//    }
//}
