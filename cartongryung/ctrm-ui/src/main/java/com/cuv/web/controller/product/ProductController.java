package com.cuv.web.controller.product;

import com.cuv.common.JSONResponse;
import com.cuv.domain.linkcode.LinkCodeService;
import com.cuv.domain.linkcode.dto.LinkCodeListDto;
import com.cuv.domain.linkhistory.LinkHistoryService;
import com.cuv.domain.linkinfo.LinkInfoService;
import com.cuv.domain.linklisting.LinkListingService;
import com.cuv.domain.linklisting.dto.LinkListingListDto;
import com.cuv.domain.linkoptioninfo.LinkOptionInfoService;
import com.cuv.domain.popularkeyword.PopularKeywordService;
import com.cuv.domain.popularkeyword.dto.PopularKeywordListDto;
import com.cuv.domain.product.ProductService;
import com.cuv.domain.product.dto.*;
import com.cuv.domain.product.enumset.CarSize;
import com.cuv.domain.product.enumset.ExteriorShape;
import com.cuv.domain.productviewshistory.ProductViewsHistoryService;
import com.cuv.domain.productviewshistory.dto.ProductViewsHistoryRecentCarListDto;
import com.cuv.util.KISA_SEED_CBC;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Tag(
        name = "사용자 - 상품",
        description = "사용자 - 상품"
)
@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final LinkCodeService linkCodeService;
    private final LinkListingService linkListingService;
    private final LinkInfoService linkInfoService;
    private final LinkOptionInfoService linkOptionInfoService;
    private final LinkHistoryService linkHistoryService;
    private final PopularKeywordService popularKeywordService;
    private final ProductViewsHistoryService productViewsHistoryService;

    @Value("${carhistory.joinCode}")
    private String joinCode;
    @Value("${carhistory.sType}")
    private String sType;
    @Value("${carhistory.memberID}")
    private String memberID;
    @Value("${carhistory.carNumType}")
    private String carNumType;
    @Value("${carhistory.seed.key}")
    private String seedKey;
    @Value("${carhistory.rType}")
    private String rType;



    /**
     * 내 차 구입 페이지 (검색)
     * @return
     */
    @Operation(
            summary = "사용자 - 내 차 구입",
            description = "내 차 구입 페이지 (검색)"
    )
    @GetMapping("/search/product")
    public String searchProduct(Model model) {
        List<Integer> priceRanges = Arrays.asList(
                0, 1_000_000, 2_000_000, 3_000_000, 4_000_000, 5_000_000, 6_000_000, 7_000_000, 8_000_000, 9_000_000,
                10_000_000, 11_000_000, 12_000_000, 13_000_000, 14_000_000, 15_000_000, 20_000_000, 25_000_000,
                30_000_000, 35_000_000, 40_000_000, 45_000_000, 50_000_000, 60_000_000, 70_000_000, 80_000_000,
                90_000_000, 100_000_000, 200_000_000, 300_000_000, 400_000_000, 500_000_000, 600_000_000, 700_000_000,
                800_000_000, 900_000_000, 1_000_000_000, 1_000_000_000);
        List<Integer> kmRanges = Arrays.asList(
                0, 2_000, 5_000, 10_000, 20_000, 30_000, 40_000, 50_000, 60_000, 70_000, 80_000, 90_000, 100_000,
                110_000, 120_000, 130_000, 140_000, 150_000, 160_000, 170_000, 180_000, 190_000, 200_000, 250_000,
                300_000, 300_000);
        List<Integer> yearRanges = IntStream.rangeClosed(1980, LocalDate.now().getYear())
                .boxed()
                .toList();

        Map<String, String> engNames = new HashMap<>();
        for (CarSize carSize : CarSize.values()) {
            if (carSize.getCode() == null) continue;
            engNames.put(carSize.getCode(), carSize.getName());
        }
        for (ExteriorShape exteriorShape : ExteriorShape.values()) {
            if (exteriorShape.getCode() == null) continue;
            engNames.put(exteriorShape.getCode(), exteriorShape.getName());
        }

        model.addAttribute("priceRanges", priceRanges);
        model.addAttribute("kmRanges", kmRanges);
        model.addAttribute("yearRanges", yearRanges);
        model.addAttribute("engNames", engNames);

        return "search/search_result";
    }

    /**
     * 차량 상세
     * @param id 상품 아이디
     * @return
     */
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        ProductDetailDto productDetail = productService.searchProductDetail(id, userDetails);

        List<ProductRecommendListDto> recommendList = productService.searchProductRecommendNoPageList();
        List<SpecGroupDto> linkOptionList = productService.searchProductLinkOptionList(productDetail.getProductDetailGradeNumber());

        // 비로그인 상태에서 최근 본 상품 추가
        if (userDetails == null) {
            productViewsHistoryService.addRecentCarToSession(id);
        } else {
            // 로그인 상태에서 최근 본 차량 추가
            productViewsHistoryService.saveProductViewsHistory(id, userDetails);
        }
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        byte bszUser_key[] = {
        };
        byte bszIV[] = {
                (byte)0x026, (byte)0x08d, (byte)0x066, (byte)0x0a7,
                (byte)0x035, (byte)0x0a8, (byte)0x01a, (byte)0x081,
                (byte)0x06f, (byte)0x0ba, (byte)0x0d9, (byte)0x0fa,
                (byte)0x036, (byte)0x016, (byte)0x025, (byte)0x001
        };

        String stdDate = today.format(formatter);
        String carnum = productDetail.getCarPlateNumber();
        /** 53라9319
         08오5060
         11고4424
         서울85바3080
         245머6418 */
