package com.cuv.admin.web.controller.management;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.linkcode.LinkCodeService;
import com.cuv.admin.domain.linkcode.dto.LinkCodeListDto;
import com.cuv.admin.domain.linkcode.dto.LinkCodeSearchDto;
import com.cuv.admin.domain.linklisting.LinkListingService;
import com.cuv.admin.domain.linklisting.dto.LinkListingListDto;
import com.cuv.admin.domain.linklisting.dto.LinkListingSearchDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

/**
 * 관리자 | 차량 관리 | 매물 연동 관리 Controller
 */
@Tag(
        name = "관리자 -> 차량 관리 -> 매물 연동 관리",
        description = "관리자 -> 차량 관리 -> 매물 연동 관리"
)
@Controller
@RequiredArgsConstructor
public class LinkageController {

    private final LinkListingService linkListingService;
    private final LinkCodeService linkCodeService;

    /**
     * 관리자 | 차량관리 > 매물 연동 관리 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @param codeCondition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 매물 연동 관리 > 목록",
            description = "관리자 매물 연동 관리 목록"
    )
    @GetMapping("/admin/management/linkage")
    public String adminManagementLinkageList(LinkListingSearchDto condition, LinkCodeSearchDto codeCondition, Model model) {
        int setPage = 1;
        int setSize = 1000;

        String page = condition.getPage();
        String size = condition.getSize();
        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);
        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());

        // 매물
        Page<LinkListingListDto> linkageLists = linkListingService.searchAllLinkage(condition, request);
        // 상사명
        Set<String> shopNames = linkageLists.stream()
                .map(LinkListingListDto::getShopName)
                .collect(Collectors.toSet());

        Map<String, String> shopNameMap = shopNames.stream()
                .collect(Collectors.toMap(shopName -> shopName, shopName -> shopName));
        // 검색 조건 - 차량 매물
        List<LinkCodeListDto> linkCodeLists = linkCodeService.searchAllLinkCode(codeCondition);

        model.addAttribute("condition", condition);
        model.addAttribute("linkageLists", linkageLists);
        model.addAttribute("linkCodeLists", linkCodeLists);
        model.addAttribute("shopNameMap", shopNameMap);

        return "management/linkup_list";
    }

    /**
     * 관리자 | 차량관리 > 매물 연동 관리 > 매물 정보 가져오기 팝업
     *
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 매물 연동 관리 > 매물 정보 가져오기 팝업",
            description = "관리자 매물 연동 관리 매물 정보 가져오기 팝업"
    )
    @GetMapping("/admin/management/calendar")
    public String adminManagementCalendar() {
        return "popup/sales_import";
    }

    /**
     * 관리자 | 차량관리 > 매물 연동 관리 > 차량 등록
     *
     * @param idList 등록할 차량의 시퀀스
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 매물 연동 관리 > 차량 등록",
            description = "관리자 매물 연동 관리 차량 등록"
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
    @PostMapping("/admin/management/linkage/write")
    @ResponseBody
    public JSONResponse<?> adminManagementLinkageWriteProc(@RequestParam("id[]") List<Long> idList) {
        try {
            linkListingService.adminManagementLinkageWriteProc(idList);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS");
    }

    /**
     * 관리자 | 차량 관리 > 매물 연동 관리 > 상사명 변경
     *
     * @param shopNames 상사명 변경 값들
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 매물 연동 관리 > 상사명 변경",
            description = "관리자 매물 연동 관리 상사명 변경"
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
    @PostMapping("/admin/management/shop-names/update")
    @ResponseBody
    public JSONResponse<?> adminManagementShopNamesUpdate(@RequestParam Map<String, String> shopNames) {
        try {
            linkListingService.shopNamesUpdate(shopNames);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS");
    }
}
