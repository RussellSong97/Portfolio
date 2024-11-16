package com.cuv.domain.linkhistory;

import com.cuv.common.JSONResponse;
import com.cuv.domain.linkhistory.entity.LinkHistory;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.product.dto.ProductCarHistoryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LinkHistoryService {
    private final LinkHistoryRepository linkHistoryRepository;
    private final MemberRepository memberRepository;
    @Value("${carhistory.url}")
    private String carHistoryUrl;

    public JSONResponse<?> searchApiCarHistory(ProductCarHistoryDto params, UserDetails userDetails) {

        if (userDetails == null) {
            return new JSONResponse<>(500, "로그인 후 사용 가능합니다.");
        }

        String joinCode = params.getJoinCode();
        String sType = params.getSType();
        String memberID = params.getMemberID();
        String carnum = params.getCarnum();
        String carNumType = params.getCarNumType();
        String stdDate = params.getStdDate();
        String rType = params.getRType();
        String malsoGb = params.getMalsoGb();
        Long productId = params.getProductId();

        Member member = memberRepository.findByEmail(userDetails.getUsername());
        Long dbMemberId = member.getId();
        LocalDateTime today = LocalDateTime.now();
        Long count = linkHistoryRepository.countByMemberIdAndProductIdAndCreatedAt(dbMemberId, productId, today);

        if (count >= 5) {
            return new JSONResponse<>(500, "하루에 5회까지 조회 가능합니다.");
        }

        LinkHistory linkHistory = linkHistoryRepository.findByMemberIdAndProductId(dbMemberId, productId);

        if (linkHistory == null) {
            // 외부 API 호출
            String url = carHistoryUrl;
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("joinCode", joinCode);
            requestBody.add("sType", sType);
            requestBody.add("memberID", memberID);
            requestBody.add("carnum", carnum);
            requestBody.add("carNumType", carNumType);
            requestBody.add("stdDate", stdDate);
            requestBody.add("rType", rType);
            requestBody.add("malsoGb", malsoGb);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("넘어온 값 확인 하기 : " + response.getBody());

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                linkHistory = saveLinkHistory(userDetails, productId, response.getBody());
                return processResponse(objectMapper, linkHistory, response.getBody());

            } catch (Exception e) {
                log.error("LinkHistory save error : " + e.getMessage());
                return new JSONResponse<>(500, "FALSE", e.getMessage());
            }
        } else {
            // 이미 조회한 데이터가 있을 경우
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return processResponse(objectMapper, linkHistory, linkHistory.getData());

            } catch (JsonProcessingException e) {
                log.error("JsonProcessingException : " + e.getMessage());
                return new JSONResponse<>(500, "FALSE", e.getMessage());
            }
        }
    }

    private JSONResponse<?> processResponse(ObjectMapper objectMapper, LinkHistory linkHistory, String responseBody) throws JsonProcessingException {
        Map<String, Object> dataMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});

        List<List<Map<String, String>>> r202List = extractData(dataMap, "r202");
        List<List<Map<String, String>>> r602List = extractData(dataMap, "r602");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("r202List", r202List);
        resultMap.put("r602List", r602List);

        return new JSONResponse<>(200, "SUCCESS", linkHistory, resultMap);
    }

    private List<List<Map<String, String>>> extractData(Map<String, Object> dataMap, String key) {
        List<List<Map<String, String>>> resultList = new ArrayList<>();
        String rawData = dataMap.get(key).toString();
        String cleanData = rawData.replaceAll("[\\[\\]{}]", "");
        String[] pairs = cleanData.split(", ");
        List<Map<String, String>> currentGroup = new ArrayList<>();

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                Map<String, String> map = new HashMap<>();
                map.put(keyValue[0].trim(), keyValue[1].trim());

                if (keyValue[0].startsWith("r602-01") && !currentGroup.isEmpty()) {
                    resultList.add(new ArrayList<>(currentGroup));
                    currentGroup.clear();
                }
                currentGroup.add(map);
            }
        }
        if (!currentGroup.isEmpty()) {
            resultList.add(currentGroup);
        }
        return resultList;
    }

    private LinkHistory saveLinkHistory(UserDetails userDetails, Long productId, String data) throws JsonProcessingException {
        Member member = memberRepository.findByEmail(userDetails.getUsername());
        ObjectMapper objectMapper = new ObjectMapper();

        Map dataMap = objectMapper.readValue(data, Map.class);
        LinkHistory linkHistory = LinkHistory.builder()
                .memberId(member.getId())
                .productId(productId)
                .data(data)
                // 일반전손사고일자
                .totalLossCount(dataMap.get("r405").toString())
                // 침수전/분손사고건수
                .floodLossCount(dataMap.get("r407").toString())
                // 도난전손사고건수
                .theftLossCount(dataMap.get("r409").toString())
                // 도난전손사고일자
                .theftLossDate(dataMap.get("r410-01").toString())
                // 용도 (1:관용, 2:자가용, 3:영업용, 4:개인택시)
                // 배열 안에 r202-05 사용
                .useType(dataMap.get("r202").toString())
                // 자차피해사고횟수
                .selfDamageAccidentCount(dataMap.get("r401").toString())
                // 자차피해보험금 합
                .selfDamageInsuranceSum(dataMap.get("r402").toString())
                // 상대방차피해사고 횟수
                .otherCarDamageAccidentCount(dataMap.get("r403").toString())
                // 상대방차피해보험금 합
                .otherCarDamageInsuranceSum(dataMap.get("r404").toString())
                // 소유자 변경 횟수
                .ownerChangeCount(dataMap.get("r204").toString())
                // 소유자 변경 타입 (01:최초등록, 02:차량번호변경, 04:소유자 변경)
                .ownerChangeType(dataMap.get("r202").toString())
                // 차량 이름
                .carName(dataMap.get("r111").toString())
                // 차량 상세 이름
                .carNameDetail(dataMap.get("r005").toString())
                // 차량 연식
                .carYear(dataMap.get("r004").toString())
                // 차량 배기량
                .carDisplacement(dataMap.get("r104").toString())
                // 조회 일자
                .standardDate(dataMap.get("r003").toString())
                // 차체형상
                .carBodyShape(dataMap.get("r107").toString())
                // 차량 옵션
                .carOption(dataMap.get("r108").toString())
                // 최초보험가입일
                .firstInsuranceDate(dataMap.get("r105").toString())
                // 사용 연료
                .fuelType(dataMap.get("r106").toString())
                // 관용 이력
                .personalUseHistory(dataMap.get("r301").toString())
                // 영업용 이력
                .businessUseHistory(dataMap.get("r302").toString())
                // 대여용 이력
                .rentalUseHistory(dataMap.get("r303").toString())
                // 차량 주행거리 배열
                .carDistanceDrive(dataMap.get("r602").toString())
                // 미가입 기간
                .noJoinDate(dataMap.get("r511-01").toString())
                .build();

        try {
            linkHistoryRepository.save(linkHistory);
        } catch (Exception e) {
            log.error("LinkHistory save error : " + e.getMessage());
        }

        return linkHistory;
    }

}
