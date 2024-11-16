package com.cuv.domain.linkhistory;

import com.cuv.common.JSONResponse;
import com.cuv.common.YN;
import com.cuv.domain.linkhistory.entity.LinkHistory;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.product.dto.ProductCarHistoryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 보험 이력조회 API 조회 및 정보 저장
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LinkHistoryService {
    private final LinkHistoryRepository linkHistoryRepository;
    private final MemberRepository memberRepository;
    @Value("${carhistory.url}")
    private String carHistoryUrl;

    /**
     * 보험 이력조회
     * @param params 보험 이력 정보
     * @param authentication 로그인 정보
     * @throws JsonProcessingException 예외 처리
     */
    public JSONResponse<?> searchApiCarHistory(ProductCarHistoryDto params, Authentication authentication) throws JsonProcessingException {

        if (authentication == null) {

            String url;

            if (params.getProductId() != null)
                url = "/product/" + params.getProductId();
             else
                 url = "/product";

            return new JSONResponse<>(401, "로그인 후 사용 가능합니다." , "/login?redirectUrl=" + url);
        }

        Member member = (Member) authentication.getPrincipal();


        String redirectUrl = params.getRedirectUrl();

        if (member.getAuthYn() == YN.N) return new JSONResponse<>(401 , "본인인증 후 사용 가능합니다." , "/join/sns?redirectUrl=" + redirectUrl);

        String joinCode = params.getJoinCode();
        String sType = params.getSType();
        String memberID = params.getMemberID();
        String carnum = params.getCarnum();
        String carNumType = params.getCarNumType();
        String stdDate = params.getStdDate();
        String rType = params.getRType();
        String malsoGb = params.getMalsoGb();
        Long productId = params.getProductId();

        member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(), MemberStatus.NORMAL);
        Long dbMemberId = member.getId();
        Long count = linkHistoryRepository.searchLinkHistoryCountAtToday(dbMemberId);

        if (count >= 5) {
            return new JSONResponse<>(100, "로그인 후에 사용하실 수 있으며, 하루에 5번까지 조회 하실 수 있습니다.");
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

            ResponseEntity<String> response;

            try {
                // 외부 API 호출
                response = restTemplate.postForEntity(url, request, String.class);
                log.info("response: " + response);
                log.info("response getBody: " + response.getBody());

                // JSON 문자열을 JsonNode로 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                // r000 키의 값을 가져옴
                String resultCode = rootNode.path("r000").asText();

                if (!"000".equals(resultCode)) { // resultCode 000 = 성공 나머지 예외 처리
                    throw new IllegalStateException(resultCode);
                }

                // LinkHistory 저장 시도
                try {
                    linkHistory = saveLinkHistory(authentication, productId, response.getBody());
                } catch (Exception e) {
                    log.error("Link history 저장 중 오류 발생: " + e.getMessage());
                    return new JSONResponse<>(500, "보험 이력 조회 저장 중 오류가 발생했습니다.");
                }

            } catch (IllegalStateException e) {
                // 외부 API의 예외 처리
                log.error("API response 오류 코드 : " + e.getMessage());
                return getError(e.getMessage());

            } catch (JsonProcessingException e) {
                // JSON 처리 중 발생한 예외 처리
                log.error("JSON 처리 중 에러 발생: " + e.getMessage());
                return new JSONResponse<>(500, "보험 이력 조회 저장 중 오류가 발생했습니다.");

            } catch (Exception e) {
                // 기타 예외 처리
                log.error("외부 API 호출 또는 데이터 저장 중 에러 발생: " + e.getMessage());
                return new JSONResponse<>(500, "처리 중 오류가 발생했습니다. 다시 시도해주세요.");
            }

            // 성공적으로 처리되었을 때의 응답
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return processResponse(objectMapper, linkHistory, response.getBody());
            } catch (JsonProcessingException e) {
                log.error("응답 처리 중 JSON 처리 실패: " + e.getMessage());
                return new JSONResponse<>(500, "응답 처리 중 오류가 발생했습니다.");
            }

        } else {
            // 이미 조회한 데이터가 있을 경우
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return processResponse(objectMapper, linkHistory, linkHistory.getData());
            } catch (JsonProcessingException e) {
//                log.error("이미 조회한 데이터 처리 중 JSON 처리 실패: " + e.getMessage());
                return new JSONResponse<>(404, "이미 조회한 데이터 처리 실패: " + e.getMessage());
            }
        }
    }

    /**
     * 보험 이력 조회 에러 코드
     * @param resultCode 오류 코드
     * @return
     */
    private JSONResponse<String> getError(String resultCode) {
        switch (resultCode) {
            case "100":
                return new JSONResponse<>(100, "필수항목 입력 누락");
            case "101":
                return new JSONResponse<>(100, "제휴사코드 없음");
            case "102":
                return new JSONResponse<>(100, "차량번호 없음");
            case "103":
                return new JSONResponse<>(100, "차량번호조회 요청시 말소차량 조회불가");
            case "104":
                return new JSONResponse<>(100, "차대번호조회 요청시 유효차량 조회불가");
            case "200":
                return new JSONResponse<>(100, "차량번호 형식오류");
            case "201":
                return new JSONResponse<>(100, "요청자료 복호화 불가");
            case "400":
                return new JSONResponse<>(100, "접근 IP 미등록");
            case "401":
                return new JSONResponse<>(100, "제휴사 이용 미허용");
            case "402":
                return new JSONResponse<>(100, "서비스 이용기간 아님");
            case "403":
                return new JSONResponse<>(100, "말소차량조회요청시 권한 없음");
            case "404":
                return new JSONResponse<>(100, "기준일자조회요청시 권한 없음");
            case "406":
                return new JSONResponse<>(100, "해당차량번호 조회권한 없음");
            case "500":
                return new JSONResponse<>(100, "요청권한 없음");
            case "507":
                return new JSONResponse<>(100, "허용 rType이 아님");
            case "888":
                return new JSONResponse<>(100, "PORT 오류");
            case "999":
                return new JSONResponse<>(100, "조회오류");
            default:
                return new JSONResponse<>(100, "인증 실패");
        }
    }


    /**
     * 보험 이력조회 필요 데이터 return
     * @param objectMapper 보험 이력 조회 정보
     * @param linkHistory 저장할 entity
     * @param responseBody 필요 데이터
     * @return
     * @throws JsonProcessingException
     */
    private JSONResponse<?> processResponse(ObjectMapper objectMapper, LinkHistory linkHistory, String responseBody) throws JsonProcessingException {
        Map<String, Object> dataMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});

        List<List<Map<String, String>>> r202List = extractData(dataMap, "r202");
        List<List<Map<String, String>>> r602List = extractData(dataMap, "r602");
        List<List<Map<String, String>>> r502List = extractData(dataMap, "r502");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("r202List", r202List);
        resultMap.put("r602List", r602List);
        resultMap.put("r502List", r502List);

        return new JSONResponse<>(200, "SUCCESS", linkHistory, resultMap);
    }

    /**
     * 보험 이력 조회 데이터 가공
     * @param dataMap 보험 이력 조회 정보
     * @param key 데이터 키
     * @return
     */
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

    /**
     * 보험 이력 조회 테이블에 조회 한 결과 저장
     * @param authentication 로그인 정보
     * @param productId 상품 아이디
     * @param data 저장할 정보
     * @return
     * @throws JsonProcessingException
     */
    private LinkHistory saveLinkHistory(Authentication authentication, Long productId, String data) throws JsonProcessingException {
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(),MemberStatus.NORMAL);
        ObjectMapper objectMapper = new ObjectMapper();

        Map dataMap = objectMapper.readValue(data, Map.class);
        LinkHistory linkHistory = LinkHistory.builder()
                .memberId(member.getId())
                .productId(productId)
                .data(data)
                // 일반전손사고일자
                .totalLossCount(dataMap.get("r405") != null ? dataMap.get("r405").toString() : "-")
