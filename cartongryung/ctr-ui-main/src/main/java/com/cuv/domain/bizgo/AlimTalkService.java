package com.cuv.domain.bizgo;

import com.cuv.domain.bizgo.entity.Bizgo;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
public class AlimTalkService {

    @Autowired
    private BizgoRepository bizgoRepository;

    // 데이터베이스에서 템플릿을 조회하는 메서드
    private String getTemplateContent(String templateCode) {
        System.out.println(11111);
        Bizgo bizgo = bizgoRepository.findByCode(templateCode);
        if (bizgo != null) {
            return bizgo.getContent();
        } else {
            return "템플릿을 찾을 수 없습니다.";
        }
    }

    // 토큰을 발급받는 메서드
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
                System.out.println("토큰 발급 실패: " + result);
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

    // 메시지를 전송하는 메서드
    public void sendMessage(String token, String templateCode, Map<String, String> placeholders, String phoneNumber) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");

        // 데이터베이스에서 템플릿 내용 조회
        String templateContent = getTemplateContent(templateCode);

        // Placeholder 치환
        String messageContent = substitutePlaceholders(templateContent, placeholders);

        RequestBody body = RequestBody.create(mediaType, String.format(
                "{\"senderKey\": \"20bee4eab1462b8f4658b6719a5c9e93f2276f11\", " +
                        "\"msgType\": \"AT\", " +
                        "\"to\": \"%s\", " +
                        "\"templateCode\": \"%s\", " +
                        "\"text\": \"%s\"}",
                phoneNumber, templateCode, messageContent
        ));

        Request request = new Request.Builder()
                .url("https://omni.ibapi.kr/v1/send/alimtalk")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Placeholder를 텍스트에 치환하는 메서드
    private String substitutePlaceholders(String content, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }
        return content;
    }

    // 토큰 발급 후 메시지 전송 메서드
    public void sendAlimTalk(String templateCode, Map<String, String> placeholders, String phoneNumber) {
        String token = getToken();
        if (token != null) {
            sendMessage(token, templateCode, placeholders, phoneNumber);
        } else {
            System.out.println("토큰 발급 실패");
        }
    }
}
