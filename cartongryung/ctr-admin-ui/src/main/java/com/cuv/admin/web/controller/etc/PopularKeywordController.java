package com.cuv.admin.web.controller.etc;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.linkcode.LinkCodeService;
import com.cuv.admin.domain.linkcode.dto.LinkCodeListDto;
import com.cuv.admin.domain.linkcode.dto.LinkCodeSearchDto;
import com.cuv.admin.domain.popularkeyword.PopularKeywordService;
import com.cuv.admin.domain.popularkeyword.dto.PopularKeywordListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Tag(
        name = "관리자 -> 기타 -> 인기 검색어 관리",
        description = "관리자 -> 기타 -> 인기 검색어 관리"
)
@Controller
@RequiredArgsConstructor
public class PopularKeywordController {

    private final PopularKeywordService popularKeywordService;
    private final LinkCodeService linkCodeService;

    /**
     * 관리자 | 기타 > 인기 검색어 관리
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 기타 > 인기 검색어 관리",
            description = "관리자 인기 검색어 관리 목록"
    )
    @GetMapping("/admin/etc/keyword")
    public String adminEtcKeyword(LinkCodeSearchDto condition, Model model) {
        // 저장된 데이터
        List<PopularKeywordListDto> popularKeywordLists = popularKeywordService.searchAllPopularKeyword();

        // 카이즈유 등급 코드
        List<LinkCodeListDto> linkCodeLists = linkCodeService.searchAllLinkCode(condition);

        model.addAttribute("popularKeywordLists", popularKeywordLists);
        model.addAttribute("linkCodeLists", linkCodeLists);

        return "etc/keyword_list";
    }

    /**
     * 관리자 | 기타 > 인기 검색어 관리 > 등록
     *
     * @param data 저장할 데이터
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 기타 > 인기 검색어 관리 > 등록",
            description = "관리자 회원 관리 등록"
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
    @PostMapping("/admin/etc/keyword/write")
    @ResponseBody
    public JSONResponse<?> adminEtcKeywordWriteProc(@RequestBody List<Map<String , Object>> data) {
        try {
            popularKeywordService.adminEtcKeywordWriteProc(data);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", data);
    }
}
