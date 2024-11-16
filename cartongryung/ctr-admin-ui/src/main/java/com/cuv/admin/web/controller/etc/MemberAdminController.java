package com.cuv.admin.web.controller.etc;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.authoritymenu.AuthorityMenuService;
import com.cuv.admin.domain.authoritymenu.entity.AuthorityMenu;
import com.cuv.admin.domain.member.enumset.MemberRole;
import com.cuv.admin.domain.memberadmin.MemberAdminService;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminDetailDto;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminListDto;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminSaveDto;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminSearchDto;
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

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Tag(
        name = "관리자 -> 기타 -> 딜러 관리, 상담사 관리, 운영자 관리",
        description = "관리자 -> 기타 -> 딜러 관리, 상담사 관리, 운영자 관리"
)
@Controller
@RequiredArgsConstructor
public class MemberAdminController {

    private final AuthorityMenuService authorityMenuService;

    private final MemberAdminService memberAdminService;

    /**
     * 관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 등록 폼
     *
     * @param condition 회원 권한을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 등록 폼",
            description = "관리자 회원 관리 등록 폼"
    )
    @GetMapping("/admin/etc/management/write")
    public String adminEtcManagementWrite(MemberAdminSearchDto condition, Model model) {
        if (MemberRole.ofRole(condition.getRole()) == MemberRole.NONE) condition.setRole(MemberRole.DEALER.getRole());

        if (MemberRole.DEALER.getRole().equals(condition.getRole()) ||
                MemberRole.COUNSELOR.getRole().equals(condition.getRole())) {
            // 메뉴 권한 목록 조회
            List<AuthorityMenu> authorityMenus = authorityMenuService.findAllAuthorityMenuByManageYn();

            model.addAttribute("authorityMenus", authorityMenus);
        }

        model.addAttribute("condition", condition);

        return "etc/management_write";

    }

    /**
     * 관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 등록
     *
     * @param requestDto 등록할 회원 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 등록",
            description = "관리자 회원 관리 등록 폼"
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
    @PostMapping("/admin/etc/management/write")
    @ResponseBody
    public JSONResponse<?> adminEtcManagementWriteProc(MemberAdminSaveDto requestDto) {
        try {
            return new JSONResponse<>(200, "SUCCESS", memberAdminService.adminEtcManagementWriteProc(requestDto));
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
    }


    /**
     * 관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 목록
     *
     * @param condition 회원 권한을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 목록",
            description = "관리자 회원 관리 목록"
    )
    @GetMapping("/admin/etc/management")
    public String adminEtcManagementList(MemberAdminSearchDto condition, Model model) {
        int setPage = 1;
        int setSize = 50;

        String page = condition.getPage();
        String size = condition.getSize();
        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);
        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());

        if (MemberRole.ofRole(condition.getRole()) == MemberRole.NONE) condition.setRole(MemberRole.DEALER.getRole());

        Page<MemberAdminListDto> memberAdminLists = memberAdminService.searchAllMemberAdmin(condition, request);

        model.addAttribute("condition", condition);
        model.addAttribute("memberAdminLists", memberAdminLists);

        return "etc/management_list";
    }

    /**
     * 관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 상세
     *
     * @param id 임원 시퀀스
     * @param condition 회원 권한을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 상세",
            description = "관리자 회원 관리 상세"
    )
    @GetMapping("/admin/etc/management/{id}")
    public String adminEtcManagementDetail(@PathVariable("id") Long id, MemberAdminSearchDto condition, Model model) {
        if (MemberRole.ofRole(condition.getRole()) == MemberRole.NONE) condition.setRole(MemberRole.DEALER.getRole());

        if (MemberRole.DEALER.getRole().equals(condition.getRole()) ||
                MemberRole.COUNSELOR.getRole().equals(condition.getRole())) {
            // 메뉴 권한 목록 조회
            List<AuthorityMenu> authorityMenus = authorityMenuService.findAllAuthorityMenuByManageYn();
            model.addAttribute("authorityMenus", authorityMenus);

            // 메뉴 권한 목록 조회 (선택된 메뉴)
            List<AuthorityMenu> selectedAuthorityMenus = authorityMenuService.findAllAuthorityMenuByLoginId(id); // Entity !!!
            model.addAttribute("selectedAuthorityMenus", selectedAuthorityMenus);
        }

        MemberAdminDetailDto memberAdmin = memberAdminService.searchMemberAdminById(id);

        model.addAttribute("condition", condition);
        model.addAttribute("memberAdmin", memberAdmin);

        return "etc/management_view";
    }

    /**
     * 관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 수정
     *
     * @param requestDto 등록할 회원 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 수정",
            description = "관리자 회원 관리 수정"
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
    @PostMapping("/admin/etc/management/edit")
    @ResponseBody
    public JSONResponse<?> adminEtcManagementEditProc(MemberAdminSaveDto requestDto) {
        try {
            return new JSONResponse<>(200, "SUCCESS", memberAdminService.adminEtcManagementEditProc(requestDto));
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
    }

    /**
     * 관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 삭제
     *
     * @param id 임원 시퀀스
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 삭제",
            description = "관리자 회원 관리 삭제"
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
    @DeleteMapping("/admin/etc/management/{id}")
    @ResponseBody
    public JSONResponse<?> adminEtcManagementDeleteProc(@PathVariable("id") Long id) {
        try {
            return new JSONResponse<>(200, "SUCCESS", memberAdminService.adminEtcManagementDeleteProc(id));
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
    }

}
