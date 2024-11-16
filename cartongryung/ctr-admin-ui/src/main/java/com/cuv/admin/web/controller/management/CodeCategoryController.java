/*
package com.cuv.admin.web.controller.management;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.codecategory.CodeCategoryService;
import com.cuv.admin.domain.codecategory.dto.CodeCategoryDetailDto;
import com.cuv.admin.domain.codecategory.dto.CodeCategoryListDto;
import com.cuv.admin.domain.codecategory.dto.CodeCategorySaveDto;
import com.cuv.admin.domain.codecategory.dto.CodeCategorySearchDto;
import com.cuv.admin.domain.codecategory.enumset.CodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CodeCategoryController {

    private final CodeCategoryService codeCategoryService;

    */
/**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     *//*

    @GetMapping("/admin/management/code/class")
    public String adminManagementCodeClass(CodeCategorySearchDto condition, Model model) {
        condition.setCodeType(CodeType.VEHICLE_CLASS.getCode());
        condition.setDepth(0);
        List<CodeCategoryListDto> codeCategoryLists = codeCategoryService.searchAllCodeCategory(condition);

        model.addAttribute("condition", condition);
        model.addAttribute("codeCategoryLists", codeCategoryLists);

        return "management/code_list";
    }

    */
/**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 등록 팝업
     *
     * @param depth 차수
     * @param id 코드 카테고리 시퀀스
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     *//*

    @GetMapping("/admin/management/code/class/write/{depth}/{id}")
    public String adminManagementOptionCodeWrite(@PathVariable("depth") int depth, @PathVariable("id") int id,
                                           CodeCategorySearchDto condition, Model model) {
        condition.setCodeType(CodeType.VEHICLE_CLASS.getCode());

        model.addAttribute("condition", condition);

        return "popup/code_write";
    }

    */
/**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정, 옵션 설정 > 등록
     *
     * @param requestDto 등록할 코드 정보를 담은 DTO
     * @author SungHa
     *//*

    @PostMapping("/admin/management/code/write")
    @ResponseBody
    public JSONResponse<?> adminManagementCodeWriteProc(CodeCategorySaveDto requestDto) {
        try {
            Long id = codeCategoryService.adminManagementCodeWriteProc(requestDto);
            requestDto.setId(id);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    */
/**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 하위 목록 출력
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     *//*

    @PostMapping("/admin/management/code/class/list/")
    @ResponseBody
    public JSONResponse<?> adminManagementCodeClassList(CodeCategorySearchDto condition) {
        condition.setCodeType(CodeType.VEHICLE_CLASS.getCode());
        List<CodeCategoryListDto> codeCategoryLists;

        try {
            codeCategoryLists = codeCategoryService.searchAllCodeCategory(condition);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", codeCategoryLists);
    }

    */
/**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정, 옵션 설정 > 수정 팝업
     *
     * @param depth 차수
     * @param id 코드 카테고리 시퀀스
     * @author SungHa
     *//*

    @GetMapping("/admin/management/code/{depth}/{id}")
    public String adminManagementCodeDetail(@PathVariable("depth") int depth, @PathVariable("id") Long id, Model model){
        CodeCategoryDetailDto codeCategory = codeCategoryService.searchCodeCategoryById(id);

        model.addAttribute("codeCategory", codeCategory);

        return "popup/code_view";
    }

    */
/**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정, 옵션 설정 > 수정
     *
     * @param requestDto 등록할 코드 정보를 담은 DTO
     * @author SungHa
     *//*

    @PostMapping("/admin/management/code/edit")
    @ResponseBody
    public JSONResponse<?> adminManagementCodeEditProc(CodeCategorySaveDto requestDto) {
        try {
            codeCategoryService.adminManagementCodeEditProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    */
/**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정, 옵션 설정 > 삭제
     *
     * @param id 삭제할 카테고리 시퀀스
     * @author SungHa
     *//*

    @DeleteMapping("/admin/management/code/{id}")
    @ResponseBody
    public JSONResponse<?> adminManagementCodeDeleteProc(@PathVariable("id") Long id) {
        try {
            codeCategoryService.adminManagementCodeDeleteProc(id);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", id);
    }

    */
/**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정, 옵션 설정 > 노출 순서 변경
     *
     * @param idList 순서 변경할 시퀀스 값의 배열
     * @author SungHa
     *//*

    @PostMapping("/admin/management/code/change")
    @ResponseBody
    public JSONResponse<?> adminManagementCodeChange(@RequestParam("id[]") List<Long> idList) {
        try {
            codeCategoryService.adminManagementCodeChange(idList);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", idList);
    }

    */
/**
     * 관리자 | 차량관리 > 코드 관리 > 옵션 설정
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     *//*

    @GetMapping("/admin/management/code/option")
    public String adminManagementCodeOption(CodeCategorySearchDto condition, Model model) {
        condition.setCodeType(CodeType.OPTION.getCode());
        condition.setDepth(0);
        List<CodeCategoryListDto> codeCategoryLists = codeCategoryService.searchAllCodeCategory(condition);

        model.addAttribute("condition", condition);
        model.addAttribute("codeCategoryLists", codeCategoryLists);

        return "management/option_list";
    }

    */
/**
     * 관리자 | 차량관리 > 코드 관리 > 옵션 설정 > 등록 팝업
     *
     * @param depth 차수
     * @param id 코드 카테고리 시퀀스
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     *//*

    @GetMapping("/admin/management/code/option/write/{depth}/{id}")
    public String adminManagementCodeOptionWrite(@PathVariable("depth") int depth, @PathVariable("id") int id,
                                                 CodeCategorySearchDto condition, Model model) {
        condition.setCodeType(CodeType.OPTION.getCode());

        model.addAttribute("condition", condition);

        return "popup/code_write";
    }

    */
/**
     * 관리자 | 차량관리 > 코드 관리 > 옵션 설정 > 하위 목록 출력
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     *//*

    @PostMapping("/admin/management/code/option/list/")
    @ResponseBody
    public JSONResponse<?> adminManagementCodeOptionList(CodeCategorySearchDto condition) {
        condition.setCodeType(CodeType.OPTION.getCode());
        List<CodeCategoryListDto> codeCategoryLists;

        try {
            codeCategoryLists = codeCategoryService.searchAllCodeCategory(condition);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", codeCategoryLists);
    }

}
*/
