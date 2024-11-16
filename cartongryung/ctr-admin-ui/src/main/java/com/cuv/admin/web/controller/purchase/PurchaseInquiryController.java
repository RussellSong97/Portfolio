package com.cuv.admin.web.controller.purchase;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.inquirydetail.InquiryDetailService;
import com.cuv.admin.domain.inquirydetail.dto.InquiryDetailListDto;
import com.cuv.admin.domain.inquirydetail.dto.InquiryDetailSaveDto;
import com.cuv.admin.domain.inquirydetail.enumset.TradeType;
import com.cuv.admin.domain.member.MemberService;
import com.cuv.admin.domain.member.dto.MemberListDto;
import com.cuv.admin.domain.member.dto.MemberSearchDto;
import com.cuv.admin.domain.member.enumset.MemberRole;
import com.cuv.admin.domain.memberadmin.MemberAdminService;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminListDto;
import com.cuv.admin.domain.product.ProductService;
import com.cuv.admin.domain.product.dto.ProductListDto;
import com.cuv.admin.domain.product.dto.ProductSearchDto;
import com.cuv.admin.domain.purchaseinquiry.PurchaseInquiryService;
import com.cuv.admin.domain.purchaseinquiry.dto.*;
import com.cuv.admin.domain.purchaseinquiry.enumset.InquiryType;
import com.cuv.admin.domain.wishlist.WishlistService;
import com.cuv.admin.domain.wishlist.dto.WishlistListDto;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Tag(
        name = "관리자 -> 내 차 구입",
        description = "관리자 -> 내 차 구입"
)
@Controller
@RequiredArgsConstructor
public class PurchaseInquiryController {

    private final PurchaseInquiryService purchaseInquiryService;

    private final MemberAdminService memberAdminService;

    private final MemberService memberService;

    private final InquiryDetailService inquiryDetailService;

    private final WishlistService wishlistService;

