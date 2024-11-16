package com.cuv.admin.domain.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

/**
 * fcm 관련 서비스(메시지 생성, 전송)
 */
/*
    @author : 전남혁 ( all_step@naver.com )
    @description : FCM 푸시 알림
    @created : 2023.05.24
    @version : 1.0.0
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    /* ========================================= GOOGLE FCM =================================================
        FCM Token: 사용자의 기기를 구분하기 위한 토큰
        Access Token: FCM 서버에 보내기 위해서 필요한 값으로 온전한 서버인지 확인하는 토큰

        FCM 작동 순서: 파이어베이스 시작(연결) 및 연결되었다는 증표인 토큰(Access Token) 받기 - 파이어베이스에 보낼 메시지 생성 및 전송 - 파이어베이스에서 유저에게 전송

        사용 라이브러리 : implementation 'com.google.firebase:firebase-admin:9.2.0' // Firebase Admin SDK
        * 1. firebase console 접속
        * 2. 프로젝트 생성
        * 3. 프로젝트 설정 -> 서비스 계정 -> 새 비공개 키 생성 -> json 파일 다운로드 ( fcm.key.location 에 경로 저장 )
        * 4. 프로젝트 설정 -> 클라우드 메시징 -> 서버 키 복사 ( 현재 미사용 )


        * 참고 : MulticastMessage 는 한번에 최대 500명에게 전송 가능


        * 참고 : 다중 전송의 BatchResponse 는 아래와 같이 사용
        int getFailureCount()               : 실패한 메세지 수
        List<SendResponse> getResponses()   : 전송 결과
        int getSuccessCount()               : 성공한 메세지 수


        * 참고 : 링크 관련
        앱은 링크를 별첨 할수 없음 (https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages?hl=ko#Message.FIELDS.data)
        네이티브 앱에선 deep link 관련 컨트롤이 가능.. 하이브리드 앱에선 불가
        ( 네이티브 앱 추가 참고 : https://firebase.google.com/docs/dynamic-links?hl=ko )
        Chrome 에서 발송되는 web push 알림은 링크를 별첨 할수 있음
        Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .setImage(imageUrl)
                            .build())
                    .setToken(targetToken)
                    .setWebpushConfig(WebpushConfig.builder().putData("link","https://www.naver.com").build())
            .build()


        * 참고 : 이미지 관련
        장치에 다운로드되어 알림에 표시될 이미지의 URL을 포함합니다.
        JPEG, PNG, BMP는 모든 플랫폼에서 완벽하게 지원됩니다.
        애니메이션 GIF 및 비디오는 iOS에서만 작동합니다.
        WebP 및 HEIF는 플랫폼 및 플랫폼 버전에 따라 다양한 수준의 지원을 제공합니다.
        Android는 1MB 이미지 크기 제한이 있습니다.
        Firebase 저장소에서 이미지 호스팅에 대한 할당량 사용 및 영향/비용: https://firebase.google.com/pricing
     */


    /**
     * 필요한 객체 생성
     */

