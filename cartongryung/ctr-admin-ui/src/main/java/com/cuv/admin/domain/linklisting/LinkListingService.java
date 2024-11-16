package com.cuv.admin.domain.linklisting;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import com.cuv.admin.domain.linkcode.LinkCodeRepository;
import com.cuv.admin.domain.linkcode.entity.LinkCode;
import com.cuv.admin.domain.linkcolorinfo.LinkColorInfoRepository;
import com.cuv.admin.domain.linkcolorinfo.entity.LinkColorInfo;
import com.cuv.admin.domain.linkinfo.LinkInfoRepository;
import com.cuv.admin.domain.linkinfo.entity.LinkInfo;
import com.cuv.admin.domain.linklisting.dto.LinkListingListDto;
import com.cuv.admin.domain.linklisting.dto.LinkListingSearchDto;
import com.cuv.admin.domain.linklisting.entity.LinkListing;
import com.cuv.admin.domain.linkoptioninfo.LinkOptionInfoRepository;
import com.cuv.admin.domain.linkoptioninfo.entity.LinkOptionInfo;
import com.cuv.admin.domain.linkoptioninfo.enumset.OptionCategory;
import com.cuv.admin.domain.linkspecinfo.LinkSpecInfoRepository;
import com.cuv.admin.domain.linkspecinfo.entity.LinkSpecInfo;
import com.cuv.admin.domain.linkvehicleinfo.LinkVehicleInfoRepository;
import com.cuv.admin.domain.linkvehicleinfo.entity.LinkVehicleInfo;
import com.cuv.admin.domain.member.enumset.MemberRole;
import com.cuv.admin.domain.memberadmin.MemberAdminRepository;
import com.cuv.admin.domain.memberadmin.MemberAdminService;
import com.cuv.admin.domain.memberadmin.entity.MemberAdmin;
import com.cuv.admin.domain.product.ProductRepository;
import com.cuv.admin.domain.product.entity.Product;
import com.cuv.admin.domain.product.enumset.CarSize;
import com.cuv.admin.domain.product.enumset.ExteriorShape;
import com.cuv.admin.domain.product.enumset.PostStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

