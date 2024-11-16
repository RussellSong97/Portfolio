package com.cuv.web.controller.terms;

import com.cuv.common.JSONResponse;
import com.cuv.domain.terms.TermsService;
import com.cuv.domain.terms.enumset.TermsType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "사용자 - 푸터" ,
        description = "사용자 - 푸터"
)
@RestController
@RequiredArgsConstructor
public class TermsController {

    private final TermsService termsService;

    /**
     * 서브 페이지에서 API 현태로 이용약관 호출
     *
     * @return String
     * @author DONGGWAN KIM
     */
    @Operation(
            summary = "사용자 - 푸터",
            description = "서브 페이지에서 API 현태로 이용약관 호출"
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
    @GetMapping("/api/terms/service")
    public String service() {
        return termsService.searchAllTerms(TermsType.SERVICE);
    }


    /**
     * 서브 페이지에서 API 현태로 개인정보처리방침 호출
     *
     * @return String
     * @author DONGGWAN KIM
     */
    @Operation(
            summary = "사용자 - 푸터",
            description = "서브 페이지에서 API 현태로 개인정보처리방침 호출"
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
    @GetMapping("/api/terms/privacy")
    public String privacy() {
        return termsService.searchAllTerms(TermsType.PRIVACY);
    }
}
