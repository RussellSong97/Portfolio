package com.cuv.admin.web.controller.main;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.boardnotice.BoardNoticeService;
import com.cuv.admin.domain.boardnotice.dto.BoardNoticeListDto;
import com.cuv.admin.domain.inquirydetail.dto.InquiryDetailCountDto;
import com.cuv.admin.domain.member.MemberService;
import com.cuv.admin.domain.member.dto.MemberJoinCountDto;
import com.cuv.admin.domain.memberadmin.MemberAdminRepository;
import com.cuv.admin.domain.memberadmin.entity.MemberAdmin;
import com.cuv.admin.domain.product.ProductService;
import com.cuv.admin.domain.purchaseinquiry.PurchaseInquiryService;
import com.cuv.admin.domain.purchaseinquiry.dto.PurchaseInquiryListDto;
import com.cuv.admin.domain.purchaseinquiry.dto.PurchaseInquirySearchDto;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import com.cuv.admin.domain.purchaseinquiry.enumset.InquiryType;
import com.cuv.admin.domain.saleinquiry.SaleInquiryService;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquiryListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 관리자 | 메인
 */
@Tag(
        name = "관리자 -> 메인",
        description = "관리자 -> 메인"
)
@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberAdminRepository memberAdminRepository;
    private final PurchaseInquiryService purchaseInquiryService;
    private final SaleInquiryService saleInquiryService;
    private final ProductService productService;
    private final MemberService memberService;
    private final BoardNoticeService boardNoticeService;

    /**
     * 관리자 | 메인
     *
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 메인",
            description = "관리자 메인으로 리다이렉트"
    )
    @GetMapping({"", "/", "/admin", "/admin/"})
    public String index() {
        return "redirect:/admin/main";
    }

    /**
     * 관리자 | 메인
     *
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 메인",
            description = "관리자 메인"
    )
    @GetMapping("/admin/main")
    public String adminMain(PurchaseInquirySearchDto purchaseInquiryCondition, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String memberAdminId = auth.getName();
        MemberAdmin memberAdmin = memberAdminRepository.findByLoginId(memberAdminId).orElse(null);

        if (memberAdmin == null)
            return "redirect:/admin/login";

        // 내 차 구입 수
        Long purchaseCount = purchaseInquiryService.searchAllPurchaseInquiryCountByMain();

        // 구입 상담 상세 수
        List<InquiryDetailCountDto> purchaseInquiryDetailCount = purchaseInquiryService.searchAllPurchaseInquiryDetailCountByMain();
        Map<ConsultationStatus, Long> purchaseStatuses = new HashMap<>();
        purchaseInquiryDetailCount.forEach(enums -> purchaseStatuses.put(enums.getConsultationStatus(), enums.getStatusCount()));

        // 내 차 팔기 수
        Long saleCount = saleInquiryService.searchAllSaleInquiryCountByMain();

        // 팔기 상담 상세 수
        List<InquiryDetailCountDto> saleInquiryDetailCount = saleInquiryService.searchAllSaleInquiryDetailCountByMain();
        Map<ConsultationStatus, Long> saleStatuses = new HashMap<>();
        saleInquiryDetailCount.forEach(enums -> saleStatuses.put(enums.getConsultationStatus(), enums.getStatusCount()));

        // 내 차 구입 목록
        purchaseInquiryCondition.setType(InquiryType.CONTACTABLE.getCode());
        List<PurchaseInquiryListDto> contactableLists =  purchaseInquiryService.searchAllPurchaseInquiryByMain(purchaseInquiryCondition);

        // 방문 예약 목록
        purchaseInquiryCondition.setType(InquiryType.VISIT_RESERVATION.getCode());
        List<PurchaseInquiryListDto> visitReservationLists =  purchaseInquiryService.searchAllPurchaseInquiryByMain(purchaseInquiryCondition);

        // 내 차 팔기 목록
        List<SaleInquiryListDto> saleInquiryLists = saleInquiryService.searchAllSaleInquiryByMain();

        // 판매 건수
        Long soldOutCount = productService.searchAllSoldOutProductCountByMain();

        // 누적 매출
        Long carAmountSale = productService.searchAllSalesByMain();
        if (carAmountSale != null) {
            model.addAttribute("monthOfSales", String.valueOf(carAmountSale / 10000));
        } else {
            model.addAttribute("monthOfSales", 0);
        }

        // 등록 차량
        Long productCount = productService.searchAllProductCountByMain();

        // 금일 가입수
        Long todayJoinCount = memberService.searchAllTodayJoinMemberCountByMain("today");

        // 총 회원수
        Long memberCount = memberService.searchAllMemberCountByMain();

        // 공지사항
        List<BoardNoticeListDto> boardNoticeLists = boardNoticeService.searchAllBoardNoticeByMain();

        model.addAttribute("purchaseCount", purchaseCount);
        model.addAttribute("purchaseStatuses", purchaseStatuses);
        model.addAttribute("saleCount", saleCount);
        model.addAttribute("saleStatuses", saleStatuses);
        model.addAttribute("contactableLists", contactableLists);
        model.addAttribute("visitReservationLists", visitReservationLists);
        model.addAttribute("saleInquiryLists", saleInquiryLists);
        model.addAttribute("soldOutCount", soldOutCount);
        model.addAttribute("productCount", productCount);
        model.addAttribute("todayJoinCount", todayJoinCount);
        model.addAttribute("memberCount", memberCount);
        model.addAttribute("boardNoticeLists", boardNoticeLists);

        return "main/index";
    }

    /**
     * 관리자 | 메인 > 회원 현황 > 차트
     *
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 메인 > 회원 현황 > 차트",
            description = "관리자 메인 회훤 현황 차트"
    )
    @GetMapping("/admin/main/chart")
    @ResponseBody
    public JSONResponse<?> getMainChart() {
        LinkedHashMap<String, Long> week = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 6; i >= 0; i--) {
            String formattedDate = today.minusDays(i).format(formatter);
            week.put(formattedDate, 0L);
        }

        List<MemberJoinCountDto> weekJoinCount = memberService.searchAllWeekJoinMemberCountByMain("week");
        weekJoinCount.forEach(data -> {
            if (week.containsKey(data.getCreatedDate())) {
                week.put(data.getCreatedDate(), data.getCount());
            }
        });

        return new JSONResponse<>(200, "SUCCESS", week.values());
    }

    /**
     * 관리자 | 로그인
     *
     * @param session 로그인 실패 메시지가 담긴 세션
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 로그인",
            description = "관리자 로그인 실패 메시지 처리"
    )
    @GetMapping("/admin/login")
    public String adminLogin(HttpSession session, Model model) {
        String errorMessage = (String) session.getAttribute("errorMessage");

        // 로그인 실패 오류 메시지
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }

        return "login/login";
    }
}