// 침수전/분손사고건수
                .floodLossCount(dataMap.get("r407") != null ? dataMap.get("r407").toString() : "-")
// 도난전손사고건수
                .theftLossCount(dataMap.get("r409") != null ? dataMap.get("r409").toString() : "-")
// 도난전손사고일자
                .theftLossDate(dataMap.get("r410-01") != null ? dataMap.get("r410-01").toString() : "-")
// 용도 (1:관용, 2:자가용, 3:영업용, 4:개인택시)
// 배열 안에 r202-05 사용
                .useType(dataMap.get("r202") != null ? dataMap.get("r202").toString() : "-")
// 자차피해사고횟수
                .selfDamageAccidentCount(dataMap.get("r401") != null ? dataMap.get("r401").toString() : "-")
// 자차피해보험금 합
                .selfDamageInsuranceSum(dataMap.get("r402") != null ? dataMap.get("r402").toString() : "-")
// 상대방차피해사고 횟수
                .otherCarDamageAccidentCount(dataMap.get("r403") != null ? dataMap.get("r403").toString() : "-")
// 상대방차피해보험금 합
                .otherCarDamageInsuranceSum(dataMap.get("r404") != null ? dataMap.get("r404").toString() : "-")
// 소유자 변경 횟수
                .ownerChangeCount(dataMap.get("r204") != null ? Long.parseLong(dataMap.get("r204").toString()) : 0L)
