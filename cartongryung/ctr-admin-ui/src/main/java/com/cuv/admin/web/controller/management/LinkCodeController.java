package com.cuv.admin.web.controller.management;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.linkcode.LinkCodeService;
import com.cuv.admin.domain.linkcode.dto.LinkCodeDetailDto;
import com.cuv.admin.domain.linkcode.dto.LinkCodeListDto;
import com.cuv.admin.domain.linkcode.dto.LinkCodeSaveDto;
import com.cuv.admin.domain.linkcode.dto.LinkCodeSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 | 차량관리 > 코드 관리 Controller
 */
@Tag(
        name = "관리자 -> 차량 관리 -> 코드 관리",
        description = "관리자 -> 차량 관리 -> 코드 관리"
)
@Controller
@RequiredArgsConstructor
public class LinkCodeController {

    private final LinkCodeService linkCodeService;

    /**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 코드 관리 > 차량 등급 설정",
            description = "관리자 코드 관리 페이지"
    )
    @GetMapping("/admin/management/code/class")
    public String adminManagementCodeClass(LinkCodeSearchDto condition, Model model) {
        condition.setDepth(0);
        List<LinkCodeListDto> linkCodeLists = linkCodeService.searchAllLinkCode(condition);

        model.addAttribute("condition", condition);
        model.addAttribute("linkCodeLists", linkCodeLists);

        return "management/code_list";
    }

    /**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 등록 팝업
     *
     * @param depth 차수
     * @param id 코드 카테고리 시퀀스
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 등록 팝업",
            description = "관리자 코드 관리 등록 팝업"
    )
    @GetMapping("/admin/management/code/class/write/{depth}/{id}")
    public String adminManagementOptionCodeWrite(@PathVariable("depth") int depth, @PathVariable("id") int id,
                                                 LinkCodeSearchDto condition, Model model) {

        model.addAttribute("condition", condition);

        return "popup/code_write";
    }

    /**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 등록
     *
     * @param requestDto 등록할 코드 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 등록",
            description = "관리자 코드 관리 등록"
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
    @PostMapping("/admin/management/code/write")
    @ResponseBody
    public JSONResponse<?> adminManagementCodeWriteProc(LinkCodeSaveDto requestDto) {
        try {
            Long id = linkCodeService.adminManagementCodeWriteProc(requestDto);
            requestDto.setId(id);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    /**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 하위 목록 출력
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 하위 목록 출력",
            description = "관리자 코드 관리 페이지 데이터 클릭 시 하위 목록 출력"
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
    @PostMapping("/admin/management/code/class/list/")
    @ResponseBody
    public JSONResponse<?> adminManagementCodeClassList(LinkCodeSearchDto condition) {
        List<LinkCodeListDto> linkCodeLists;

        try {
            linkCodeLists = linkCodeService.searchAllLinkCode(condition);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", linkCodeLists);
    }

    /**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 수정 팝업
     *
     * @param depth 차수
     * @param id 코드 카테고리 시퀀스
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 수정 팝업",
            description = "관리자 코드 관리 페이지 수정 팝업"
    )
    @GetMapping("/admin/management/code/{depth}/{id}")
    public String adminManagementCodeDetail(@PathVariable("depth") int depth, @PathVariable("id") Long id, Model model){
        LinkCodeDetailDto linkCode = linkCodeService.searchLinkCodeById(id);

        model.addAttribute("linkCode", linkCode);

        return "popup/code_view";
    }

    /**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정, 옵션 설정 > 수정
     *
     * @param requestDto 등록할 코드 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 수정",
            description = "관리자 코드 관리 수정"
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
    @PostMapping("/admin/management/code/edit")
    @ResponseBody
    public JSONResponse<?> adminManagementCodeEditProc(LinkCodeSaveDto requestDto) {
        try {
            linkCodeService.adminManagementCodeEditProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    /**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 삭제
     *
     * @param id 삭제할 카테고리 시퀀스
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 삭제",
            description = "관리자 코드 관리 삭제"
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
    @DeleteMapping("/admin/management/code/{id}")
    @ResponseBody
    public JSONResponse<?> adminManagementCodeDeleteProc(@PathVariable("id") Long id) {
        try {
            linkCodeService.adminManagementCodeDeleteProc(id);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", id);
    }

    /**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 노출 순서 변경
     *
     * @param idList 순서 변경할 시퀀스 값의 배열
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 노출 순서 변경",
            description = "관리자 코드 관리 노출 순서 변경"
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
    @PostMapping("/admin/management/code/change")
    @ResponseBody
    public JSONResponse<?> adminManagementCodeChange(@RequestParam("id[]") List<Long> idList) {
        try {
            linkCodeService.adminManagementCodeChange(idList);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", idList);
    }

}
