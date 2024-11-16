package com.cuv.admin.web.controller.management;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.linkcode.LinkCodeService;
import com.cuv.admin.domain.linkcode.dto.LinkCodeListDto;
import com.cuv.admin.domain.linkcode.dto.LinkCodeSearchDto;
import com.cuv.admin.domain.member.enumset.MemberRole;
import com.cuv.admin.domain.memberadmin.MemberAdminService;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminListDto;
import com.cuv.admin.domain.product.ProductService;
import com.cuv.admin.domain.product.dto.ProductListDto;
import com.cuv.admin.domain.product.dto.ProductSaveDto;
import com.cuv.admin.domain.product.dto.ProductSearchDto;
import com.cuv.admin.domain.product.enumset.PostStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

/**
 * 관리자 | 차량관리 > 판매 차량 Controller
 */
@Tag(
        name = "관리자 -> 차량 관리 -> 판매 차량",
        description = "관리자 -> 차량 관리 -> 판매 차량"
)
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final LinkCodeService linkCodeService;

    private final MemberAdminService memberAdminService;

    /**
     * 관리자 | 차량 관리 > 판매 차량 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @param codeCondition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 판매 차량 > 목록",
            description = "관리자 판매 차량 목록"
    )
    @GetMapping("/admin/management/product")
    public String adminManagementProductList(ProductSearchDto condition, LinkCodeSearchDto codeCondition, Model model) {
        int setPage = 1;
        int setSize = 1000;

        String page = condition.getPage();
        String size = condition.getSize();
        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);
        PageRequest request = PageRequest.of(setPage - 1, setSize);

        Page<ProductListDto> productLists = productService.searchAllProduct(condition, request);

        // 검색 조건 - 차량 매물
        List<LinkCodeListDto> linkCodeLists = linkCodeService.searchAllLinkCode(codeCondition);

        // 검색 조건 - 딜러
        List<MemberAdminListDto> memberDealerLists = memberAdminService.searchAllMemberDealer(MemberRole.DEALER.getRole());

        model.addAttribute("condition", condition);
        model.addAttribute("productLists", productLists);
        model.addAttribute("linkCodeLists", linkCodeLists);
        model.addAttribute("memberDealerLists", memberDealerLists);

        return "management/sale_vehicle_list";
    }

    /**
     * 관리자 | 차량 관리 > 판매 차량 > 목록 - 판매 노출 상태 변경: 게시, 판매완료
     * 관리자 | 내 차 구입 > 상세 > 판매 완료
     *
     * @param requestDto 상태값을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량 관리 > 판매 차량 > 목록 - 판매 노출 상태 변경: 게시, 판매완료, 관리자 | 내 차 구입 > 상세 > 판매 완료",
            description = "관리자 판매 차량 상태 변경"
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
    @PostMapping("/admin/management/product/change")
    @ResponseBody
    public JSONResponse<?> adminManagementProductChange(ProductSaveDto requestDto) {
        try {
            productService.adminManagementProductChange(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS");
    }

    /**
     * 관리자 | 차량 관리 > 판매 차량 > 게시 중지 > 팝업
     *
     * @param id 상품 시퀀스
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 판매 차량 > 게시 중지 > 팝업",
            description = "관리자 판매 차량 게시 중지 팝업"
    )
    @GetMapping("/admin/management/product/popup/{id}")
    public String adminManagementProductPopup(@PathVariable("id") Long id, Model model) {
        String reason = productService.findPostStopReasonById(id);

        model.addAttribute("productId", id);
        model.addAttribute("reason", reason);

        return "popup/stop_reason_write";
    }

    /**
     * 관리자 | 차량 관리 > 판매 차량 > 게시 중지 > 팝업 > 저장
     *
     * @param requestDto 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 판매 차량 > 게시 중지 > 팝업 > 저장",
            description = "관리자 판매 차량 게시 중지 저장"
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
    @PostMapping("/admin/management/product/reason/save")
    @ResponseBody
    public JSONResponse<?> adminManagementProductReasonSave(ProductSaveDto requestDto) {
        try {
            productService.adminManagementProductReasonSave(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS");
    }

    /**
     * 관리자 | 차량관리 > 추천 차량 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 추천 차량 > 목록",
            description = "관리자 추천 차량 목록"
    )
    @GetMapping("/admin/management/recommend")
    public String adminManagementRecommend(ProductSearchDto condition, Model model) {
        int setPage = 1;
        int setSize = 50;

        String page = condition.getPage();
        String size = condition.getSize();
        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);
        PageRequest request = PageRequest.of(setPage - 1, setSize);

        // 판매 차량 - 판매 노출: 게시, 상사명: 디에스오토인 차량
        condition.setStatus(Collections.singletonList(PostStatus.POST.getCode()));
        condition.setShopName("디에스 오토");
        condition.setType("product");
        Page<ProductListDto> recommendLists = productService.searchAllProduct(condition, request);

        // 검색 조건 - 딜러
        List<MemberAdminListDto> memberDealerLists = memberAdminService.searchAllMemberDealer(MemberRole.DEALER.getRole());

        model.addAttribute("condition", condition);
        model.addAttribute("recommendLists", recommendLists);
        model.addAttribute("memberDealerLists", memberDealerLists);

        return "management/recommend_list";
    }

    /**
     * 관리자 | 차량관리 > 판매 차량 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author Sangbin
     */
    @Operation(
            summary = "관리자 | 차량관리 > 판매 차량 > 엑셀",
            description = "관리자 판매 차량 엑셀"
    )
    @GetMapping("/admin/management/all/excel")
    public void adminSaleAllExcel(ProductSearchDto condition, HttpServletResponse response) throws IOException {
        Workbook wb = productService.searchAllPostingProductExcelLists(condition);

        String fileName = URLEncoder.encode("판매 차량.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }

    /**
     * 관리자 | 차량관리 > 판매 차량 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    @GetMapping("/admin/management/recommend/excel")
    public void adminRecommendProductListExcel(ProductSearchDto condition, HttpServletResponse response) throws IOException {
        Workbook wb = productService.searchRecommendProductExcelList(condition);

        String fileName = URLEncoder.encode("추천 차량.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }
}
