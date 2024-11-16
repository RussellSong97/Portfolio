package com.cuv.domain.linkhistory;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LinkHistoryServiceTest {

    @Test
    void searchApiCarHistory() {
        String r202 = "[{r202-02=20180208, r202-01=02, r202-03=08오XXXX, r202-05=2, r202-04=1}, " +
                "{r202-02=20180208, r202-01=02, r202-03=08오XXXX, r202-05=2, r202-04=1}]";

        // 대괄호와 중괄호 제거
        r202 = r202.replaceAll("[\\[\\]{}]", "");

        // 쉼표로 분리
        String[] pairs = r202.split(", ");

        // 키-값 쌍을 저장할 리스트 생성
        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> map = new HashMap<>();

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                map.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        result.add(map);

        // 결과 출력
        System.out.println(result);
        for (Map<String, String> stringStringMap : result) {
            System.out.println("stringStringMap.get(\"r202-02\") = " + stringStringMap.get("r202-02"))
            ;
        }

    }
}