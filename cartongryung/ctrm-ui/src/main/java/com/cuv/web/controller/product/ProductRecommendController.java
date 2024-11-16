package com.cuv.web.controller.product;

import com.cuv.common.JSONResponse;
import com.cuv.domain.product.ProductService;
import com.cuv.domain.product.dto.ProductDetailDto;
import com.cuv.domain.product.dto.ProductRecommendListDto;
import com.cuv.domain.product.dto.SpecGroupDto;
import com.cuv.domain.productviewshistory.ProductViewsHistoryService;
import com.cuv.util.KISA_SEED_CBC;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

@Tag(
        name = "사용자 - 추천 차량",
        description = "사용자 - 추천 차량"
)
@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductRecommendController {

    private final ProductService productService;
    private String page;
    private String size;

    /**
     * 사용자 > 추천 차량 리스트
     * @author 송다운
     * @param authentication 로그인 정보
     * @return
     */
    @Operation(
            summary = "사용자 - 추천 차량",
            description = "사용자 - 추천 차량 리스트"
    )
    @GetMapping("/product/recommend")
    public String productRecommend(Model model, Authentication authentication) {
        int setPage = 1;
        int setSize = 20;

        if(hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if(hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);

        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());
        Page<ProductRecommendListDto> recommendList = productService.searchProductRecommendList(request, authentication);

        model.addAttribute("recommendList", recommendList);

        return "vehicle/recommend_list";
    }

    /**
     * 추천 차량 > 스크롤 이벤트
     * @param map 페이지 변수가 담긴 map
     * @author SungHa
     */
    @Operation(
            summary = "사용자 - 추천 차량",
            description = "추천 차량 > 스크롤 이벤트"
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
    @PostMapping("/api/recommend/list")
    @ResponseBody
    public JSONResponse<?> apiRecommendList(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = productService.searchApiRecommendProduct(map);
        } catch (Exception e) {
            log.error("errorMessage Search Recommend Result Controller : " + e.getMessage());
            return new JSONResponse<>(500, "FALSE");
        }
        return response;
    }


    /**
     * 상품 상세 - 제원정보 (팝업)
     * @param map 상품 상세 제원정보 팝업 데이터
     * @return
     */
    @Operation(
            summary = "사용자 - 차량 상세",
            description = "상품 상세 - 제원정보 (팝업)"
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
    @PostMapping("/api/search/product/spec/list")
    @ResponseBody
    public JSONResponse<?> searchProductSpecList(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = productService.searchApiProductSpecList(map);
        } catch (Exception e) {
            return new JSONResponse<>(500, "FALSE");
        }
        return response;
    }

    /**
     * 차량상세 > 픽 카운트
     * @author 박성민
     */
    @Operation(
            summary = "사용자 - 차량 상세",
            description = "차량상세 > 픽 카운트"
    )
    @ResponseBody
    @GetMapping("/api/product/getPickCountOfProduct/{id}")
    public Long getPickCountOfProduct(@PathVariable Long id) {
        return productService.getProductIdOfPick(id);
    }
}