//    private final String API_URL = "https://fcm.googleapis.com/v1/projects/" +
//            "cartongtest-c2e80/messages:send";

    @Value("${fcm.key.location}")
    private String fcmKeyLocation;


    private final String fireBaseScope = "https://www.googleapis.com/auth/cloud-platform";





    // ============================================ FIREBASE INIT =============================================
    /**
     *  FCM 초기화 ( 필수 )
     *  application.properties 에서 fcm.key.location 의 경로에 있는 json 파일을 읽어서 초기화
     *  json 파일이 없으면 초기화 하지 않음
     *  이미 초기화 되었을 경우 하지 않음
    */
    @PostConstruct
    public void init() {
        try {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials
                            .fromStream(new ClassPathResource(fcmKeyLocation).getInputStream())
                            .createScoped(List.of(fireBaseScope))
                    )
                    .build();
            log.info("Firebase initializeApp start : " + FirebaseApp.getApps().isEmpty());
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        } catch (Exception e) {
            throw new RuntimeException("Firebase initializeApp error", e);
        }
    }
    // ================================================================================================

    // ================================ 구글로부터 내 서버가 접근권한이 있다는 Access Token 받기(유저 토큰 X) ========================================

    public String getAccessToken() throws IOException {

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(fcmKeyLocation).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        log.info(("fcm 엑세스 토큰" + googleCredentials.getAccessToken().getTokenValue()));
        return googleCredentials.getAccessToken().getTokenValue();
    }

    // ================================================================================================


    // =================================== FCM 단일건 메세지 객체 생성 ========================================
    /** 단일건 메세지 객체 생성
     * @param targetToken : FCM 토큰
     * @param title : 제목
     * @param body : 내용
     * @param imageUrl : 이미지 URL
     * @return : Firebase Cloud Messaging 메세지 객체
    */
    public Message setSingleMessage(String title, String body, String imageUrl, String targetToken, String linkUrl) {
        Message message;

        if (hasText(imageUrl) && hasText(linkUrl)) {

            message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .setImage(imageUrl)
                            .build())
                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder()
                                    .setAlert(ApsAlert.builder()
                                            .setTitle(title)
                                            .setBody(body)
                                            .build())
                                    .setMutableContent(true)
                                    .build())
                            .setFcmOptions(ApnsFcmOptions.builder()
                                    .setImage(imageUrl)
                                    .build())
                            .build())
                    .setAndroidConfig(AndroidConfig.builder()
                            .setNotification(AndroidNotification.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .setImage(imageUrl)
                                    .setPriority(AndroidNotification.Priority.HIGH)
                                    .build())
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build()
                    )
                    .putData("link", linkUrl)
                    .putData("image", imageUrl)
                    .setToken(targetToken)
                    .build();

        } else if (hasText(imageUrl)){

            message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder()
                                    .setAlert(ApsAlert.builder()
                                            .setTitle(title)
                                            .setBody(body)
                                            .build())
                                    .setMutableContent(true)
                                    .build())
                            .setFcmOptions(ApnsFcmOptions.builder()
                                    .setImage(imageUrl)
                                    .build())
                            .build())

                    .setAndroidConfig(AndroidConfig.builder()
                            .setNotification(AndroidNotification.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .setImage(imageUrl)
                                    .setPriority(AndroidNotification.Priority.HIGH)
                                    .build())
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .putData("image",imageUrl)
                    .setToken(targetToken)
                    .build();

        } else if (hasText(linkUrl)){
            message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setAndroidConfig(AndroidConfig.builder()
                            .setNotification(AndroidNotification.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .setImage(imageUrl)
                                    .setPriority(AndroidNotification.Priority.HIGH)
                                    .build())
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .putData("link",linkUrl)
                    .setToken(targetToken)
                    .build();

        } else {
            message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setAndroidConfig(AndroidConfig.builder()
                            .setNotification(AndroidNotification.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .setImage(imageUrl)
                                    .setPriority(AndroidNotification.Priority.HIGH)
                                    .build())
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .setToken(targetToken)
                    .build();
        }

        return message;
    }

    // ===================================================================================================

    // ================================= FCM 단일건 다중 전송 메세지 객체 생성 =====================================
    /** 한번에 여러명에게 FCM 메세지 전송을 위한 객체 생성
     *  @param title : 제목
     *  @param body : 내용
     *  @param imageUrl : 이미지 URL
     *  @param registrationTokens : 등록된 토큰
     *  @param linkUrl : URL
     *  @return : 다중 FCM 메세지 객체
    */
    public MulticastMessage setMulticastMessage(String title, String body, String imageUrl, List<String> registrationTokens, String linkUrl) {
        MulticastMessage multicastMessage;

        if (hasText(imageUrl) && hasText(linkUrl)) {
            multicastMessage =  MulticastMessage.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .setImage(imageUrl)
                            .build())

                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder()
                                    .setAlert(ApsAlert.builder()
                                            .setTitle(title)
                                            .setBody(body)
                                            .build())
                                    .setMutableContent(true)
                                    .build())
                            .setFcmOptions(ApnsFcmOptions.builder()
                                    .setImage(imageUrl)
                                    .build())
                            .build())

                    .setAndroidConfig(AndroidConfig.builder()
                            .setNotification(AndroidNotification.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .setImage(imageUrl)
                                    .setPriority(AndroidNotification.Priority.HIGH)
                                    .build())
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .putData("link", linkUrl)
                    .putData("image",imageUrl)

                    .addAllTokens(registrationTokens)
                    .build();
        } else if (hasText(imageUrl)){

            multicastMessage =  MulticastMessage.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .setImage(imageUrl)
                            .build())

                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder()
                                    .setAlert(ApsAlert.builder()
                                            .setTitle(title)
                                            .setBody(body)
                                            .build())
                                    .setMutableContent(true)
                                    .build())
                            .setFcmOptions(ApnsFcmOptions.builder()
                                    .setImage(imageUrl)
                                    .build())
                            .build())

                    .setAndroidConfig(AndroidConfig.builder()
                            .setNotification(AndroidNotification.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .setImage(imageUrl)
                                    .setPriority(AndroidNotification.Priority.HIGH)
                                    .build())
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .putData("image",imageUrl)

                    .addAllTokens(registrationTokens)
                    .build();
        } else if (hasText(linkUrl)){

            multicastMessage =  MulticastMessage.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setAndroidConfig(AndroidConfig.builder()
                            .setNotification(AndroidNotification.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .setImage(imageUrl)
                                    .setPriority(AndroidNotification.Priority.HIGH)
                                    .build())
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .putData("link",linkUrl)
                    .addAllTokens(registrationTokens)
                    .build();
        } else {
            multicastMessage =  MulticastMessage.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setAndroidConfig(AndroidConfig.builder()
                            .setNotification(AndroidNotification.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .setImage(imageUrl)
                                    .setPriority(AndroidNotification.Priority.HIGH)
                                    .build())
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .addAllTokens(registrationTokens)
                    .build();
        }

        return multicastMessage;
    }
    // ===================================================================================================


    // ====================================== FCM 단일건 메세지 전송 ==========================================
    /** FCM 토큰으로 메시지 보내기
        @param message : FCM 메세지 객체
        @return : FCM 응답
    */

    public String sendSingleMessage(Message message) {
        String response;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Firebase sendSingleMessage error", e);
        }
        return response;
    }
    // ===================================================================================================


    // ====================================== FCM 여러종류의 메세지 전송 ==========================================
    /** 메세지 여러번 보내기
        @param messageList : FCM 메세지 객체
        @return : FCM 응답 ( 객체 타입 )
    */
    public BatchResponse sendListMessage(List<Message> messageList) {
        BatchResponse response;
        try {
            response = FirebaseMessaging.getInstance().sendAll(messageList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Firebase sendListMessage error", e);
        }
        return response;
    }
    // ===================================================================================================


    // ====================================== FCM 단일 메세지 다중 전송 ==========================================
    /** 여러명에게 한번에 메세지 보내기
        @param multicastMessage : FCM 다중 메세지 객체
        @return : FCM 응답 ( 객체 타입 )
    */
    public BatchResponse sendMultiMessage(MulticastMessage multicastMessage) {
        BatchResponse response;
        try {
            response = FirebaseMessaging.getInstance().sendEachForMulticast(multicastMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Firebase sendMultiMessage error", e);
        }
        return response;
    }
    // ===================================================================================================

}