    private final ProductService productService;


    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의, 연락처 없는 문의, 방문 예약 > 목록
     *
     * @param condition 상담 구분을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 구입 > 연락 가능 문의, 연락처 없는 문의, 방문 예약 > 목록",
            description = "관리자 내 차 구입 목록"
    )
    @GetMapping("/admin/purchase/inquiry")
    public String adminPurchaseInquiryList(PurchaseInquirySearchDto condition, Model model) {
        // 내 차 구입 목록 유형
        if (InquiryType.ofCode(condition.getType()) == InquiryType.NONE) condition.setType(InquiryType.CONTACTABLE.getCode());
        model.addAttribute("condition", condition);

        // 내 차 구입 문의별 개수
        List<Long> purchaseInquiryCount = purchaseInquiryService.searchAllPurchaseInquiryCount();
        model.addAttribute("purchaseInquiryCount", purchaseInquiryCount);

        int setPage = 1;
        int setSize = 50;

        String page = condition.getPage();
        String size = condition.getSize();
        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);
        String orderColumn = hasText(condition.getSort()) ? condition.getSort() : "id";

        Sort.Direction direction = Sort.Direction.DESC;
        if (hasText(condition.getOrder()) && condition.getOrder().equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }

        // 연락 가능 문의
        if (InquiryType.CONTACTABLE.getCode().equals(condition.getType())) {
            PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by(direction, orderColumn));
            Page<PurchaseInquiryListDto> contactableLists = purchaseInquiryService.searchAllPurchaseInquiryLists(condition, request);

            model.addAttribute("contactableLists", contactableLists);

            // 검색 조건 - 딜러
            List<MemberAdminListDto> memberDealerLists = memberAdminService.searchAllMemberDealer(MemberRole.DEALER.getRole());

            // 검색 조건 - 상담원 (딜러 & 상담원)
            List<MemberAdminListDto> memberCounselorLists = memberAdminService.searchAllMemberDealerAndMemberCounselor(MemberRole.ADMIN.getRole());

            model.addAttribute("memberDealerLists", memberDealerLists);
            model.addAttribute("memberCounselorLists", memberCounselorLists);

            return "purchase/purchase_list_able";

        // 연락처 없는 문의
        } else if (InquiryType.WITHOUT_CONTACT.getCode().equals(condition.getType())) {
            PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by(direction, orderColumn));
            Page<PurchaseInquiryListDto> withoutContactLists = purchaseInquiryService.searchAllWithoutContactInquiryLists(condition, request);

            model.addAttribute("withoutContactLists", withoutContactLists);

            return "purchase/purchase_list_unable";

        // 방문 예약
        } else {
            PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by(direction, orderColumn));
            Page<PurchaseInquiryListDto> visitReservationLists = purchaseInquiryService.searchAllPurchaseInquiryLists(condition, request);

            model.addAttribute("visitReservationLists", visitReservationLists);

            return "purchase/visit_list";
        }
    }

    /**
     * 관리자 | 내 차 구입 > 연락처 없는 문의, 방문 예약 > 연락 가능 문의 이동
     *
     * @param idList 이동할 시퀀스 값의 배열
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 구입 > 연락처 없는 문의, 방문 예약 > 연락 가능 문의 이동",
            description = "관리자 연락처 없는 문의, 방문 예약 목록에서 연락 가능 문의로 이동"
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
    @PostMapping("/admin/purchase/inquiry/move")
    @ResponseBody
    public JSONResponse<?> adminPurchaseInquiryMove(@RequestParam("id[]") List<Long> idList) {
        try {
            purchaseInquiryService.adminPurchaseInquiryMove(idList);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", idList);
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의, 연락처 없는 문의, 방문 예약 > 삭제
     *
     * @param idList 삭제할 시퀀스 값의 배열
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 구입 > 연락 가능 문의, 연락처 없는 문의, 방문 예약 > 삭제",
            description = "관리자 내 차 구입 삭제"
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
    @DeleteMapping("/admin/purchase/inquiry/delete")
    @ResponseBody
    public JSONResponse<?> adminPurchaseInquiryDeleteProc(@RequestParam("id[]") List<Long> idList) {
        try {
            purchaseInquiryService.adminPurchaseInquiryDeleteProc(idList);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", idList);
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 등록 > 팝업
     *
     * @param inquiryId 상담 시퀀스
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 구입 > 연락 가능 문의 등록 > 팝업",
            description = "관리자 연락 가능 문의 등록 팝업"
    )
    @GetMapping("/admin/purchase/inquiry/popup")
    public String adminPurchaseInquiryPopup(@RequestParam(value = "inquiryId", required = false) Long inquiryId, Model model) {
        // 문의에 등록된 회원 정보
        PurchaseInquiryMemberDto purchaseInquiryMemberInfo = new PurchaseInquiryMemberDto();
        if (inquiryId != null) {
            purchaseInquiryMemberInfo = purchaseInquiryService.searchMemberInfoByPurchaseInquiryId(inquiryId);
        }

        model.addAttribute("purchaseInquiryMemberInfo", purchaseInquiryMemberInfo);

        return "popup/able_write";
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 등록 > 회원 목록
     *
     * @param page 페이지 번호
     * @param size 데이터 개수
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 구입 > 연락 가능 문의 등록 > 회원 목록",
            description = "관리자 연락 가능 문의 등록 회원 목록"
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
    @PostMapping("/admin/purchase/member")
    @ResponseBody
    public JSONResponse<?> adminPurchaseMemberList(@RequestParam("page") Integer page, @RequestParam("size") Integer size, MemberSearchDto condition) {
        String setPage = String.valueOf(page);
        String setSize = String.valueOf(size);
        if (hasText(setPage) && setPage.matches("^\\d+$")) page = Math.max(Integer.parseInt(setPage), 1);
        if (hasText(setSize) && setSize.matches("^\\d+$")) size = Math.max(Integer.parseInt(setSize), 1);

        condition.setPage(String.valueOf(page));
        condition.setSize(String.valueOf(size));

        PageRequest request = PageRequest.of(page - 1, size, Sort.by("realName").descending());

        Page<MemberListDto> memberLists = memberService.searchAllMember(condition, request);

        return new JSONResponse<>(200, "SUCCESS", memberLists);
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 등록 > 저장
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 구입 > 연락 가능 문의 등록 > 저장",
            description = "관리자 연락 가능 문의 등록 저장"
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
    @PostMapping("/admin/purchase/member/write")
    @ResponseBody
    public JSONResponse<?> adminPurchaseMemberWriteProc(PurchaseInquiryMemberDto requestDto) {
        try {
            purchaseInquiryService.adminPurchaseMemberWriteProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 상세
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 구입 > 연락 가능 문의 > 상세",
            description = "관리자 연락 가능 문의 상세"
    )
    @GetMapping("/admin/purchase/inquiry/{id}")
    public String adminPurchaseInquiryDetail(@PathVariable("id") Long id, Model model) {
        // 내 차 구입 문의별 개수
        List<Long> purchaseInquiryCount = purchaseInquiryService.searchAllPurchaseInquiryCount();

        // 상담 기록
        List<InquiryDetailListDto> inquiryDetailLists = inquiryDetailService.searchAllInquiryDetailListsByTradeType(id, TradeType.BUY.getCode());

        // 문의 내용
        PurchaseInquiryDetailDto purchaseInquiry = purchaseInquiryService.searchPurchaseInquiryById(id);

        // 구매희망 차량 목록
        List<WishlistListDto> wishlists = wishlistService.searchAllWishlists(id);

        model.addAttribute("purchaseInquiryCount", purchaseInquiryCount);
        model.addAttribute("inquiryDetailLists", inquiryDetailLists);
        model.addAttribute("purchaseInquiry", purchaseInquiry);
        model.addAttribute("wishlists", wishlists);

        return "purchase/purchase_view_able";
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 상세 > 상담 기록 저장
     * 내 차 팔기 > 상세 > 상담 기록 저장
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 구입 > 연락 가능 문의 > 상세 > 상담 기록 저장",
            description = "관리자 연락 가능 문의에서 상담 기록 저장"
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
    @PostMapping("/admin/purchase/inquiry/detail/write")
    @ResponseBody
    public JSONResponse<?> adminInquiryDetailWriteProc(InquiryDetailSaveDto requestDto) {
        try {
            inquiryDetailService.adminInquiryDetailWriteProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 상세 > 차량 조회 팝업
     *
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 구입 > 연락 가능 문의 > 상세 > 차량 조회 팝업",
            description = "관리자 연락 가능 문의 차량 조회 팝업"
    )
    @GetMapping("/admin/purchase/product")
    public String adminPurchaseProductList(ProductSearchDto condition, Model model) {
        int setPage = 1;
        int setSize = 50;

        String page = condition.getPage();
        String size = condition.getSize();
        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);
        PageRequest request = PageRequest.of(setPage - 1, setSize);
        Page<ProductListDto> productLists = productService.searchAllPostingProductLists(condition, request);

        model.addAttribute("productLists", productLists);

        return "popup/vehicle_search";
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 상세 > 저장
     * 관리자 | 내 차 팔기 > 상세 > 저장
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 구입 > 연락 가능 문의 > 상세 > 저장, 관리자 | 내 차 팔기 > 상세 > 저장",
            description = "관리자 연락 가능 문의 저장"
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
    @PostMapping("/admin/purchase/inquiry/edit")
    @ResponseBody
    public JSONResponse<?> adminPurchaseInquiryEditProc(PurchaseInquirySaveDto requestDto) {
        try {
            purchaseInquiryService.adminPurchaseInquiryEditProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 구입 > 연락 가능 문의 > 엑셀",
            description = "관리자 연락 가능 문의 엑셀"
    )
    @GetMapping("/admin/purchase/contactable/excel")
    public void adminPurchaseContactableExcel(PurchaseInquirySearchDto condition, HttpServletResponse response) throws IOException {
        Workbook wb = purchaseInquiryService.searchAllContactableInquiryExcelLists(condition);

        String fileName = URLEncoder.encode("연락 가능 문의.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }

    /**
     * 관리자 | 내 차 구입 > 연락처 없는 문의 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 구입 > 연락처 없는 문의 > 엑셀",
            description = "관리자 연락처 없는 문의 엑셀"
    )
    @GetMapping("/admin/purchase/without/excel")
    public void adminPurchaseWithoutContactExcel(PurchaseInquirySearchDto condition, HttpServletResponse response) throws IOException {
        Workbook wb = purchaseInquiryService.searchAllWithoutContactInquiryExcelLists(condition);

        String fileName = URLEncoder.encode("연락처 없는 문의.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }

    /**
     * 관리자 | 내 차 구입 > 방문 예약 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author Sangbin
     */
    @Operation(
            summary = "관리자 | 내 차 구입 > 방문 예약 > 엑셀",
            description = "관리자 방문 예약 엑셀"
    )
    @GetMapping("/admin/purchase/visit/excel")
    public void adminPurchaseVisitExcel(PurchaseInquirySearchDto condition, HttpServletResponse response) throws IOException {
        Workbook wb = purchaseInquiryService.searchAllVisitInquiryExcelLists(condition);

        String fileName = URLEncoder.encode("방문 예약.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }
}