/**
 * 카메니저, 카이즈유 관련 정보 처리(조히, 저장)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LinkListingService {

    private final WebClient webClient;
    private final LinkListingRepository linkListingRepository;
    private final LinkInfoRepository linkInfoRepository;
    private final LinkCodeRepository linkCodeRepository;
    private final LinkVehicleInfoRepository linkVehicleInfoRepository;
    private final LinkSpecInfoRepository linkSpecInfoRepository;
    private final LinkOptionInfoRepository linkOptionInfoRepository;
    private final LinkColorInfoRepository linkColorInfoRepository;
    private final ProductRepository productRepository;
    private final MemberAdminRepository memberAdminRepository;
    private final MemberAdminService memberAdminService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final int batchSize = 100;

    // String baseUrl = "https://devextapi.carmanager.co.kr"; // 테스트 서버
//     String baseUrl = "https://extapi.carmanager.co.kr"; // 운영 서버
    String baseUrl = "http://api.ctrm.co.kr/carmanager"; // 운영 서버

    @Autowired
    public LinkListingService(LinkListingRepository linkListingRepository,
                              LinkInfoRepository linkInfoRepository, LinkCodeRepository linkCodeRepository,
                              LinkVehicleInfoRepository linkVehicleInfoRepository, LinkSpecInfoRepository linkSpecInfoRepository,
                              LinkOptionInfoRepository linkOptionInfoRepository, LinkColorInfoRepository linkColorInfoRepository,
                               ProductRepository productRepository, MemberAdminRepository memberAdminRepository, MemberAdminService memberAdminService) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .codecs(codecs -> codecs
                        .defaultCodecs()
                        .maxInMemorySize(1024 * 1024 * 1024))
                .build();
        this.linkListingRepository = linkListingRepository;
        this.linkInfoRepository = linkInfoRepository;
        this.linkCodeRepository = linkCodeRepository;
        this.linkVehicleInfoRepository = linkVehicleInfoRepository;
        this.linkSpecInfoRepository = linkSpecInfoRepository;
        this.linkOptionInfoRepository = linkOptionInfoRepository;
        this.linkColorInfoRepository = linkColorInfoRepository;
        this.productRepository = productRepository;
        this.memberAdminRepository = memberAdminRepository;
        this.memberAdminService = memberAdminService;
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카매니저 가져오기
     * 매물정보 가져오기
     *
     * @param condition 날짜 정보를 담은 DTO
     * @param session   진행 상태를 담을 세션
     * @author SungHa
     */
    public void adminManagementGetLinkage(LinkListingSearchDto condition, HttpSession session) throws JsonProcessingException {
        int sdate = Integer.parseInt(condition.getSdate().replaceAll("-", ""));

        // uri 생성
        String uri = "/dsauto/DSAuto_" + sdate + ".json";

        // GET 요청 수행
        String strJson = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        // JSON 문자열이 null이 아니면 BOM(Byte Order Mark) 제거
        if (strJson != null) {
            strJson = strJson.replace("\ufeff", "");

            // ObjectMapper로 Json 파싱
            JsonNode jsonNode = objectMapper.readTree(strJson);
            session.setAttribute("status", "running");

            // 파싱된 JsonNode를 전달
            // Gateway close로 인한 비동기 처리
            CompletableFuture.runAsync(() -> {
                adminWriteLinkage(jsonNode, session);
            });
        }

    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 매물정보 가져오기 - 카매니저
     *
     * @param jsonNode 매물 정보가 담긴 json
     * @param session  추가된 매물 건수를 담을 세션
     * @author SungHa
     */
    protected void adminWriteLinkage(JsonNode jsonNode, HttpSession session) {
        AtomicInteger length = new AtomicInteger();

        for (int i = 0; i < jsonNode.size(); i++) {
            JsonNode jn = jsonNode.get(i);

            Optional<LinkListing> linkageData = linkListingRepository.findById(jn.get("CarNo").asLong());

            LinkListing linkListing;

            // 값이 있을 경우
            if (linkageData.isPresent()) {
                linkListing = linkageData.get();
                linkListing.setState(3); // 상태값: 중복

                linkListingRepository.save(linkListing);

            } else {
                LinkListing newLinkListing = LinkListing.builder()
                        .id(jn.get("CarNo").asLong())
                        .carJesiNo(jn.get("CarJesiNo").asLong())
                        .shopNo(jn.get("ShopNo").asLong())
                        .shopName(jn.get("ShopName").asText())
                        .userName(jn.get("UserName").asText())
                        .userHp(jn.get("UserHP").asText())
                        .carPlateNumber(jn.get("CarPlateNumber").asText())
                        .carName(jn.get("CarName").asText())
                        .carRegYear(jn.get("CarRegYear").asText())
                        .carFrameNo(jn.get("CarFrameNo").asText())
                        .carRegDay(jn.get("CarRegDay").asText())
                        .carMakerNo(jn.get("CarMakerNo").asLong())
                        .carModelNo(jn.get("CarModelNo").asLong())
                        .carModelDetailNo(jn.get("CarModelDetailNo").asLong())
                        .carGradeNo(jn.get("CarGradeNo").asLong())
                        .carGradeDetailNo(jn.get("CarGradeDetailNo").asLong())
                        .carCheckoutNo(jn.get("CarCheckoutNo").asLong())
                        .carMission(jn.get("CarMission").asText())
                        .carFuel(jn.get("CarFuel").asText())
                        .carUseKm(jn.get("CarUseKm").asLong())
                        .carColor(jn.get("CarColor").asText())
                        .carAmountSale(jn.get("CarAmountSale").asLong() * 10000)
                        .carTruckTon(jn.get("CarTruckTon").asLong())
                        .carContent(jn.get("CarContent").asText())
                        .checkouturl(jn.get("Checkouturl").asText())
                        .lastUpdateDate(jn.get("LastUpdateDate").asText())
                        .sido(jn.get("Sido").asText())
                        .city(jn.get("City").asText())
                        .imageInfo(String.valueOf(jn.get("ImageInfo")))
                        .state(1) // 상태값: 신규
                        .build();

                linkListingRepository.save(newLinkListing);
                length.getAndIncrement();

                if (i % batchSize == 0) {
                    linkListingRepository.flush();
                    linkListingRepository.clear();
                }
            }
        }

        linkListingRepository.flush();
        linkListingRepository.clear();

        // 매물에서 상태값이 2(기존)인 차대번호를 가진 상품 삭제 처리
        // 매물에서 상태값이 2(기존)인 매물 상태값 4(삭제)로 전환
        linkListingRepository.deleteAllByState();

        // 매물에서 상태값이 2(기존)인 매물을 4(삭제) 및 삭제처리로 전환
        linkListingRepository.revertState();

        // 완료 처리
        session.setAttribute("status", "complete");
        session.setAttribute("count", length);
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카매니저 가져오기 > 상태 확인
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기 > 상태 확인
     *
     * @param session 진행 상태 및 추가된 매물 건수를 담을 세션
     * @author SungHa
     */
    public HttpSession adminManagementGetStatus(HttpSession session) {
        if (session.getAttribute("status") == "complete") {
            session.removeAttribute("message");
            return session;
        } else if (session.getAttribute("status") == "fail") {
            throw new IllegalArgumentException(String.valueOf(session.getAttribute("message")));
        }

        return null;
    }

    /**
     * 관리자 | 차량관리 > 매물 연동 관리 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    public Page<LinkListingListDto> searchAllLinkage(LinkListingSearchDto condition, Pageable pageable) {
        return linkListingRepository.searchAllLinkage(condition, pageable);
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기
     * 중고차 정보를 얻기 위한 치량번호 및 소유주 저장 (s1)
     *
     * @param idList  차량 정보를 조회할 시퀀스 값의 배열
     * @param session 진행 상태를 담을 세션
     * @author SungHa
     */
    @Transactional
    public void adminManagementGetCarInfo(List<Long> idList, HttpSession session) throws JsonProcessingException {
        List<Map<String, String>> linkageList = new ArrayList<>();

        for (Long id : idList) {
            Map<String, String> linkageMap = new LinkedHashMap<>();
            session.setAttribute("status", "running");

            LinkListing linkListing = linkListingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 데이터입니다."));
            linkageMap.put("VHRNO", linkListing.getCarPlateNumber());

            log.info("vhrno ******************************** {}", linkageMap.get("VHRNO"));

            linkageList.add(linkageMap);
        }
        adminSendCarIsYou(linkageList, session);
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기
     * 중고차 정보를 얻기 위한 치량번호 및 소유주 저장 (s1)
     *
     * @param linkageList 차량번호와 소유주명을 담은 map
     * @param session     진행 상태를 담은 세션
     * @author SungHa
     */
    @Transactional
    protected void adminSendCarIsYou(List<Map<String, String>> linkageList, HttpSession session) throws JsonProcessingException {
        // 차량 정보 조히
        String carInfoUrl = "http://api.ctrm.co.kr/carisyou/P1/2";

        List<CompletableFuture<Boolean>> futures = linkageList.stream().map(map -> {
            return CompletableFuture.supplyAsync(() -> {
                log.info("카이즈유 전송 ************************");
                try {
                    return webClient.post()
                            .uri(carInfoUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(objectMapper.writeValueAsString(map))
                            .retrieve()
                            .bodyToMono(String.class)
                            .toFuture()
                            .thenApply(strCarInfoJson -> {
                                if (strCarInfoJson != null) {
                                    try {
                                        strCarInfoJson = strCarInfoJson.replace("\ufeff", "");
                                        JsonNode jsonNode = null;
                                        jsonNode = objectMapper.readTree(strCarInfoJson);
                                        if ("000".equals(jsonNode.get("INFO").get("PROCESS_IMPRTY_RESN_CODE").asText())) {
                                            adminWriteCarInfo(jsonNode, map, session);
                                            return true;
                                        } else {
                                            log.info("Processing failed for map: {}", map);
                                        }
                                    } catch (JsonProcessingException e) {
                                        return false;
                                    }
                                }
                                return false;
                            })
                            .exceptionally(e -> {
                                log.error("Error processing request for map: {}", map, e);
                                return false;
                            });
                } catch (JsonProcessingException e) {
                    log.error("Error processing JSON for map: {}", map, e);
                    return CompletableFuture.completedFuture(false);
                }
            }).thenCompose(result -> result); // CompletableFuture<CompletableFuture<Boolean>> -> CompletableFuture<Boolean>
        }).toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        CompletableFuture<List<Boolean>> allResults = allOf.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));

        try {
            List<Boolean> results = allResults.get();
            long successCount = results.stream().filter(result -> result).count();
            long failCount = results.size() - successCount;

            session.setAttribute("successCount", successCount);
            session.setAttribute("failCount", failCount);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error waiting for results", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기
     * 선택된 차량 번호로 조회된 중고 차량 정보 저장
     *
     * @param jsonNode 중고차 정보를 담은 json
     * @param map      차량 번호를 담은 map
     * @param session  진행 상태를 담은 세션
     * @author SungHa
     */
    @Transactional
    protected void adminWriteCarInfo(JsonNode jsonNode, Map<String, String> map, HttpSession session) throws JsonProcessingException {
        log.info("adminWriteCarInfo ******************************************");

        Optional<LinkInfo> linkInfoData = linkInfoRepository.findByVhrno(map.get("VHRNO"));

        log.info("adminWriteCarInfo linkInfoData ****************************************** {}", linkInfoData);

        LinkInfo linkInfo;
        if (linkInfoData.isEmpty()) {
            linkInfo = LinkInfo.builder()
                    .vin(getAsText(jsonNode.get("INFO").get("CARINFO"), "VIN"))
                    .vhrno(map.get("VHRNO"))
                    .brandNbr(getAsLong(jsonNode.get("INFO").get("CARINFO"), "BRAND_NBR"))
                    .brandNm(getAsText(jsonNode.get("INFO").get("CARINFO"), "BRAND_NM"))
                    .repCarClassNbr(getAsLong(jsonNode.get("INFO").get("CARINFO"), "REP_CAR_CLASS_NBR"))
                    .repCarClassNm(getAsText(jsonNode.get("INFO").get("CARINFO"), "REP_CAR_CLASS_NM"))
                    .yearType(getAsText(jsonNode.get("INFO").get("CARINFO"), "YEAR_TYPE"))
                    .carClassNbr(getAsLong(jsonNode.get("INFO").get("CARINFO"), "CAR_CLASS_NBR"))
                    .carClassNm(getAsText(jsonNode.get("INFO").get("CARINFO"), "CAR_CLASS_NM"))
                    .carGradeNbr(getAsLong(jsonNode.get("INFO").get("CARINFO").get("GRADE_LIST").get(0), "CAR_GRADE_NBR"))
                    .carGradeNm(getAsText(jsonNode.get("INFO").get("CARINFO").get("GRADE_LIST").get(0), "CAR_GRADE_NM"))
                    .gradeFuelRate(getAsText(jsonNode.get("INFO").get("CARINFO").get("GRADE_LIST").get(0), "GRADE_FUEL_RATE"))
                    .tireSizeFront(getAsText(jsonNode.get("INFO").get("CARINFO"), "TIRE_ANFR_FOM"))
                    .tireSizeBack(getAsText(jsonNode.get("INFO").get("CARINFO"), "TIRE_PSFR_FOM"))
                    .colorNm(getAsText(jsonNode.get("INFO").get("CARINFO"), "COLOR_NM"))
                    .fuel(getAsText(jsonNode.get("INFO").get("CARINFO"), "FUEL"))
                    .enginesize(getAsText(jsonNode.get("INFO").get("CARINFO"), "ENGINESIZE"))
                    .istdTrans(getAsText(jsonNode.get("INFO").get("CARINFO"), "ISTD_TRANS"))
                    .extShape(getAsText(jsonNode.get("INFO").get("CARINFO"), "EXT_SHAPE"))
                    .person(getAsText(jsonNode.get("INFO").get("CARINFO"), "TKCAR_PSCAP_CO"))
                    .engineForm(getAsText(jsonNode.get("INFO").get("CARINFO"), "ENGINE_FORM"))
                    .prye(getAsText(jsonNode.get("INFO").get("CARINFO"), "PRYE"))
                    .insptValidPdDe(convertDateRange(Objects.requireNonNull(getAsText(jsonNode.get("INFO").get("CARINFO"), "INSPT_VALID_PD_DE_FULL"))))
                    .trvlDstnc(getAsLong(jsonNode.get("INFO").get("CARINFO"), "TRVL_DSTNC"))
                    .frstRegistDe(getAsText(jsonNode.get("INFO").get("CARINFO"), "FRST_REGIST_DE"))
                    .carInfoJson(objectMapper.writeValueAsString(jsonNode))
                    .build();
            linkInfoRepository.save(linkInfo);

            // 새로운 데이터일 떄 코드 추가
            adminManagementLinkCodeSave(linkInfo, null);

            session.setAttribute("status", "complete");

            log.info("END *********************************************************************");
        }
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기
     * 신차 업데이트 차량 상세정보 조회 (s4) > 자동차검사기간 데이터 변환
     *
     * @param input 자동차검사기간
     * @author SungHa
     */
    private String convertDateRange(String input) {
        String[] dates = input.split("~");
        if (dates.length != 2) {
            throw new IllegalArgumentException("Invalid input format, expected yyyymmdd~yyyymmdd");
        }

        LocalDate startDate = LocalDate.parse(dates[0], DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate endDate = LocalDate.parse(dates[1], DateTimeFormatter.ofPattern("yyyyMMdd"));

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedStartDate = startDate.format(outputFormatter);
        String formattedEndDate = endDate.format(outputFormatter);

        return formattedStartDate + "~" + formattedEndDate;
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기
     * 신차 업데이트 차량 목록 조회 (s3)
     *
     * @author SungHa
     */
    public List<Map<String, String>> adminManagementGetNewCarGradeList() throws JsonProcessingException {
        log.info("adminManagementGetNewCarGradeList S3 ***************************");

        // 신차 업데이트 내역 조히
//        String carUpdatedUrl = "https://usedcardev.carisyou.net/carinfo/ctrm/S3/2";
//        String carUpdatedUrl = "https://usedcar.carisyou.net/carinfo/ctrm/S3/2";
        String carUpdatedUrl = "http://api.ctrm.co.kr/carisyou/S3/2";

        Map<String, Object> carInfoMap = new LinkedHashMap<>();
        List<Map<String, String>> newUpdatedGradeList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        carInfoMap.put("REGIST_DE", now.format(formatter));
        log.info("REGIST_DE = {} ", now.format(formatter));

        // POST 요청 수행
        String strNewGradeListJson = webClient.post()
                .uri(carUpdatedUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(carInfoMap))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // JSON 문자열이 null이 아니면 BOM(Byte Order Mark) 제거
        if (strNewGradeListJson != null) {
            strNewGradeListJson = strNewGradeListJson.replace("\ufeff", "");

            // ObjectMapper로 Json 파싱
            JsonNode jsonNode = objectMapper.readTree(strNewGradeListJson);

            if ("000".equals(jsonNode.get("INFO").get("PROCESS_IMPRTY_RESN_CODE").asText())) {
                if (!jsonNode.get("INFO").get("GRADE_LIST").isEmpty()) {
                    log.info("result grade list NOT empty ***************************");

                    newUpdatedGradeList = adminGetNewUpdatedGrade(jsonNode, now.format(formatter));
                }
            } else {
                throw new IllegalArgumentException(jsonNode.get("INFO").get("PROCESS_IMPRTY_RESN_DTLS").asText());
            }
        }

        log.info("return newUpdatedGradeList SUCCESS *************************** ");

        return newUpdatedGradeList;
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기
     * 해당 날짜에 대해 업데이트된 신차 데이터가 있을 때, 해당 차량들의 상세 정보를 조회를 위한 설정
     *
     * @param jsonNode   조회 날짜를 담은 json
     * @param registDate 현재 날짜
     * @author SungHa
     */
    public List<Map<String, String>> adminGetNewUpdatedGrade(JsonNode jsonNode, String registDate) {
        log.info("adminGetNewUpdatedGrade *************************** ");
        List<Map<String, String>> newCarList = new ArrayList<>();

        for (JsonNode js : jsonNode.get("INFO").get("GRADE_LIST")) {
            Map<String, String> newCarMap = new LinkedHashMap<>();

            newCarMap.put("REGIST_DE", registDate);
            newCarMap.put("CAR_GRADE_NBR", js.get("CAR_GRADE_NBR").asText());

            newCarList.add(newCarMap);
        }

        log.info("newCarList size *************************** = {} ", newCarList.size());

        return newCarList;
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기
     * 신차 업데이트 차량 상세정보 조회 (s4)
     *
     * @param newUpdatedGradeList 업데이트된 신차 등급 번호 목록
     * @author SungHa
     */
    @Transactional
    public void adminManagementGetUpdatedData(List<Map<String, String>> newUpdatedGradeList) throws JsonProcessingException {
        log.info("adminManagementGetUpdatedData S4 ***************************");

        // 업데이트 차량 상세 정보
//        String carDetailUrl = "https://usedcardev.carisyou.net/carinfo/ctrm/S4/2";
//        String carDetailUrl = "https://usedcar.carisyou.net/carinfo/ctrm/S4/2";
        String carDetailUrl = "http://api.ctrm.co.kr/carisyou/S4/2";

        for (Map<String, String> gradeMap : newUpdatedGradeList) {
            // POST 요청 수행
            String strCarDetailJson = webClient.post()
                    .uri(carDetailUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(gradeMap))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // JSON 문자열이 null이 아니면 BOM(Byte Order Mark) 제거
            log.info("strCarDetailJson = {} ", strCarDetailJson);

            if (strCarDetailJson != null) {
                strCarDetailJson = strCarDetailJson.replace("\ufeff", "");

                // ObjectMapper로 Json 파싱
                JsonNode jsonNode = objectMapper.readTree(strCarDetailJson);

                if ("000".equals(jsonNode.get("INFO").get("PROCESS_IMPRTY_RESN_CODE").asText())) {
                    if (!jsonNode.get("INFO").get("BASIC_INFO").isEmpty() || !jsonNode.get("INFO").get("SPEC_INFO_LIST").isEmpty()
                            || !jsonNode.get("INFO").get("OPT_INFO_LIST").isEmpty() || !jsonNode.get("INFO").get("COLOR_INFO_LIST").isEmpty()) {
                        log.info("More Than One Information ***************************");
                        adminWriteUpdatedData(jsonNode);
                    }
                } else {
                    throw new IllegalArgumentException(jsonNode.get("INFO").get("PROCESS_IMPRTY_RESN_DTLS").asText());
                }
            }
        }
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기
     * 신차 데이터 있으면 각 정보 테이블 조회 후 새로운 데이터 저장
     *
     * @param jsonNode 신차 정보를 답은 json
     * @author SungHa
     */
    @Transactional
    public void adminWriteUpdatedData(JsonNode jsonNode) throws JsonProcessingException {
        log.info("adminWriteUpdatedData ***************************");
        log.info("LinkVehicleInfo Build Start ***************************");

        // 차량 정보
        JsonNode vehicleInfoList = jsonNode.get("INFO").get("BASIC_INFO");

        for (JsonNode vehicleInfo : vehicleInfoList) {
            LinkVehicleInfo linkVehicleInfo = LinkVehicleInfo.builder()
                    .ciuCode(getAsText(vehicleInfo, "CIU_CODE"))
                    .brandNbr(getAsLong(vehicleInfo, "BRAND_NBR"))
                    .brandNm(getAsText(vehicleInfo, "BRAND_NM"))
                    .brandEngNm(getAsText(vehicleInfo, "BRAND_ENG_NM"))
                    .prodNationCd(getAsText(vehicleInfo, "PROD_NATION_CD"))
                    .importYn(getAsEnum(vehicleInfo, "IMPORT_YN"))
                    .brandImageUrl(getAsText(vehicleInfo, "BRAND_IMG_URL"))
                    .brandUrl(getAsText(vehicleInfo, "BRAND_URL"))
                    .brandExposureYn(getAsEnum(vehicleInfo, "BRAND_EXPOSURE_YN"))
                    .brandActiveYn(getAsEnum(vehicleInfo, "BRAND_ACTIVE_YN"))
                    .repCarClassNbr(getAsLong(vehicleInfo, "REP_CAR_CLASS_NBR"))
                    .repCarClassNm(getAsText(vehicleInfo, "REP_CAR_CLASS_NM"))
                    .repActiveYn(getAsEnum(vehicleInfo, "REP_ACTIVE_YN"))
                    .repExposureYn(getAsEnum(vehicleInfo, "REP_EXPOSURE_YN"))
                    .carClassNbr(getAsLong(vehicleInfo, "CAR_CLASS_NBR"))
                    .carClassNm(getAsText(vehicleInfo, "CAR_CLASS_NM"))
                    .carClassEngNm(getAsText(vehicleInfo, "CAR_CLASS_ENG_NM"))
                    .carRivalNbr(getAsText(vehicleInfo, "CAR_RIVAL_NBR"))
                    .yearType(getAsText(vehicleInfo, "YEAR_TYPE"))
                    .repImageUrl(getAsText(vehicleInfo, "REP_IMG_URL"))
                    .classActiveYn(getAsEnum(vehicleInfo, "CLASS_ACTIVE_YN"))
                    .classExposureYn(getAsEnum(vehicleInfo, "CLASS_EXPOSURE_YN"))
                    .carGradeNbr(getAsLong(vehicleInfo, "CAR_GRADE_NBR"))
                    .carGradeNm(getAsText(vehicleInfo, "CAR_GRADE_NM"))
                    .carGradeNmEng(getAsText(vehicleInfo, "CAR_GRADE_NM_ENG"))
                    .salesSeCd(getAsInt(vehicleInfo, "SALES_SE_CD"))
                    .salePrice(getAsInt(vehicleInfo, "SALE_PRICE"))
                    .salePriceUnit(getAsInt(vehicleInfo, "SALE_PRICE_UNIT"))
                    .recommYn(getAsEnum(vehicleInfo, "RECOMM_YN"))
                    .carSize(getAsText(vehicleInfo, "CAR_SIZE"))
                    .extShape(getAsText(vehicleInfo, "EXT_SHAPE"))
                    .istdTrans(getAsText(vehicleInfo, "ISTD_TRANS"))
                    .releaseDt(getAsInt(vehicleInfo, "RELEASE_DT"))
                    .discontinuedDt(getAsText(vehicleInfo, "DISCONTINUED_DT"))
                    .carAsortCode(getAsInt(vehicleInfo, "CAR_ASORT_CODE"))
                    .cmtYn(getAsEnum(vehicleInfo, "CMT_YN"))
                    .useType(getAsInt(vehicleInfo, "USE_TYPE"))
                    .gradeActiveYn(getAsEnum(vehicleInfo, "GRADE_ACTIVE_YN"))
                    .vehicleInfoJson(objectMapper.writeValueAsString(jsonNode))
                    .build();

            linkVehicleInfoRepository.save(linkVehicleInfo);

            log.info("LinkVehicleInfo Build End ***************************");

            // 새로운 데이터일 떄 코드 추가
            adminManagementLinkCodeSave(null, linkVehicleInfo);

            log.info("LinkCode Save End ***************************");
        }

        // 제원 정보
        JsonNode specInfoList = jsonNode.get("INFO").get("SPEC_INFO_LIST");
        List<LinkSpecInfo> newSpecInfoList = new ArrayList<>();
        log.info("LinkSpecInfo Build Start ***************************");

        for (JsonNode specInfo : specInfoList) {
            LinkSpecInfo linkSpecInfo = LinkSpecInfo.builder()
                    .carGradeNbr(getAsLong(specInfo, "CAR_GRADE_NBR"))
                    .specImageId(getAsText(specInfo, "SPEC_IMAGE_ID"))
                    .specCtgry(getAsText(specInfo, "SPEC_CTGRY"))
                    .specNm(getAsText(specInfo, "SPEC_NM"))
                    .alphanumCtgry(getAsText(specInfo, "ALPHANUM_CTGRY"))
                    .specMultiItem(getAsText(specInfo, "SPEC_MULTI_ITEM"))
                    .specUom(getAsText(specInfo, "SPEC_UOM"))
                    .specValue(getAsText(specInfo, "SPEC_VALUE"))
                    .brandActiveYn(YN.Y)
                    .brandExposureYn(YN.Y)
                    .repActiveYn(YN.Y)
                    .repExposureYn(YN.Y)
                    .classActiveYn(YN.Y)
                    .classExposureYn(YN.Y)
                    .gradeActiveYn(YN.Y)
                    .gradeExposureYn(YN.Y)
                    .cmtYn(YN.N)
                    .salesSeCd(1)
                    .build();

            newSpecInfoList.add(linkSpecInfo);

            log.info("LinkSpecInfo Build End Size *************************** = {} ", newSpecInfoList.size());
        }

        linkSpecInfoRepository.saveAll(newSpecInfoList);

        log.info("LinkSpecInfo Save End ***************************");

        // 옵션 정보
        JsonNode optionInfoList = jsonNode.get("INFO").get("OPT_INFO_LIST");
        List<LinkOptionInfo> newOptionInfoList = new ArrayList<>();
        log.info("LinkOptionInfo Build Start ***************************");

        for (JsonNode optionInfo : optionInfoList) {
            LinkOptionInfo linkOptionInfo = LinkOptionInfo.builder()
                    .carGradeNbr(getAsLong(optionInfo, "CAR_GRADE_NBR"))
                    .optType(getAsInt(optionInfo, "OPT_TYPE"))
                    .optPickType(getAsText(optionInfo, "OPT_PICK_TYPE"))
                    .optCtgry(OptionCategory.getDetailByCode(getAsText(optionInfo, "OPT_CTGRY")))
                    .optNm(getAsText(optionInfo, "OPT_NM"))
                    .pickOptPrice(getAsText(optionInfo, "PICK_OPT_PRICE"))
                    .pickOptDesc(getAsText(optionInfo, "PICK_OPT_DESC"))
                    .pickOptGrpCd(getAsText(optionInfo, "PICK_OPT_GRP_CD"))
                    .brandActiveYn(YN.Y)
                    .brandExposureYn(YN.Y)
                    .repActiveYn(YN.Y)
                    .repExposureYn(YN.Y)
                    .classActiveYn(YN.Y)
                    .classExposureYn(YN.Y)
                    .gradeActiveYn(YN.Y)
                    .gradeExposureYn(YN.Y)
                    .cmtYn(YN.N)
                    .salesSeCd(1)
                    .build();

            newOptionInfoList.add(linkOptionInfo);

            log.info("LinkOptionInfo Build End Size *************************** = {} ", newOptionInfoList.size());
        }

        linkOptionInfoRepository.saveAll(newOptionInfoList);

        log.info("LinkOptionInfo Save End ***************************");

        // 색상 정보
        JsonNode colorInfoList = jsonNode.get("INFO").get("COLOR_INFO_LIST");
        List<LinkColorInfo> newColorInfoList = new ArrayList<>();
        log.info("LinkColorInfo Build Start ***************************");

        for (JsonNode colorInfo : colorInfoList) {
            LinkColorInfo linkColorInfo = LinkColorInfo.builder()
                    .carGradeNbr(getAsLong(colorInfo, "CAR_GRADE_NBR"))
                    .outClrNm(getAsText(colorInfo, "OUT_CLR_NM"))
                    .outClrPrice(getAsInt(colorInfo, "OUT_CLR_PRICE"))
                    .outClrIcon(getAsText(colorInfo, "OUT_CLR_ICON"))
                    .inClrNm(getAsText(colorInfo, "IN_CLR_NM"))
                    .inClrPrice(getAsInt(colorInfo, "IN_CLR_PRICE"))
                    .inClrIcon(getAsText(colorInfo, "IN_CLR_ICON"))
                    .brandActiveYn(YN.Y)
                    .brandExposureYn(YN.Y)
                    .repActiveYn(YN.Y)
                    .repExposureYn(YN.Y)
                    .classActiveYn(YN.Y)
                    .classExposureYn(YN.Y)
                    .gradeActiveYn(YN.Y)
                    .gradeExposureYn(YN.Y)
                    .cmtYn(YN.N)
                    .salesSeCd(1)
                    .build();

            newColorInfoList.add(linkColorInfo);

            log.info("LinkColorInfo Build End Size *************************** = {} ", newColorInfoList.size());
        }

        linkColorInfoRepository.saveAll(newColorInfoList);

        log.info("LinkColorInfo Save End ***************************");
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기 > json null 처리
     *
     * @param node      데이터가 담겨 있는 json
     * @param fieldName 데이터가 담겨 있는 key
     * @author SungHa
     */
    private String getAsText(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return (field != null && !field.isNull()) ? field.asText() : null;
    }

/**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기 > json null 처리
     *
     * @param node      데이터가 담겨 있는 json
     * @param fieldName 데이터가 담겨 있는 key
     * @author SungHa
     */
    private Long getAsLong(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return (field != null && !field.isNull()) ? field.asLong() : null;
    }

/**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기 > json null 처리
     *
     * @param node      데이터가 담겨 있는 json
     * @param fieldName 데이터가 담겨 있는 key
     * @author SungHa
     */
    private Integer getAsInt(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return (field != null && !field.isNull()) ? field.asInt() : null;
    }

/**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기 > json null 처리
     *
     * @param node      데이터가 담겨 있는 json
     * @param fieldName 데이터가 담겨 있는 key
     * @author SungHa
     */
    private YN getAsEnum(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return (field != null && !field.isNull()) ? YN.valueOf(field.asText()) : null;
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기 - 검색용 코드 저장
     * 새로운 넘버링일 떄 코드 테이블에 새로운 데이터 추가
     *
     * @param linkInfo        중고 차량 데이터
     * @param linkVehicleInfo 업데이트된 차량 정보 데이터
     * @author SungHa
     */
//    @Transactional
    protected void adminManagementLinkCodeSave(LinkInfo linkInfo, LinkVehicleInfo linkVehicleInfo) {
        log.info("code save *****************************************");
        // 중고 차량 정보 조회 시
        if (linkInfo != null) {
            processNewLinkCode(linkInfo);
        }

        // 신차 데이터 조회 시
        if (linkVehicleInfo != null) {
            processNewLinkCode(linkVehicleInfo);
        }
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기 - 검색용 코드 저장
     * 새로운 넘버링일 떄 코드 테이블에 새로운 데이터 추가
     *
     * @param linkInfo 중고 차량 데이터
     * @author SungHa
     */
    protected void processNewLinkCode(LinkInfo linkInfo) {
        log.info("processNewLinkCode ************************************");
        Long makerId = getOrCreateLinkCode(linkInfo.getBrandNbr(), linkInfo.getBrandNm(), 0L, 0);
        Long modelId = getOrCreateLinkCode(linkInfo.getRepCarClassNbr(), linkInfo.getRepCarClassNm(), makerId, 1);
        Long detailModelId = getOrCreateLinkCode(linkInfo.getCarClassNbr(), linkInfo.getCarClassNm(), modelId, 2);
        getOrCreateLinkCode(linkInfo.getCarGradeNbr(), linkInfo.getCarGradeNm(), detailModelId, 3);
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기 - 검색용 코드 저장
     * 새로운 넘버링일 떄 코드 테이블에 새로운 데이터 추가
     *
     * @param linkVehicleInfo 업데이트된 차량 정보 데이터
     * @author SungHa
     */
    private void processNewLinkCode(LinkVehicleInfo linkVehicleInfo) {
        Long makerId = getOrCreateLinkCode(linkVehicleInfo.getBrandNbr(), linkVehicleInfo.getBrandNm(), 0L, 0);
        Long modelId = getOrCreateLinkCode(linkVehicleInfo.getRepCarClassNbr(), linkVehicleInfo.getRepCarClassNm(), makerId, 1);
        Long detailModelId = getOrCreateLinkCode(linkVehicleInfo.getCarClassNbr(), linkVehicleInfo.getCarClassNm(), modelId, 2);
        getOrCreateLinkCode(linkVehicleInfo.getCarGradeNbr(), linkVehicleInfo.getCarGradeNm(), detailModelId, 3);
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 카이즈유 가져오기 - 검색용 코드 저장
     * 새로운 넘버링일 떄 코드 테이블에 새로운 데이터 추가
     *
     * @param id       연동 코드 일련번호
     * @param name     연동 코드 이름
     * @param parentId 연동 코드 상위 일련번호
     * @param depth    연동 코드 차수
     * @author SungHa
     */
    private Long getOrCreateLinkCode(Long id, String name, Long parentId, int depth) {
        log.info("getOrCreateLinkCode *****************************************");

        Optional<LinkCode> optionalLinkCode = linkCodeRepository.findById(id);

        if (optionalLinkCode.isEmpty()) {
            int viewOrder;
            Integer lastOrderSeq = linkCodeRepository.searchLastOrderSeq(parentId, depth);
            if (lastOrderSeq == null) {
                viewOrder = 1;
            } else {
                viewOrder = lastOrderSeq + 1;
            }

            LinkCode linkCode = LinkCode.builder()
                    .id(id)
                    .parentLinkNbrId(parentId)
                    .linkDataNm(name)
                    .depth(depth)
                    .viewOrder(viewOrder)
                    .attachment(null)
                    .afterServiceDate(null)
                    .viewYn(YN.Y)
                    .build();

            linkCodeRepository.save(linkCode);
            return id;
        } else {
            return optionalLinkCode.get().getId();
        }
    }

    /**
     * 관리자 | 차량관리 > 매물 연동 관리 > 상사명 변경
     *
     * @param shopNames 업데이트 할 상사명 목록
     * @author SungHa
     */
    @Transactional
    public void shopNamesUpdate(Map<String, String> shopNames) {
        for (Map.Entry<String, String> entry : shopNames.entrySet()) {
            String oldName = entry.getKey();
            String newName = entry.getValue();
            if (oldName.equals(newName)) {
                continue;
            }
            linkListingRepository.updateShopName(oldName, newName);
        }
    }

    /**
     * 관리자 | 차량관리 > 매물 연동 관리 > 차량 등록
     *
     * @param idList 등록할 차량의 시퀀스
     * @author SungHa
     */
    @Transactional
    public void adminManagementLinkageWriteProc(List<Long> idList) {
        for (Long id : idList) {
            log.info("매물 아이디={}", id);
            Optional<LinkListing> existingLinkListing = linkListingRepository.findById(id);
            if (existingLinkListing.isPresent()) {
            log.info("존재하는 linklisting 차량번호 ={}",existingLinkListing.get().getCarPlateNumber());
                Optional<LinkInfo> existingLinkInfo = linkInfoRepository.findByVhrno(existingLinkListing.get().getCarPlateNumber());
                if (existingLinkInfo.isPresent()) {
                    log.info("존재하는 linkinfo");
                    LinkListing linkListing = existingLinkListing.get();
                    log.info("존재하는 linkListing ={}", linkListing);
                    LinkInfo linkInfo = existingLinkInfo.get();
                    log.info("존재하는 linkinfo ={}", linkInfo);

                    // 차량 등록
                    proceedProductRegistration(linkListing, linkInfo);
                } else {
                    throw new IllegalArgumentException("해당 매물에 대한 정보를 모두 가져온 후 차량 등록을 해주세요. 차량 번호 = " + existingLinkListing.get().getCarPlateNumber());
                }
            } else {
                throw new IllegalArgumentException("해당 매물에 대한 정보를 모두 가져온 후 차량 등록을 해주세요.");
            }
        }
    }

    /**
     * 관리자 | 차량관리 > 매물 연동 관리 > 차량 등록
     *
     * @param linkListing 카매니저 매물 정보
     * @param linkInfo    카이즈유 제원 정보
     * @author SungHa
     */
    private void proceedProductRegistration(LinkListing linkListing, LinkInfo linkInfo) {
        log.info("proceedProductRegistration");
        // 딜러 랜덤 배정
        Long memberDealerId = memberAdminService.searchMemberDealerOrderByAssignmentAt(MemberRole.DEALER.getRole());

        // 상품 차량 번호 생성
        LocalDate currentDate = LocalDate.now();
        Random random = new Random();
        int randomNumber = random.nextInt(900) + 100;

        String productUniqueNumber = "C" + currentDate.format(DateTimeFormatter.ofPattern("yyMMdd")) + "-" + randomNumber;

        String carSize = null;
        List<LinkVehicleInfo> linkVehicleInfo = linkVehicleInfoRepository.findByCarGradeNbr(linkInfo.getCarGradeNbr());
        if (linkVehicleInfo != null && !linkVehicleInfo.isEmpty()) {
            carSize = linkVehicleInfo.get(0).getCarSize();
        }

        // 차량 이미지 변환
        List<AttachmentDto> attachmentDtoLists = new ArrayList<>();
        if (hasText(linkListing.getImageInfo())) {
            try {
                JsonNode jsonNode = objectMapper.readTree(linkListing.getImageInfo());
                for (JsonNode attachment : jsonNode) {
                    String imageUrl = attachment.get("ImageUrl").asText();
                    AttachmentDto attachmentDto = new AttachmentDto();
                    attachmentDto.setRealUrl(imageUrl);

                    attachmentDtoLists.add(attachmentDto);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        Product product = Product.builder()
                .memberDealerId(memberDealerId != null ? memberDealerId : 0L)
                .vehicleIdentificationNumber(linkListing.getCarFrameNo())
                .productUniqueNumber(productUniqueNumber)
                .carPlateNumber(linkListing.getCarPlateNumber())
                .carImageUrl(attachmentDtoLists)
                .shopName(linkListing.getShopName())
                .makerNumber(linkInfo.getBrandNbr())
                .modelNumber(linkInfo.getRepCarClassNbr())
                .detailModelNumber(linkInfo.getCarClassNbr())
                .detailGradeNumber(linkInfo.getCarGradeNbr())
                .extShape(ExteriorShape.ofEngName(linkInfo.getExtShape()))
                .carSize(CarSize.ofEngName(carSize))
                .carRegDay(linkListing.getCarRegDay())
                .carRegYear(linkListing.getCarRegYear())
                .carMission(linkListing.getCarMission())
                .carFuel(linkListing.getCarFuel())
                .carColor(linkListing.getCarColor())
                .carUseKm(linkListing.getCarUseKm())
                .city(linkListing.getCity())
                .carAmountSale(linkListing.getCarAmountSale())
                .performanceInspectionUrl(linkListing.getCheckouturl())
                .content(linkListing.getCarContent())
                .postStatus(PostStatus.WAITING)
                .hits(0L)
                .build();

        productRepository.save(product);

        // 차량 배정 일시 업데이트
        if (memberDealerId != null) {
            MemberAdmin memberAdmin = memberAdminRepository.findById(memberDealerId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 딜러입니다."));
            memberAdmin.setAssignmentAt(LocalDateTime.now());
        }

        linkListing.setListedYn(YN.Y);
    }

}
