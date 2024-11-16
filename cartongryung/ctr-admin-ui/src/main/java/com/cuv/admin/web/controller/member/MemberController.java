package com.cuv.admin.web.controller.member;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.member.MemberService;
import com.cuv.admin.domain.member.dto.*;
import com.cuv.admin.domain.member.enumset.RegType;
import com.cuv.admin.domain.memberadminmemo.MemberAdminMemoService;
import com.cuv.admin.domain.purchaseinquiry.dto.PurchaseInquirySearchDto;
import com.cuv.admin.web.security.auth.PrincipalDetails;
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
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

@Tag(
    name = "관리자 -> 회원 -> 활동 회원, 탈퇴 회원, 푸시 관리",
    description = "관리자 -> 회원 -> 활동 회원, 탈퇴 회원, 푸시 관리"
)
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberAdminMemoService memberAdminMemoService;

    /**
     * 관리자 | 회원 -> 활동회원 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 회원 -> 활동회원 목록",
            description = "관리자 활동 회원 목록"
    )
    @GetMapping("/admin/member/member")
    public String adminMember(MemberSearchDto condition, Model model) {
        int setPage = 1;
        int setSize = 50;

        String page = condition.getPage();
        String size = condition.getSize();

        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);

        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());

        Page<MemberListDto> memberList = memberService.searchAllMember(condition, request);

        model.addAttribute("memberList", memberList);
        model.addAttribute("condition", condition);

        return "member/member_list";
    }

    /**
     * 관리자 | 회원 -> 활동회원 상세
     *
     * @param id 회원 시퀀스
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 회원 -> 활동회원 상세",
            description = "관리자 회원 활동회원 상세"
    )
    @GetMapping("/admin/member/member/{id}")
    public String adminMemberDetail(@PathVariable("id") Long id, Model model) {
        MemberDetailDto memberDetail = memberService.searchMemberById(id);
        List<MemberAdminMemoListDto> memberAdminMemoList = memberAdminMemoService.searchMemberAdminMemoList(id);
        List<MemberPurchaseInquiryListDto> purchaseInquiryList = memberService.searchMemberPurchaseList(id);
        List<MemberSaleInquiryListDto> saleInquiryList = memberService.searchMemberSaleList(id);

        model.addAttribute("memberDetail", memberDetail);
        model.addAttribute("purchaseInquiryList", purchaseInquiryList);
        model.addAttribute("saleInquiryList", saleInquiryList);
        model.addAttribute("memberAdminMemoList", memberAdminMemoList);

        return "member/member_view";
    }

    /**
     * 관리자 | 회원 -> 활동회원 메모 저장
     *
     * @param map 메모 데이터를 담은 map
     * @param principalDetails 로그인한 관리자 정보
     * @return
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 회원 -> 활동회원 메모 저장",
            description = "관리자 활동 회원 메모 저장"
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
    @PostMapping("/admin/member/member/memo")
    @ResponseBody
    public JSONResponse<?> adminMemberMemoSave(@RequestBody Map<String, Object> map, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        JSONResponse<?> response;
        try {
            response = memberAdminMemoService.saveMemberAdminMemo(map, principalDetails);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }

    /**
     * 관리자 | 회원 -> 활동회원 정보 수정
     *
     * @param dto 회원 정보를 담은 DTO
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 회원 -> 활동회원 정보 수정",
            description = "관리자 활동회원 정보 수정"
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
    @PostMapping("/admin/member/member/update")
    @ResponseBody
    public JSONResponse<?> adminMemberUpdate(MemberSaveDto dto) {
        JSONResponse<?> response;
        try {
            response = memberService.updateMember(dto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }


    /**
     * 관리자 | 회원 -> 탈퇴회원 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 회원 -> 탈퇴회원 목록",
            description = "관리자 탈퇴 회원 목록"
    )
    @GetMapping("/admin/member/withdraw")
    public String adminMemberWithdraw(MemberSearchDto condition, Model model) {
        int setPage = 1;
        int setSize = 50;

        String page = condition.getPage();
        String size = condition.getSize();

        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);

        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("withdrawAt").descending());

        Page<MemberListDto> withdrawList = memberService.searchAllMemberWithdrawList(condition, request);

        model.addAttribute("withdrawList", withdrawList);
        model.addAttribute("condition", condition);
        return "member/withdraw_list";
    }

    /**
     * 관리자 | 회원 -> 탈퇴회원 사유 팝업
     * @param id 회원 시퀀스
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 회원 -> 탈퇴회원 사유 팝업",
            description = "관리자 탈퇴회원 사유 팝업"
    )
    @GetMapping("/admin/member/withdraw/popup/{id}")
    public String adminMemberWithdrawPopup(@PathVariable("id") Long id, Model model) {
        MemberWithdrawDto result = memberService.searchMemberWithdrawPopup(id);
        model.addAttribute("result", result);
        return "popup/withdraw_reason";
    }

    /**
     * 관리자 | 회원 > 활동회원 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 회원 > 활동회원 > 엑셀",
            description = "관리자 회원 활동회원 엑셀"
    )
    @GetMapping("/admin/member/active/excel")
    public void adminActiveMemberTableExcel(MemberSearchDto condition, HttpServletResponse response) throws IOException {
        Workbook wb = memberService.adminActiveMemberTableExcel(condition);

        String fileName = URLEncoder.encode("활동회원.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }

    /**
     * 관리자 | 회원 > 탈퇴회원 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 회원 > 탈퇴회원 > 엑셀",
            description = "관리자 탈퇴회원 엑셀"
    )
    @GetMapping("/admin/member/withdraw/excel")
    public void adminWithdrawMemberTableExcel(MemberSearchDto condition, HttpServletResponse response) throws IOException {
        Workbook wb = memberService.adminWithdrawMemberTableExcel(condition);

        String fileName = URLEncoder.encode("탈퇴회원.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }
}