// 소유자 변경 타입 (01:최초등록, 02:차량번호변경, 04:소유자 변경)
                .ownerChangeType(dataMap.get("r202") != null ? dataMap.get("r202").toString() : "-")
// 차량 이름
                .carName(dataMap.get("r111") != null ? dataMap.get("r111").toString() : "-")
// 차량 상세 이름
                .carNameDetail(dataMap.get("r005") != null ? dataMap.get("r005").toString() : "-")
// 차량 연식
                .carYear(dataMap.get("r004") != null ? dataMap.get("r004").toString() : "-")
// 차량 배기량
                .carDisplacement(dataMap.get("r104") != null ? dataMap.get("r104").toString() : "-")
// 조회 일자
                .standardDate(dataMap.get("r003") != null ? dataMap.get("r003").toString() : "-")
// 차체형상
                .carBodyShape(dataMap.get("r107") != null ? dataMap.get("r107").toString() : "-")
// 차량 옵션
                .carOption(dataMap.get("r108") != null ? dataMap.get("r108").toString() : "-")
// 최초보험가입일
                .firstInsuranceDate(dataMap.get("r105") != null ? dataMap.get("r105").toString() : "-")
// 사용 연료
                .fuelType(dataMap.get("r106") != null ? dataMap.get("r106").toString() : "-")
// 관용 이력
                .personalUseHistory(dataMap.get("r301") != null ? dataMap.get("r301").toString() : "-")
// 영업용 이력
                .businessUseHistory(dataMap.get("r302") != null ? dataMap.get("r302").toString() : "-")
// 대여용 이력
                .rentalUseHistory(dataMap.get("r303") != null ? dataMap.get("r303").toString() : "-")
// 차량 주행거리 배열
                .carDistanceDrive(dataMap.get("r602") != null ? dataMap.get("r602").toString() : "-")
// 미가입 기간
                .noJoinDate(dataMap.get("r511-01") != null ? dataMap.get("r511-01").toString() : "-")
                .build();

        try {
            linkHistoryRepository.save(linkHistory);
        } catch (Exception e) {
            log.error("LinkHistory save error controller (insert error =============================) : " + e.getMessage());
        }

        return linkHistory;
    }

}