//        String carnum = "서울85바3080";
        String enc_sType = null, enc_carnum = null, enc_memberID = null;
        // 받은 암호화 키
        bszUser_key = KISA_SEED_CBC.hexToByteArray(seedKey);

        try {
            enc_sType = KISA_SEED_CBC.SeedEncryptPlaintext(sType, bszUser_key, bszIV);
            enc_carnum = KISA_SEED_CBC.SeedEncryptPlaintext(carnum, bszUser_key, bszIV);
            enc_memberID = KISA_SEED_CBC.SeedEncryptPlaintext(memberID, bszUser_key, bszIV);
        } catch(Exception e) {
            e.printStackTrace();
        }
        productService.addHits(id);
        String regYear = productDetail.getCarRegYear();
        int carRegYear = Integer.parseInt(regYear); // String을 int로 변환
        int currentYear = Year.now().getValue(); // 현재 년도 구하기
        int yearDifference = currentYear - carRegYear; // 현재 년도에서 carRegYear 빼기

        model.addAttribute("joinCode", joinCode);
        model.addAttribute("sType", enc_sType);
        model.addAttribute("memberID", enc_memberID);
        model.addAttribute("carnum", enc_carnum);
        model.addAttribute("carNumType", carNumType);
        model.addAttribute("stdDate", stdDate);
        model.addAttribute("rType", rType);

        model.addAttribute("productDetail", productDetail);
        model.addAttribute("recommendList", recommendList);
        model.addAttribute("linkOptionList", linkOptionList);
        model.addAttribute("yearDifference", yearDifference);

        return "vehicle/vehicle_view";
    }


    /**
     * 검색 조건에 관련된 결과
     * @param queryParams 검색 정보
     * @return
     */
    @Operation(
            summary = "사용자 - 내 차 구입",
            description = "검색 조건에 관련된 결과"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

    @GetMapping("/api/search/product")
    public ResponseEntity<JSONResponse<?>> apiSearchProduct(
            @RequestParam Map<String, String> queryParams) {
        JSONResponse<?> response;
        try {
            response = productService.searchApiProduct(queryParams);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("errorMessage Search Result Controller : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new JSONResponse<>(500, "FALSE"));
        }
    }



    /**
     * 연료 태그 클릭시 연료 데이터 가지고 오기
     * @return
     */
    @Operation(
            summary = "사용자 - 내 차 구입",
            description = "연료 태그 클릭시 연료 데이터 가지고 오기"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

    @PostMapping("/api/search/product/fuel")
    @ResponseBody
    public JSONResponse<?> apiSearchProductFuel() {
        JSONResponse<?> response;
        try {
            response = linkListingService.searchApiProductFuel();
        } catch (Exception e) {
            return new JSONResponse<>(500, "FAIL");
        }
        return response;
    }

    /**
     * 변속 태그 클릭시 변속기 데이터 가지고 오기
     * @return
     */
    @Operation(
            summary = "사용자 - 내 차 구입",
            description = "변속 태그 클릭시 변속기 데이터 가지고 오기"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

    @PostMapping("/api/search/product/istd_trans")
    @ResponseBody
    public JSONResponse<?> apiSearchProductIstdTrans() {
        JSONResponse<?> response;
        try {
            response = linkListingService.searchApiProductIstdTrans();
        } catch (Exception e) {
            return new JSONResponse<>(500, "FAIL");
        }
        return response;
    }

    /**
     * 차량 옵션 태그 클릭시 옵션 데이터 가지고 오기
     * @return
     */
    @Operation(
            summary = "사용자 - 내 차 구입",
            description = "차량 옵션 태그 클릭시 옵션 데이터 가지고 오기"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

    @PostMapping("/api/search/product/option")
    @ResponseBody
    public JSONResponse<?> apiSearchProductOption() {
        JSONResponse<?> response;
        try {
            response = linkOptionInfoService.searchApiProductOption();
        } catch (Exception e) {
            return new JSONResponse<>(500, "FAIL");
        }
        return response;
    }

    /**
     * 차량 지역 태그 클릭시 지역 데이터 가지고 오기
     * @return
     */
    @Operation(
            summary = "사용자 - 내 차 구입",
            description = "차량 지역 태그 클릭시 지역 데이터 가지고 오기"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

    @PostMapping("/api/search/product/sido")
    @ResponseBody
    public JSONResponse<?> apiSearchProductSido() {
        JSONResponse<?> response;
        try {
            response = linkListingService.searchApiProductSido();
        } catch (Exception e) {
            return new JSONResponse<>(500, "FAIL");
        }
        return response;
    }

    /**
     * 옵션 태그 옵션 더보기 클릭시 데이터 가지고 오기
     * @return
     */
    @Operation(
            summary = "사용자 - 내 차 구입",
            description = "옵션 태그 옵션 더보기 클릭시 데이터 가지고 오기"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

    @PostMapping("/api/search/product/more/option")
    @ResponseBody
    public JSONResponse<?> apiSearchProductMoreOption() {
        JSONResponse<?> response;
        try {
            response = linkOptionInfoService.searchApiProductMoreOption();
        } catch (Exception e) {
            return new JSONResponse<>(500, "FAIL");
        }
        return response;
    }

    /**
     * 색상 태그 더보기 클릭
     * @return
     */
    @Operation(
            summary = "사용자 - 내 차 구입",
            description = "색상 태그 더보기 클릭"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

    @PostMapping("/api/search/product/more/color")
    @ResponseBody
    public JSONResponse<?> apiSearchProductMoreColor() {
        JSONResponse<?> response;
        try {
            response = linkInfoService.searchApiProductMoreColor();
        } catch (Exception e) {
            return new JSONResponse<>(500, "FAIL");
        }
        return response;
    }

    /**
     * 검색 페이지 -> 색상 태그 클릭
     * @return
     */
    @Operation(
            summary = "사용자 - 내 차 구입",
            description = "검색 페이지 -> 색상 태그 클릭"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

    @PostMapping("/api/search/product/color")
    @ResponseBody
    public JSONResponse<?> apiSearchProductColor() {
        JSONResponse<?> response;
        try {
            response = linkInfoService.searchApiProductColor();
        } catch (Exception e) {
            return new JSONResponse<>(500, "FAIL");
        }
        return response;
    }

    /**
     * 차량 상세 -> 보험 이력 조회ㅣ API
     * @param params 보험 이력 조회를 위한 dto 정보
     * @param authentication 로그인 정보
     * @return
     */
    @Operation(
            summary = "사용자 - 차량 상세",
            description = "차량 상세 -> 보험 이력 조회ㅣ API"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

    @PostMapping("/api/search/carhistory")
    @ResponseBody
    public JSONResponse<?> searchCarHistory(ProductCarHistoryDto params,
                                            Authentication authentication) {
        JSONResponse<?> response;
        try {
            response = linkHistoryService.searchApiCarHistory(params, authentication);
        } catch (Exception e) {
            return new JSONResponse<>(500, "FALSE");
        }
        return response;
    }

    /**
     * 모바일 필터 페이지
     * @param condition 모바일 검색 조건 dto
     * @return
     */
    @Operation(
            summary = "사용자 - 모바일 필터",
            description = "모바일 필터 페이지"
    )
    @GetMapping("/search/product/mobile/filter")
    public String searchProductMobileFilter(ProductTotalSearchDto condition, Model model) {

        int startYear = 1980;
        int currentYear = LocalDate.now().getYear();

        List<Integer> yearList = new ArrayList<>();

        for (int year = startYear; year <= currentYear; year++) {
            yearList.add(year);
        }

        // 연료
        List<LinkListingListDto> carFuel = linkListingService.searchMobileFuelList();
        // 변속기
        List<LinkListingListDto> carIstdTrans = linkListingService.searchMobileIstdTrans();
        // 지역
        //TODO 추후 주석 제거
//        List<LinkListingListDto> carSido = linkListingService.searchMobileSidoList();

        List<LinkListingListDto> carColors = linkListingService.searchMobileCarColorList();

        model.addAttribute("carFuel", carFuel);
//        model.addAttribute("carSido", carSido);
        model.addAttribute("carIstdTrans", carIstdTrans);
        model.addAttribute("condition", condition);
        model.addAttribute("carColors", carColors);
        model.addAttribute("yearList", yearList);
        return "search/search_filter";
    }

    /**
     * 모바일 옵션 정보
     * @return
     */
    @Operation(
            summary = "사용자 - 모바일 필터",
            description = "모바일 옵션 정보"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

    @PostMapping("/api/search/mobile/option")
    @ResponseBody
    public JSONResponse<?> apiSearchMobileOption() {
        JSONResponse<?> response;
         try {
             response = linkOptionInfoService.searchMobileOption();
         } catch (Exception e) {
                return new JSONResponse<>(500, "FAIL");
         }
        return response;
    }

    /**
     * 모바일 제조사/모델 검색 페이지
     * @param condition 모바일 제조사/모델 검색 조건
     * @return
     */
    @Operation(
            summary = "사용자 - 모바일 제조사/모델 검색 페이지",
            description = "모바일 제조사/모델 검색 페이지"
    )
    @GetMapping("/search/product/mobile/filter/maker")
    public String searchProductMobileFilterMaker(ProductTotalSearchDto condition, Model model) {
        List<LinkCodeListDto> firstCategoryList = linkCodeService.searchFirstCategoryList();
        model.addAttribute("firstCategoryList", firstCategoryList);
        return "search/search_maker";
    }


    /**
     * 제조사 카테고리 조회
     * @param map 카테고리 정보
     * @return
     */
    @Operation(
            summary = "사용자 - 모바일 제조사/모델 검색 페이지",
            description = "제조사 카테고리 조회"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })
    @PostMapping("/api/search/category")
    @ResponseBody
    public JSONResponse<?> searchCategory(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = productService.searchCategory(map);
        } catch (Exception e) {
            log.error("errorMessage Search Result Controller : " + e.getMessage());
            return new JSONResponse<>(500, "FALSE");
        }
        return response;
    }

    /**
     * 차종 검색 페이지
     * @param condition 차종 검색 조건 dto
     * @return
     */
    @Operation(
            summary = "사용자 - 모바일 차종 검색 페이지",
            description = "차종 검색 페이지"
    )
    @GetMapping("/search/product/mobile/filter/kind")
    public String searchProductMobileFilterKind(ProductTotalSearchDto condition, Model model) {
        List<ExteriorShape> shapes = Arrays.stream(ExteriorShape.values())
                .filter(shape -> shape != ExteriorShape.NONE)
                .collect(Collectors.toList());

        List<CarSize> sizes = Arrays.stream(CarSize.values())
                        .filter(size -> size != CarSize.NONE)
                                .collect(Collectors.toList());

        model.addAttribute("exteriorShapes", shapes);
        model.addAttribute("size", sizes);
        return "search/search_type";
    }

    /**
     * 모바일 검색 페이지
     * @return
     */
    @Operation(
            summary = "사용자 - 모바일 검색 페이지",
            description = "모바일 검색 페이지"
    )
    @GetMapping("/search/mobile")
    public String searchMobile(Model model) {
        // 인기 검색어
        List<PopularKeywordListDto> popularKeywordLists = popularKeywordService.searchAllPopularKeyword();
        // 최근 본 차량
        List<ProductViewsHistoryRecentCarListDto> recentList = productViewsHistoryService.searchProductViewsHistoryRecentCarList();
        model.addAttribute("popularKeywordLists", popularKeywordLists);
        model.addAttribute("recentList", recentList);
        return "search/search";
    }

    /**
     * 모바일 차종 검색
     * @param map 모바일 차종 검색을 위한 정보
     * @return
     */
    @Operation(
            summary = "사용자 - 모바일 차종 검색",
            description = "모바일 차종 검색"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })
    @PostMapping("/api/search/mobile/kind")
    @ResponseBody
    public JSONResponse<?> searchMobileKind(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = productService.searchMobileKind(map);
        } catch (Exception e) {
            log.error("errorMessage Search Result Controller : " + e.getMessage());
            return new JSONResponse<>(500, "FALSE");
        }
        return response;
    }


    /**
     * 최근 본 차량 삭제
     *
     * @param map 삭제를 위한 정보
     * @param session 비로그인 회원을 위한 session
     * @return
     */
    @Operation(
            summary = "사용자 - 모바일 검색",
            description = "최근 본 차량 삭제"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })
    @PostMapping("/api/search/mobile/delete")
    @ResponseBody
    public JSONResponse<?> apiSearchMobileDelete(@RequestBody Map<String, Object> map , HttpSession session) {
        JSONResponse<?> response;
        try {
            String loginYn = (String) map.get("loginYn");
            Long id = Long.valueOf(map.get("id").toString());
            if ("Y".equals(loginYn)){
                response = productService.deleteRecentProduct(map);
            } else {
                // 세션에서 recentCars 리스트를 가져옴
                List<String> recentCars = (List<String>) session.getAttribute("recentCars");

                if (recentCars != null) {
                    // 리스트에서 특정 id를 제거
                    recentCars.remove(id);
                    // 변경된 리스트를 세션에 다시 저장
                    session.setAttribute("recentCars", recentCars);
                }
                response = new JSONResponse<>(200, "OK");
            }

        } catch (Exception e) {
            return new JSONResponse<>(500, "FALSE");
        }

        return response;
    }

    /**
     * 조건에 맞는 하위 카테고리 정보
     * @param map 카테고리 정보
     * @return
     */
    @Operation(
            summary = "사용자 - 모바일 검색",
            description = "조건에 맞는 하위 카테고리 정보"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })
    @PostMapping("/api/search/category/children")
    @ResponseBody
    public JSONResponse<?> searchApiCategoryChildren(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = linkCodeService.searchApiCategoryChildren(map);
        } catch (Exception  e) {
            return new JSONResponse<>(500, "FALSE");
        }
        return response;
    }
}
