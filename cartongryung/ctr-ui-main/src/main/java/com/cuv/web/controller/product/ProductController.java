package com.cuv.web.controller.product;

import com.cuv.common.JSONResponse;
import com.cuv.domain.linkcode.LinkCodeService;
import com.cuv.domain.linkcode.dto.LinkCodeListDto;
import com.cuv.domain.linkhistory.LinkHistoryService;
import com.cuv.domain.linkinfo.LinkInfoService;
import com.cuv.domain.linklisting.LinkListingService;
import com.cuv.domain.linklisting.dto.LinkListingListDto;
import com.cuv.domain.linkoptioninfo.LinkOptionInfoService;
import com.cuv.domain.linkoptioninfo.dto.LinkOptionInfoListDto;
import com.cuv.domain.popularkeyword.PopularKeywordService;
import com.cuv.domain.popularkeyword.dto.PopularKeywordListDto;
import com.cuv.domain.product.ProductService;
import com.cuv.domain.product.dto.ProductCarHistoryDto;
import com.cuv.domain.product.dto.ProductListDto;
import com.cuv.domain.product.dto.ProductTotalSearchDto;
import com.cuv.domain.product.enumset.ExteriorShape;
import com.cuv.domain.product_copy.ProductCopyService;
import com.cuv.domain.product_copy.dto.ApiProductDto;
import com.cuv.domain.productviewshistory.ProductViewsHistoryService;
import com.cuv.domain.productviewshistory.dto.ProductViewsHistoryRecentCarListDto;
import com.cuv.domain.terms.TermsRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private  final ProductService productService;
    private final LinkCodeService linkCodeService;
    private final LinkListingService linkListingService;
    private final LinkInfoService linkInfoService;
    private final LinkOptionInfoService linkOptionInfoService;
    private final LinkHistoryService linkHistoryService;
    private final PopularKeywordService popularKeywordService;
    private final ProductViewsHistoryService productViewsHistoryService;

    @GetMapping("/search/product")
    public String searchProduct(ProductTotalSearchDto condition, Model model) {
        // 검색 리스트
        List<ProductListDto> productList = productService.searchProductTotalSearchList(condition);
        List<LinkCodeListDto> codeList = linkCodeService.searchAllLinkCodeList();
        List<LinkCodeListDto> firstCategoryList = linkCodeService.searchFirstCategoryList();

        int startYear = 1980;
        int currentYear = LocalDate.now().getYear();

        List<Integer> yearList = new ArrayList<>();

        for (int year = startYear; year <= currentYear; year++) {
            yearList.add(year);
        }

        model.addAttribute("productList", productList);
        model.addAttribute("condition", condition);
        model.addAttribute("codeList", codeList);
        model.addAttribute("firstCategoryList", firstCategoryList);
        model.addAttribute("yearList", yearList);
        return "search/search_result";
    }

    // post 로 보내는 검색
    @PostMapping("/api/search/product")
    @ResponseBody
    public JSONResponse<?> apiSearchProduct(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = productService.searchApiProduct(map);
        } catch (Exception e) {
            log.error("errorMessage Search Result Controller : " + e.getMessage());
            return new JSONResponse<>(500, "FALSE");
        }
        return response;
    }


    /**
     * 연료 태그 클릭시 연료 데이터 가지고 오기
     * @return
     */
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
     * 검색 페이지 -> 옵션 태그 클릭
     * @return
     */
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
     * 검색 페이지 -> 색상 태그 클릭
     * @return
     */
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
     * @param params
     * @param userDetails
     * @return
     */
    @PostMapping("/api/search/carhistory")
    @ResponseBody
    public JSONResponse<?> searchCarHistory(ProductCarHistoryDto params, @AuthenticationPrincipal UserDetails userDetails) {
        JSONResponse<?> response;
        try {
            response = linkHistoryService.searchApiCarHistory(params, userDetails);
        } catch (Exception e) {
            return new JSONResponse<>(500, "FALSE");
        }
        return response;
    }

    /**
     * 모바일 필터 페이지
     * @param condition
     * @param model
     * @return
     */
    @GetMapping("/search/product/mobile/filter")
    public String searchProductMobileFilter(ProductTotalSearchDto condition, Model model) {

        int startYear = 1980;
        int currentYear = LocalDate.now().getYear();

        List<Integer> yearList = new ArrayList<>();

        for (int year = startYear; year <= currentYear; year++) {
            yearList.add(year);
        }

        List<LinkListingListDto> carFuel = linkListingService.searchMobileFuelList();
        List<LinkListingListDto> carIstdTrans = linkListingService.searchMobileIstdTrans();
        List<LinkListingListDto> carSido = linkListingService.searchMobileSidoList();
        List<LinkOptionInfoListDto> carOption = linkOptionInfoService.searchMobileOptionList();
        Long totalCount = productService.countById();

        model.addAttribute("carFuel", carFuel);
        model.addAttribute("carOption", carOption);
        model.addAttribute("carSido", carSido);
        model.addAttribute("carIstdTrans", carIstdTrans);
        model.addAttribute("condition", condition);
        model.addAttribute("yearList", yearList);
        model.addAttribute("totalCount", totalCount);
        return "search/search_filter";
    }

    /**
     * 모바일 제조사/모델 검색 페이지
     * @param condition
     * @param model
     * @return
     */
    @GetMapping("/search/product/mobile/filter/maker")
    public String searchProductMobileFilterMaker(ProductTotalSearchDto condition, Model model) {
        List<LinkCodeListDto> firstCategoryList = linkCodeService.searchFirstCategoryList();

        model.addAttribute("firstCategoryList", firstCategoryList);
        return "search/search_maker";
    }

    /**
     * 모바일 매물 카운트
     * @param map
     * @return
     */
    @PostMapping("/api/search/mobile/count")
    @ResponseBody
    public JSONResponse<?> searchMobileCount(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = productService.searchMobileProductCount(map);
        } catch (Exception e) {
            log.error("errorMessage Search Result Controller : " + e.getMessage());
            return new JSONResponse<>(500, "FALSE");
        }
        return response;
    }

    /**
     * 제조사 카테고리 조회
     * @param map
     * @return
     */
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
     * @param condition
     * @param model
     * @return
     */
    @GetMapping("/search/product/mobile/filter/kind")
    public String searchProductMobileFilterKind(ProductTotalSearchDto condition, Model model) {
        List<ExteriorShape> shapes = Arrays.stream(ExteriorShape.values())
                .filter(shape -> shape != ExteriorShape.NONE)
                .collect(Collectors.toList());
        model.addAttribute("exteriorShapes", shapes);
        return "search/search_type";
    }

    /**
     * 모바일 검색 페이지
     * @param model
     * @return
     */
    @GetMapping("/search/mobile")
    public String searchMobile(Model model) {
        List<PopularKeywordListDto> popularKeywordLists = popularKeywordService.searchAllPopularKeyword();
        List<ProductViewsHistoryRecentCarListDto> recentList = productViewsHistoryService.searchProductViewsHistoryRecentCarList();
        model.addAttribute("popularKeywordLists", popularKeywordLists);
        model.addAttribute("recentList", recentList);
        return "search/search";
    }

    /**
     * 모바일 차종 검색
     * @param map
     * @return
     */
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
}
