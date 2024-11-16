package com.cuv.domain.product_copy;

import com.cuv.common.JSONResponse;
import com.cuv.domain.product_copy.dto.ApiProductDto;
import com.cuv.util.SeedCBC;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductCopyService {


    public JSONResponse<?> testInsurance(ApiProductDto dto) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();

        System.out.println("dto.getStype : " + dto.getSType());
        System.out.println("dto. get RType : " + dto.getRType());
        System.out.println("dto.getJoinCode : " + dto.getJoinCode());
        System.out.println("dto.getMemberID : " + dto.getMemberID());
        System.out.println("dto.getCarnum : " + dto.getCarnum());
        System.out.println("dto.getCarNumType : " + dto.getCarNumType());
        System.out.println("dto.getStdDate : " + dto.getStdDate());

        // 암호화할 데이터
        String joinCode = dto.getJoinCode();
        String sType = SeedCBC.encrypt("1");
        String memberID = SeedCBC.encrypt(dto.getMemberID());
        String carnum = SeedCBC.encrypt(dto.getCarnum());
        String carNumType = dto.getCarNumType();
        String stdDate = dto.getStdDate();
        String rType = "J";

        map.put("joinCode", joinCode);
        map.put("sType", sType);
        map.put("memberID", memberID);
        map.put("carnum", carnum);
        map.put("carNumType", carNumType);
        map.put("stdDate", stdDate);
        map.put("rType", rType);

        // JSON 형식의 요청 데이터 작성
        String mapStr = objectMapper.writeValueAsString(map);
        String testKey = "96,ca,a1,f1,8a,9b,bd,47,4e,54,27,5b,0f,25,ce,d0";

        System.out.println("넘어온 map STr : " + mapStr);
        String encodedKey = Base64.getEncoder().encodeToString(testKey.getBytes(StandardCharsets.UTF_8));

        // HTTP 요청 작성
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://www.carhistory.or.kr:8811/dataTrans/carhistoryAPI3Test.car"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + encodedKey)
                .POST(HttpRequest.BodyPublishers.ofString(mapStr))
                .build();


        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 응답 결과 출력
        System.out.println("응답 데이터 확인 :" + response.body());
        System.out.println("응답 데이터 확인222 :" + response);
        return new JSONResponse<>(200, "SUCCESS");
    }

    public void testInsuranceV2() {
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("http://www.carhistory.or.kr:8811/dataTrans/carhistoryAPI3Test.car"))
//                .header("Content-Type", "application/json")
//                .header("Authorization", "Basic " + encodedKey)
//                .POST(HttpRequest.BodyPublishers.ofString(mapStr))
//                .build();
    }
}
