package com.cuv.admin.domain.bizgo;

import com.cuv.admin.domain.bizgo.entity.Bizgo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 알림톡 서비스 (알림톡 전송)
 */
@Service
@Transactional
@Slf4j
public class AlimTalkService {

    @Autowired
    private BizgoRepository bizgoRepository;

    /**
     * bizgo 테이블 알림톡 템플릿 조회
     * @param templateCode 조회할 템플릿의 코드
     *
     * @author Sangbin
     */
    private String getTemplateContent(String templateCode) {
        Bizgo bizgo = bizgoRepository.findByCode(templateCode);
        if (bizgo != null) {
            return bizgo.getContent();
        } else {
            return "템플릿을 찾을 수 없습니다.";
        }
    }

    /**
     * bizgo 토큰 발급
     *
     * @author Sangbin
     */
    public String getToken() {
        HttpURLConnection conn = null;
        String token = null;

        try {
            URL url = new URL("https://omni.ibapi.kr/v1/auth/token");
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("X-IB-Client-Id", "ctrm_om_nl0us0j0"); // 발급받은 계정 아이디
            conn.setRequestProperty("X-IB-Client-Passwd", "EA102OE0752VRBL98510"); // 발급받은 계정 비밀번호
            conn.connect();

            InputStream is = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));

            StringBuilder buff = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                buff.append(line);
            }

            // 응답을 JSON으로 파싱
            JSONObject jsonResponse = new JSONObject(buff.toString());
            String code = jsonResponse.getString("code");
            String result = jsonResponse.getString("result");

            if ("A000".equals(code) && "Success".equals(result)) {
                // 'data' 필드 안의 'token' 필드에서 토큰 추출
                JSONObject data = jsonResponse.getJSONObject("data");
                token = data.getString("token");
            } else {
                log.error("토큰 발급 실패: " + result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return token;
    }

    /**
     * 지정된 템플릿과 데이터를 사용하여 비즈고 알림톡 메시지를 전송
     *
     * @param templateCode 템플릿코드
     * @param token 비즈고 토큰
     * @param placeholders 치환할 문자열
     * @param phoneNumber 전화번호
     *
     * @author Sangbin
     */
    public void sendMessage(String token, String templateCode, Map<String, String> placeholders, String phoneNumber) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        // 데이터베이스에서 템플릿 내용 조회
        String templateContent = getTemplateContent(templateCode);

        // Placeholder 치환
        String messageContent = substitutePlaceholders(templateContent, placeholders);

        // 버튼 정의
        String buttonsJson = "";
        switch(templateCode)

        {
            case "code15":
                buttonsJson = "["
                        + "{"
                        + "  \"type\": \"WL\","
                        + "  \"name\": \"내 차 목록 바로가기\","
                        + "  \"urlPc\": \"https://ctrm.co.kr/sale/vehicle\","
                        + "  \"urlMobile\": \"https://ctrm.co.kr/sale/vehicle\""
                        + "}]";
                break;
            case "code-01":
                buttonsJson = "["
                        + "{"
                        + "  \"type\": \"WL\","
                        + "  \"name\": \"메인바로가기\","
                        + "  \"urlPc\": \"https://ctrm.co.kr/\","
                        + "  \"urlMobile\": \"https://ctrm.co.kr/\""
                        + "}]";
                break;
            case "code-10":
                buttonsJson = "["
                        + "{"
                        + "  \"type\": \"WL\","
                        + "  \"name\": \"메인페이지 바로가기\","
                        + "  \"urlPc\": \"https://ctrm.co.kr/\","
                        + "  \"urlMobile\": \"https://ctrm.co.kr/\""
                        + "}]";
                break;
            case "code-04":
                buttonsJson = "["
                        + "{"
                        + "  \"type\": \"WL\","
                        + "  \"name\": \"마이페이지 바로가기\","
                        + "  \"urlPc\": \"https://ctrm.co.kr/mypage/purchase\","
                        + "  \"urlMobile\": \"https://ctrm.co.kr/mypage/purchase\""
                        + "}]";
                break;
            case "code-03":
            case "code-07":
            case "code-08":
                buttonsJson = "["
                        + "{"
                        + "  \"type\": \"WL\","
                        + "  \"name\": \"마이페이지 바로가기\","
                        + "  \"urlPc\": \"https://ctrm.co.kr/mypage/inquiry\","
                        + "  \"urlMobile\": \"https://ctrm.co.kr/mypage/inquiry\""
                        + "}]";
                break;
            case "code-09":
                buttonsJson = "["
                        + "{"
                        + "  \"type\": \"WL\","
                        + "  \"name\": \"마이페이지 바로가기\","
                        + "  \"urlPc\": \"https://ctrm.co.kr/mypage/sale\","
                        + "  \"urlMobile\": \"https://ctrm.co.kr/mypage/sale\""
                        + "}]";
                break;
            case "code-05":
                buttonsJson = "["
                        + "{"
                        + "  \"type\": \"WL\","
                        + "  \"name\": \"평생책임서비스 바로가기\","
                        + "  \"urlPc\": \"https://ctrm.co.kr/service01\","
                        + "  \"urlMobile\": \"https://ctrm.co.kr/service01\""
                        + "}]";
                break;
            // 버튼이 없는 경우는 빈 배열
            default:
                buttonsJson = "[]";
                break;
        }

        String jsonString = String.format(
                "{\"senderKey\": \"20bee4eab1462b8f4658b6719a5c9e93f2276f11\", " +
                        "\"msgType\": \"AT\", " +
                        "\"to\": \"%s\", " +
                        "\"templateCode\": \"%s\", " +
                        "\"text\": \"%s\", " +
                        "\"button\": %s}",
                phoneNumber, templateCode, messageContent, buttonsJson
        );

        log.info("Request JSON: " + jsonString); // 디버깅용 출력

        RequestBody body = RequestBody.create(mediaType, jsonString);

        Request request = new Request.Builder()
                .url("https://omni.ibapi.kr/v1/send/alimtalk")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try {
            Response response = client.newCall(request).execute();
            log.info(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * placeholders를 텍스트에 치환
     *
     * @param content
     * @param placeholders 치환할 문자열
     *
     * @author Sangbin
     */
    private String substitutePlaceholders(String content, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }
        return content;
    }

    /**
     * 토큰 발급 후 메시지 전송 메서드
     *
     * @param templateCode 템플릿 코드
     * @param placeholders 치환할 문자열
     * @param phoneNumber 전화번호
     *
     * @author Sangbin
     */
    public void sendAlimTalk(String templateCode, Map<String, String> placeholders, String phoneNumber) {
        String token = getToken();
        if (token != null) {
            sendMessage(token, templateCode, placeholders, phoneNumber);
        } else {
            log.error("토큰 발급 실패");
        }
    }
}
