package com.cuv.admin.web.controller.etc;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.terms.TermsService;
import com.cuv.admin.domain.terms.dto.TermsSaveDto;
import com.cuv.admin.domain.terms.enumset.TermsType;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Tag(
        name = "관리자 -> 기타 -> 서비스 약관 관리",
        description = "관리자 -> 기타 -> 서비스 약관 관리"
)
@Controller
@RequiredArgsConstructor
public class TermsController {

    private final TermsService termsService;

    /**
     * 관리자 | 기타 > 서비스 약관 관리 > 등록 폼 및 상세
     *
     * @param condition 글 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 기타 > 서비스 약관 관리 > 등록 폼 및 상세",
            description = "관리자 서비스 약관 관리 등록 폼 및 상세"
    )
    @GetMapping("/admin/etc/terms")
    public String adminEtcTermsWrite(TermsSaveDto condition, Model model) {
        if (TermsType.ofCode(condition.getType()) == TermsType.NONE) condition.setType(TermsType.SERVICE.getCode());

        TermsSaveDto terms = termsService.searchAllTerms(condition);
        if (terms == null) {
            TermsSaveDto termsDto = new TermsSaveDto();
            model.addAttribute("terms", termsDto);
        } else {
            model.addAttribute("terms", terms);
        }

        model.addAttribute("condition", condition);

        return "etc/terms_write";
    }

    /**
     * 관리자 | 기타 > 서비스 약관 관리 > 등록 및 수정
     *
     * @param requestDto 등록할 글 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 기타 > 서비스 약관 관리 > 등록 및 수정",
            description = "관리자 서비스 약관 관리 등록 폼 및 상세"
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
    @PostMapping("/admin/etc/terms/write")
    @ResponseBody
    public JSONResponse<?> adminEtcTermsWriteProc(TermsSaveDto requestDto) {
        try {
            termsService.adminEtcTermsWriteProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

}
