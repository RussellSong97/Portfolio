package com.cuv.domain.linklisting;

import com.cuv.common.JSONResponse;
import com.cuv.common.YN;
import com.cuv.domain.linkcode.LinkCodeRepository;
import com.cuv.domain.linkcode.entity.LinkCode;
import com.cuv.domain.linkinfo.LinkInfoRepository;
import com.cuv.domain.linkinfo.dto.LinkInfoDetailDto;
import com.cuv.domain.linkinfo.entity.LinkInfo;
import com.cuv.domain.linklisting.dto.LinkListingListDto;
import com.cuv.domain.saleinquiry.dto.SaleInquirySaveDto;
import com.cuv.domain.salevehicle.SaleVehicleRepository;
import com.cuv.domain.salevehicle.entity.SaleVehicle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class LinkListingService {

    private final WebClient webClient;
    private final LinkInfoRepository linkInfoRepository;
    private final LinkCodeRepository linkCodeRepository;
    private final SaleVehicleRepository saleVehicleRepository;
    private final LinkListingRepository linkListingRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
//    String baseUrl = "https://usedcardev.carisyou.net/carinfo/ctrm/S1/2"; // 개발
    String baseUrl = "http://api.cartongryung.co.kr/carisyou/S1/2"; // 운영

    @Autowired
    public LinkListingService(LinkInfoRepository linkInfoRepository,
                              LinkCodeRepository linkCodeRepository, SaleVehicleRepository saleVehicleRepository, LinkListingRepository linkListingRepository) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .codecs(codecs -> codecs
                        .defaultCodecs()
                        .maxInMemorySize(1024 * 1024 * 100))
                .build();
        this.linkInfoRepository = linkInfoRepository;
        this.linkCodeRepository = linkCodeRepository;
        this.saleVehicleRepository = saleVehicleRepository;
        this.linkListingRepository = linkListingRepository;
    }

    /**
     * 내 차 팔기 > 차량 정보 조회
     * 카이즈유 중고차 정보 조회 (s1)
     *
     * @param requestDto 차량 정보를 조회할 데이터를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public LinkInfoDetailDto getCarInfo(SaleInquirySaveDto requestDto) throws JsonProcessingException {
        Map<String, String> linkageMap = new LinkedHashMap<>();
        LinkInfoDetailDto linkInfo = new LinkInfoDetailDto();

        Optional<SaleVehicle> saleVehicle = saleVehicleRepository.findByCarPlateNumber(requestDto.getCarPlateNumber());

        if (saleVehicle.isPresent())
            throw new IllegalArgumentException("이미 판매 등록된 차량번호입니다.");

        linkageMap.put("VHRNO", requestDto.getCarPlateNumber());
        linkageMap.put("OWNER_NM", requestDto.getOwnerName());

        // POST 요청 수행
        String strCarInfoJson = webClient.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(linkageMap))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (strCarInfoJson != null) {
            strCarInfoJson = strCarInfoJson.replace("\ufeff", "");

            // ObjectMapper로 Json 파싱
            JsonNode jsonNode = objectMapper.readTree(strCarInfoJson);

            if ("000".equals(jsonNode.get("INFO").get("PROCESS_IMPRTY_RESN_CODE").asText())) {
                linkInfo.setVhrno(requestDto.getCarPlateNumber());
                linkInfo.setBrandNm(getAsText(jsonNode.get("INFO").get("CARINFO"), "BRAND_NM"));
                linkInfo.setCarClassNm(getAsText(jsonNode.get("INFO").get("CARINFO"), "CAR_CLASS_NM"));
                linkInfo.setFrstRegistDe(getAsText(jsonNode.get("INFO").get("CARINFO"), "FRST_REGIST_DE"));
                linkInfo.setTrvlDstnc(getAsLong(jsonNode.get("INFO").get("CARINFO"), "TRVL_DSTNC"));
                linkInfo.setTireSizeFront(getAsText(jsonNode.get("INFO").get("CARINFO").get("GRADE_LIST").get(0), "TIRE_SIZE_FRONT"));

                linkInfo.setOwnerName(requestDto.getOwnerName());
                linkInfo.setLinkInfoId(this.saveCarInfo(jsonNode, linkageMap));

            } else {
                throw new IllegalArgumentException("소유자명을 확인할 수 없습니다.\n\n다시 확인해주세요.");
            }
        }
        return linkInfo;
    }

    /**
     * 내 차 팔기 > 신청 완료
     * 등록한 차량 번호로 조회된 중고 차량 정보 저장
     *
     * @param jsonNode 중고차 정보를 담은 json
     * @param map      차량 번호를 담은 map
     * @author SungHa
     */
    @Transactional
    public Long saveCarInfo(JsonNode jsonNode, Map<String, String> map) throws JsonProcessingException {
        Optional<LinkInfo> linkInfoData = linkInfoRepository.findByVhrno(map.get("VHRNO"));

        LinkInfo linkInfo;
        if (linkInfoData.isEmpty()) {
            linkInfo = LinkInfo.builder()
                    .vhrno(map.get("VHRNO"))
                    .vin(getAsText(jsonNode.get("INFO").get("CARINFO"), "VIN"))
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
                    .tireSizeFront(getAsText(jsonNode.get("INFO").get("CARINFO").get("GRADE_LIST").get(0), "TIRE_SIZE_FRONT"))
                    .tireSizeBack(getAsText(jsonNode.get("INFO").get("CARINFO").get("GRADE_LIST").get(0), "TIRE_SIZE_BACK"))
                    .colorNm(getAsText(jsonNode.get("INFO").get("CARINFO"), "COLOR_NM"))
                    .fuel(getAsText(jsonNode.get("INFO").get("CARINFO"), "FUEL"))
                    .enginesize(getAsText(jsonNode.get("INFO").get("CARINFO"), "ENGINESIZE"))
                    .istdTrans(getAsText(jsonNode.get("INFO").get("CARINFO"), "ISTD_TRANS"))
                    .extShape(getAsText(jsonNode.get("INFO").get("CARINFO"), "EXT_SHAPE"))
                    .person(getAsText(jsonNode.get("INFO").get("CARINFO"), "PERSON"))
                    .engineForm(getAsText(jsonNode.get("INFO").get("CARINFO"), "ENGINE_FORM"))
                    .prye(getAsText(jsonNode.get("INFO").get("CARINFO"), "PRYE"))
                    .insptValidPdDe(getAsText(jsonNode.get("INFO").get("CARINFO"), "INSPT_VALID_PD_DE"))
                    .trvlDstnc(getAsLong(jsonNode.get("INFO").get("CARINFO"), "TRVL_DSTNC"))
                    .frstRegistDe(getAsText(jsonNode.get("INFO").get("CARINFO"), "FRST_REGIST_DE"))
                    .carInfoJson(objectMapper.writeValueAsString(jsonNode))
                    .build();

            linkInfoRepository.save(linkInfo);

            // 새로운 데이터일 떄 코드 추가
            adminManagementLinkCodeSave(linkInfo);

            return linkInfo.getId();
        } else {
            return linkInfoData.get().getId();
        }
    }

    /**
     * 내 차 팔기 > 차량 정보 조회 - json null 처리
     *
     * @param node 데이터가 담겨 있는 json
     * @param fieldName 데이터가 담겨 있는 key
     * @author SungHa
     */
    private String getAsText(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return (field != null && !field.isNull()) ? field.asText() : null;
    }

    private Long getAsLong(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return (field != null && !field.isNull()) ? field.asLong() : null;
    }

    /**
     * 내 차 팔기 > 차량 정보 조회 - 검색용 코드 저장
     * 새로운 넘버링일 떄 코드 테이블에 새로운 데이터 추가
     *
     * @param linkInfo 중고 차량 데이터
     * @author SungHa
     */
    @Transactional
    public void adminManagementLinkCodeSave(LinkInfo linkInfo) {
        // 중고 차량 정보 조회 시
        Long makerId = getOrCreateLinkCode(linkInfo.getBrandNbr(), linkInfo.getBrandNm(), 0L, 0);
        Long modelId = getOrCreateLinkCode(linkInfo.getRepCarClassNbr(), linkInfo.getRepCarClassNm(), makerId, 1);
        Long detailModelId = getOrCreateLinkCode(linkInfo.getCarClassNbr(), linkInfo.getCarClassNm(), modelId, 2);
        getOrCreateLinkCode(linkInfo.getCarGradeNbr(), linkInfo.getCarGradeNm(), detailModelId, 3);
    }

    /**
     * 내 차 팔기 > 차량 정보 조회 - 검색용 코드 저장
     * 새로운 넘버링일 떄 코드 테이블에 새로운 데이터 추가
     *
     * @param id 연동 코드 일련번호
     * @param name 연동 코드 이름
     * @param parentId 연동 코드 상위 일련번호
     * @param depth 연동 코드 차수
     * @author SungHa
     */
    private Long getOrCreateLinkCode(Long id, String name, Long parentId, int depth) {
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

    public JSONResponse<?> searchApiProductFuel() {
        List<LinkListingListDto> linkListingList = linkListingRepository.searchApiProductFuel();
        return new JSONResponse<>(200, "SUCCESS", linkListingList);
    }

    public JSONResponse<?> searchApiProductSido() {
        try {
            List<LinkListingListDto> linkListingList = linkListingRepository.searchApiProductSido();
            return new JSONResponse<>(200, "SUCCESS", linkListingList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new JSONResponse<>(500, "FAIL");
        }
    }

    public JSONResponse<?> searchApiProductIstdTrans() {
        try {
            List<LinkListingListDto> linkListingList = linkListingRepository.searchApiProductIstdTrans();
            return new JSONResponse<>(200, "SUCCESS", linkListingList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new JSONResponse<>(500, "FAIL");
        }
    }

    public List<LinkListingListDto> searchMobileFuelList() {
        return linkListingRepository.searchMobileFuelList();
    }

    public List<LinkListingListDto> searchMobileIstdTrans() {
        return linkListingRepository.searchMobileIstdTrans();
    }

    public List<LinkListingListDto> searchMobileSidoList() {
        return linkListingRepository.searchMobileSidoList();
    }
}
