package com.cuv.admin.web.controller.promotion;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.common.YN;
import com.cuv.admin.domain.exhibition.ExhibitionService;
import com.cuv.admin.domain.exhibition.dto.ExhibitionDetailDto;
import com.cuv.admin.domain.exhibition.dto.ExhibitionListDto;
import com.cuv.admin.domain.exhibition.dto.ExhibitionSaveDto;
import com.cuv.admin.domain.exhibition.dto.ExhibitionSearchDto;
import com.cuv.admin.domain.exhibition.enumset.ExhibitionType;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Tag(
        name = "관리자 -> 프로모션 -> 이벤트, 팝업, 상단 띠 배너, 배너 관리",
        description = "관리자 -> 프로모션 -> 이벤트, 팝업, 상단 띠 배너, 배너 관리"
)
@Controller
@RequiredArgsConstructor
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    private final ProductService productService;

    private final LinkCodeService linkCodeService;

    private final MemberAdminService memberAdminService;

    /**
     * 관리자 | 프로모션 > 전시 관리 > 작성 폼
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 프로모션 > 전시 관리 > 작성 폼",
            description = "관리자 전시 관리 작성 폼"
    )
    @GetMapping("/admin/promotion/exhibition/write")
    public String adminPromotionExhibitionWrite(ExhibitionSearchDto condition, Model model) {
        if (ExhibitionType.ofCode(condition.getType()) == ExhibitionType.NONE) condition.setType(ExhibitionType.EVENT.getCode());

        model.addAttribute("condition", condition);

        return "promotion/exhibition_write";
    }

    /**
     * 관리자 | 프로모션 > 전시 관리 > 등록
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 프로모션 > 전시 관리 > 등록",
            description = "관리자 전시 관리 등록"
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
    @PostMapping("/admin/promotion/exhibition/write")
    @ResponseBody
    public JSONResponse<?> adminPromotionExhibitionWriteProc(ExhibitionSaveDto requestDto) {
        try {
            exhibitionService.adminPromotionExhibitionWriteProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    /**
     * 관리자 | 프로모션 > 전시 관리 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 프로모션 > 전시 관리 > 목록",
            description = "관리자 전시 관리 목록"
    )
    @GetMapping("/admin/promotion/exhibition")
    public String adminPromotionExhibitionList(ExhibitionSearchDto condition, Model model) {
        int setPage = 1;
        int setSize = 50;

        String page = condition.getPage();
        String size = condition.getSize();
        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);
        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());

        if (ExhibitionType.ofCode(condition.getType()) == ExhibitionType.NONE) condition.setType(ExhibitionType.EVENT.getCode());

        Page<ExhibitionListDto> exhibitionLists = exhibitionService.searchAllExhibition(condition, request);

        model.addAttribute("condition", condition);
        model.addAttribute("exhibitionLists", exhibitionLists);

        return "promotion/exhibition_list";
    }

    /**
     * 관리자 | 프로모션 > 전시 관리 > 상세
     *
     * @param id 전시 시퀀스
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 프로모션 > 전시 관리 > 상세",
            description = "관리자 전시 관리 상세"
    )
    @GetMapping("/admin/promotion/exhibition/{id}")
    public String adminPromotionExhibitionDetail(@PathVariable("id") Long id, ExhibitionSearchDto condition, Model model) {
        if (ExhibitionType.ofCode(condition.getType()) == ExhibitionType.NONE) condition.setType(ExhibitionType.EVENT.getCode());

        ExhibitionDetailDto exhibition = exhibitionService.searchExhibitionById(id);

        model.addAttribute("condition", condition);
        model.addAttribute("exhibition", exhibition);

        return "promotion/exhibition_view";
    }

    /**
     * 관리자 | 프로모션 > 전시 관리 > 수정
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 프로모션 > 전시 관리 > 수정",
            description = "관리자 전시 관리 수정"
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
    @PostMapping("/admin/promotion/exhibition/edit")
    @ResponseBody
    public JSONResponse<?> adminPromotionExhibitionEditProc(ExhibitionSaveDto requestDto) {
        try {
            exhibitionService.adminPromotionExhibitionEditProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    /**
     * 관리자 | 프로모션 > 전시 관리 > 목록 > 삭제
     *
     * @param idList 삭제할 시퀀스 값의 배열
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 프로모션 > 전시 관리 > 목록 > 삭제",
            description = "관리자 전시 관리 목록 삭제"
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
    @DeleteMapping("/admin/promotion/exhibition/delete")
    @ResponseBody
    public JSONResponse<?> adminPromotionExhibitionListDeleteProc(@RequestParam("id[]") List<Long> idList) {
        try {
            exhibitionService.adminPromotionExhibitionListDeleteProc(idList);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", idList);
    }

    /**
     * 관리자 | 프로모션 > 전시 관리 > 상세 > 삭제
     *
     * @param id 삭제할 시퀀스
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 프로모션 > 전시 관리 > 상세 > 삭제",
            description = "관리자 전시 관리 상세 삭제"
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
    @DeleteMapping("/admin/promotion/exhibition/{id}")
    @ResponseBody
    public JSONResponse<?> adminPromotionExhibitionDeleteProc(@PathVariable("id") Long id) {
        try {
            exhibitionService.adminPromotionExhibitionDeleteProc(id);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", id);
    }

    /**
     * 관리자 | 프로모션 > 메인 상품 진열 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @param codeCondition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 프로모션 > 메인 상품 진열 > 목록",
            description = "관리자 메인 상품 진열 목록"
    )
    @GetMapping("/admin/promotion/recommend")
    public String adminPromotionRecommend(ProductSearchDto condition, LinkCodeSearchDto codeCondition, Model model) {
        if (condition.getType() == null) {
            condition.setType("car");
        }

        condition.setStatus(Collections.singletonList(PostStatus.POST.getCode()));
        condition.setRecommendYn(YN.Y.getYn());
        List<ProductListDto> recommendCarLists = productService.searchAllProduct(condition);

        // 검색 조건 - 차량명
        List<LinkCodeListDto> linkCodeLists = linkCodeService.searchAllLinkCode(codeCondition);

        // 검색 조건 - 딜러
        List<MemberAdminListDto> memberDealerLists = memberAdminService.searchAllMemberDealer(MemberRole.DEALER.getRole());

        model.addAttribute("condition", condition);
        model.addAttribute("recommendCarLists", recommendCarLists);
        model.addAttribute("linkCodeLists", linkCodeLists);
        model.addAttribute("memberDealerLists", memberDealerLists);

        return "promotion/recommend_car_list";
    }

    /**
     * 관리자 | 프로모션 > 메인 상품 진열 > 추천차량 팝업
     *
     * @param condition 검색 조건을 담은 DTO
     * @param codeCondition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 프로모션 > 메인 상품 진열 > 추천차량 팝업",
            description = "관리자 메인 상품 진열 추천차량 팝업"
    )
    @GetMapping("/admin/promotion/recommend/popup")
    public String adminPromotionRecommendPopup(ProductSearchDto condition, LinkCodeSearchDto codeCondition, Model model) {
        if (!"car".equals(condition.getType())) condition.setType("value");
        // 판매 차량 - 판매 노출: 게시, 상사명: 디에스오토인 차량
        condition.setStatus(Collections.singletonList(PostStatus.POST.getCode()));
        condition.setShopName("디에스 오토");
        condition.setRecommendYn(YN.N.getYn());
        List<ProductListDto> recommendCarLists = productService.searchAllProduct(condition);

        // 검색 조건 - 차량명
        List<LinkCodeListDto> linkCodeLists = linkCodeService.searchAllLinkCode(codeCondition);

        // 검색 조건 - 딜러
        List<MemberAdminListDto> memberDealerLists = memberAdminService.searchAllMemberDealer(MemberRole.DEALER.getRole());

        model.addAttribute("condition", condition);
        model.addAttribute("recommendCarLists", recommendCarLists);
        model.addAttribute("linkCodeLists", linkCodeLists);
        model.addAttribute("memberDealerLists", memberDealerLists);

        return "popup/recommend_list";
    }

    /**
     * 관리자 | 프로모션 > 메인 상품 진열 > 추천차량 팝업 > 저장
     * 관리자 | 프로모션 > 메인 상품 진열 > 목록 > 삭제
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 프로모션 > 메인 상품 진열 > 추천차량 팝업 > 저장, 관리자 | 프로모션 > 메인 상품 진열 > 목록 > 삭제",
            description = "관리자 메인 상품 진열 데이터 관리"
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
    @PostMapping("/admin/promotion/recommend/save")
    @ResponseBody
    public JSONResponse<?> adminPromotionRecommendSave(ProductSaveDto requestDto) {
        try {
            productService.adminPromotionRecommendSave(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

}
