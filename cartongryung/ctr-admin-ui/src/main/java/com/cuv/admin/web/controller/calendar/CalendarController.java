package com.cuv.admin.web.controller.calendar;

import com.cuv.admin.domain.calendar.CalendarService;
import com.cuv.admin.domain.calendar.dto.*;
import com.cuv.admin.domain.calendar.dto.consult.ConsultDto;
import com.cuv.admin.domain.calendar.dto.consult.ConsultSummaryDto;
import com.cuv.admin.domain.calendar.dto.counslorDealer.StatDto;
import com.cuv.admin.domain.calendar.dto.member.MemberStatDto;
import com.cuv.admin.domain.calendar.dto.member.MemberStatMonthAndYearDto;
import com.cuv.admin.domain.member.enumset.MemberRole;
import com.cuv.admin.domain.memberadmin.MemberAdminService;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 관리자 | 통계 컨트롤러
 */
@Tag(
        name = "관리자 -> 통계",
        description = "관리자 -> 통계 -> 통계 경로"
)
@RequiredArgsConstructor
@Controller
public class CalendarController {

    private final CalendarService calendarService;
    private final MemberAdminService memberAdminService;

    /**
     * 관리자 | 통계 > 회원 > 일별
     *
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 회원통계 > 일별",
            description = "회원통계 일별"
    )
    @GetMapping("/admin/stats/stats")
    public String enterMemberStat(Model model, StatSearchDto condition) {

        List<MemberStatDto> memberStatDtoList = calendarService.getMemberStat(condition);

        model.addAttribute("memberStatDtoList", memberStatDtoList);
        model.addAttribute("condition", condition);

        return "stats/stats_member";
    }

    /**
     * 관리자 | 통계 > 회원 > 월별
     *
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 회원통계 > 월별",
            description = "회원통계 월별"
    )
    @GetMapping("/admin/stats/stats/month")
    public String enterMemberStatMonth(Model model, StatSearchDto condition) {

        List<MemberStatMonthAndYearDto> memberStatDtoList = calendarService.getMemberStatMonth(condition);

        model.addAttribute("memberStatDtoList", memberStatDtoList);
        model.addAttribute("condition", condition);

        return "stats/stats_member_m";
    }

    /**
     * 관리자 | 통계 > 회원 > 일별
     *
     * @author 박성민
     */

    @Operation(
            summary = "관리자 | 통계 > 회원통계 > 년별",
            description = "회원통계 년별"
    )
    @GetMapping("/admin/stats/stats/year")
    public String enterMemberStatYear(Model model, StatSearchDto condition) {

        List<MemberStatMonthAndYearDto> memberStatDtoList = calendarService.getMemberStatYear(condition);

        model.addAttribute("memberStatDtoList", memberStatDtoList);
        model.addAttribute("condition", condition);

        return "stats/stats_member_y";
    }

    /**
     * 관리자 | 통계 > 상담원 > 팔기
     *
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 상담원 > 팔기",
            description = "상담원 내차 팔기 통계"
    )
    @GetMapping("/admin/stats/counselor/sale")
    public String counselorSale(Model model, StatSearchDto condition) {

        List<StatDto> saleStats = calendarService.getCounselorSaleStat(condition);

        // Sort the saleStats by date in descending order
        saleStats.sort((a, b) -> b.getCalendarDate().compareTo(a.getCalendarDate()));

        List<MemberAdminListDto> memberCounselorLists = memberAdminService.searchAllMemberCounselor(MemberRole.COUNSELOR.getRole());

        // Create a map to store stats for each date and counselor
        Map<LocalDate, Map<Long, int[]>> groupedStats = new LinkedHashMap<>();

        // Initialize the map with all dates and counselors
        saleStats.stream().map(StatDto::getCalendarDate).distinct().forEach(date -> {
            Map<Long, int[]> counselorStats = new HashMap<>();
            memberCounselorLists.forEach(counselor ->
                    counselorStats.put(counselor.getId(), new int[]{0, 0})
            );
            groupedStats.put(date, counselorStats);
        });

        // Fill in the actual stats
        for (StatDto stat : saleStats) {
            Map<Long, int[]> dateStats = groupedStats.get(stat.getCalendarDate());
            if (dateStats == null) {
                dateStats = new HashMap<>();
                groupedStats.put(stat.getCalendarDate(), dateStats);
            }

            int[] counselorStat = dateStats.get(stat.getId());
            if (counselorStat == null) {
                counselorStat = new int[2];
                dateStats.put(stat.getId(), counselorStat);
            }

            counselorStat[0] = stat.getCompletedCounsel();
            counselorStat[1] = stat.getCompletedTrade();
        }

        // Calculate totals for each counselor
        Map<Long, int[]> counselorTotals = new HashMap<>();
        memberCounselorLists.forEach(counselor -> {
            int totalCompletedCounsel = saleStats.stream()
                    .filter(stat -> stat.getId().equals(counselor.getId()))
                    .mapToInt(StatDto::getCompletedCounsel)
                    .sum();
            int totalCompletedTrade = saleStats.stream()
                    .filter(stat -> stat.getId().equals(counselor.getId()))
                    .mapToInt(StatDto::getCompletedTrade)
                    .sum();
            counselorTotals.put(counselor.getId(), new int[]{totalCompletedCounsel, totalCompletedTrade});
        });

        model.addAttribute("groupedStats", groupedStats);
        model.addAttribute("condition", condition);
        model.addAttribute("memberCounselorLists", memberCounselorLists);
        model.addAttribute("counselorTotals", counselorTotals);

        return "stats/stats_counselor_sale";
    }

    /**
     * 관리자 | 통계 > 상담원 > 구입
     *
     * @author 박성민
     */

    @Operation(
            summary = "관리자 | 통계 > 상담원 > 구매",
            description = "상담원 내차 구매 통계"
    )
    @GetMapping("/admin/stats/counselor/purchase")
    public String counselorPurchase(Model model, StatSearchDto condition) {

        List<StatDto> saleStats = calendarService.getCounselorPurchaseStat(condition);

        // Sort the saleStats by date in descending order
        saleStats.sort((a, b) -> b.getCalendarDate().compareTo(a.getCalendarDate()));

        List<MemberAdminListDto> memberCounselorLists = memberAdminService.searchAllMemberCounselor(MemberRole.COUNSELOR.getRole());

        // Create a map to store stats for each date and counselor
        Map<LocalDate, Map<Long, int[]>> groupedStats = new LinkedHashMap<>();

        // Initialize the map with all dates and counselors
        saleStats.stream().map(StatDto::getCalendarDate).distinct().forEach(date -> {
            Map<Long, int[]> counselorStats = new HashMap<>();
            memberCounselorLists.forEach(counselor ->
                    counselorStats.put(counselor.getId(), new int[]{0, 0})
            );
            groupedStats.put(date, counselorStats);
        });

        // Fill in the actual stats
        for (StatDto stat : saleStats) {
            Map<Long, int[]> dateStats = groupedStats.get(stat.getCalendarDate());
            if (dateStats == null) {
                dateStats = new HashMap<>();
                groupedStats.put(stat.getCalendarDate(), dateStats);
            }

            int[] counselorStat = dateStats.get(stat.getId());
            if (counselorStat == null) {
                counselorStat = new int[2];
                dateStats.put(stat.getId(), counselorStat);
            }

            counselorStat[0] = stat.getCompletedCounsel();
            counselorStat[1] = stat.getCompletedTrade();
        }

        // Calculate totals for each counselor
        Map<Long, int[]> counselorTotals = new HashMap<>();
        memberCounselorLists.forEach(counselor -> {
            int totalCompletedCounsel = saleStats.stream()
                    .filter(stat -> stat.getId().equals(counselor.getId()))
                    .mapToInt(StatDto::getCompletedCounsel)
                    .sum();
            int totalCompletedTrade = saleStats.stream()
                    .filter(stat -> stat.getId().equals(counselor.getId()))
                    .mapToInt(StatDto::getCompletedTrade)
                    .sum();
            counselorTotals.put(counselor.getId(), new int[]{totalCompletedCounsel, totalCompletedTrade});
        });

        model.addAttribute("groupedStats", groupedStats);
        model.addAttribute("condition", condition);
        model.addAttribute("memberCounselorLists", memberCounselorLists);
        model.addAttribute("counselorTotals", counselorTotals);

        return "stats/stats_counselor_purchase";
    }

    /**
     * 관리자 | 통계 > 회원 > 일별회원 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */

    @Operation(
            summary = "관리자 | 통계 > 회원 > 일별 > 엑셀다운",
            description = "일별회원 엑셀다운"
    )
    @GetMapping("/admin/stats/stats/excel")
    public void searchMemberStatExcel(StatSearchDto condition, HttpServletResponse response) throws IOException {

        Workbook wb = calendarService.searchMemberStatExcel(condition);

        String fileName = URLEncoder.encode("일별회원.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }

    /**
     * 관리자 | 통계 > 회원 > 월별회원 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 회원 > 월별 > 엑셀다운",
            description = "월별회원 엑셀다운"
    )
    @GetMapping("/admin/stats/month/excel")
    public void searchMemberStatMonthExcel(StatSearchDto condition, HttpServletResponse response) throws IOException {

        Workbook wb = calendarService.searchMemberStatMonthExcel(condition);

        String fileName = URLEncoder.encode("월별회원.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }

    /**
     * 관리자 | 통계 > 회원 > 년별회원 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 회원 > 년별 > 엑셀다운",
            description = "년별회원 엑셀다운"
    )
    @GetMapping("/admin/stats/year/excel")
    public void searchMemberStatYearExcel(StatSearchDto condition, HttpServletResponse response) throws IOException {
        Workbook wb = calendarService.searchMemberStatYearExcel(condition);

        String fileName = URLEncoder.encode("년별회원.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }

    /**
     * 관리자 | 통계 > 상담원 > 구매 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 상담원 > 구입 > 엑셀다운",
            description = "내차 구입 상담원 엑셀다운"
    )
    @GetMapping("/admin/stats/counselor/purchase/excel")
    public void searchCounselorPurchaseStatExcel(StatSearchDto condition, HttpServletResponse response) throws IOException {

        Workbook wb = calendarService.searchCounselorPurchaseExcel(condition);

        String fileName = URLEncoder.encode("상담원(내차 구입).xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }

    /**
     * 관리자 | 통계 > 상담원 > 팔기 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 상담원 > 팔기 > 엑셀다운",
            description = "내차 팔기 상담원 엑셀다운"
    )
    @GetMapping("/admin/stats/counselor/sale/excel")
    public void searchCounselorSaleStatExcel(StatSearchDto condition, HttpServletResponse response) throws IOException {

        Workbook wb = calendarService.searchCounselorSaleExcel(condition);

        String fileName = URLEncoder.encode("상담원(내차 팔기).xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }

    /**
     * 관리자 | 통계 > 딜러 > 구입 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 딜러 > 구입 > 엑셀다운",
            description = "내차 구입 딜러 엑셀다운"
    )
    @GetMapping("/admin/stats/dealer/purchase/excel")
    public void searchDealerPurchaseStatExcel(StatSearchDto condition, HttpServletResponse response) throws IOException {

        Workbook wb = calendarService.searchDealerPurchaseExcel(condition);

        String fileName = URLEncoder.encode("딜러(내차 구입).xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }

    /**
     * 관리자 | 통계 > 딜러 > 판매 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 딜러 > 팔기 > 엑셀다운",
            description = "내차 팔기 딜러 엑셀다운"
    )
    @GetMapping("/admin/stats/dealer/sale/excel")
    public void searchDealerSaleStatExcel(StatSearchDto condition, HttpServletResponse response) throws IOException {

        Workbook wb = calendarService.searchDealerSaleExcel(condition);

        String fileName = URLEncoder.encode("딜러(내차 팔기).xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }

    /**
     * 관리자 | 통계 > 딜러 > 구입
     *
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 딜러 > 구매",
            description = "내차 구매 딜러"
    )
    @GetMapping("/admin/stats/dealer/purchase")
    public String dealerPurchase(Model model, StatSearchDto condition) {

        List<StatDto> saleStats = calendarService.getDealerPurchaseStat(condition);

        // Sort the saleStats by date in descending order
        saleStats.sort((a, b) -> b.getCalendarDate().compareTo(a.getCalendarDate()));

        List<MemberAdminListDto> memberDealerLists = memberAdminService.searchAllMemberDealer(MemberRole.DEALER.getRole());

        // Create a map to store stats for each date and counselor
        Map<LocalDate, Map<Long, int[]>> groupedStats = new LinkedHashMap<>();

        // Initialize the map with all dates and counselors
        saleStats.stream().map(StatDto::getCalendarDate).distinct().forEach(date -> {
            Map<Long, int[]> dealerStats = new HashMap<>();
            memberDealerLists.forEach(dear ->
                    dealerStats.put(dear.getId(), new int[]{0, 0})
            );
            groupedStats.put(date, dealerStats);
        });

        // Fill in the actual stats
        for (StatDto stat : saleStats) {
            Map<Long, int[]> dateStats = groupedStats.get(stat.getCalendarDate());
            if (dateStats == null) {
                dateStats = new HashMap<>();
                groupedStats.put(stat.getCalendarDate(), dateStats);
            }

            int[] counselorStat = dateStats.get(stat.getId());
            if (counselorStat == null) {
                counselorStat = new int[2];
                dateStats.put(stat.getId(), counselorStat);
            }

            counselorStat[0] = stat.getCompletedCounsel();
            counselorStat[1] = stat.getCompletedTrade();
        }

        // Calculate totals for each counselor
        Map<Long, int[]> dealerTotals = new HashMap<>();
        memberDealerLists.forEach(dear -> {
            int totalCompletedCounsel = saleStats.stream()
                    .filter(stat -> stat.getId().equals(dear.getId()))
                    .mapToInt(StatDto::getCompletedCounsel)
                    .sum();
            int totalCompletedTrade = saleStats.stream()
                    .filter(stat -> stat.getId().equals(dear.getId()))
                    .mapToInt(StatDto::getCompletedTrade)
                    .sum();
            dealerTotals.put(dear.getId(), new int[]{totalCompletedCounsel, totalCompletedTrade});
        });

        model.addAttribute("groupedStats", groupedStats);
        model.addAttribute("condition", condition);
        model.addAttribute("memberDealerLists", memberDealerLists);
        model.addAttribute("dealerTotals", dealerTotals);

        return "stats/stats_dealer_purchase";
    }

    /**
     * 관리자 | 통계 > 딜러 > 판매
     *
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 딜러 > 판매",
            description = "내차 판매 딜러"
    )
    @GetMapping("/admin/stats/dealer/sale")
    public String dealerSale(Model model, StatSearchDto condition) {

        List<StatDto> saleStats = calendarService.getDealerSaleStat(condition);

        // Sort the saleStats by date in descending order
        saleStats.sort((a, b) -> b.getCalendarDate().compareTo(a.getCalendarDate()));

        List<MemberAdminListDto> memberDealerLists = memberAdminService.searchAllMemberDealer(MemberRole.DEALER.getRole());

        // Create a map to store stats for each date and counselor
        Map<LocalDate, Map<Long, int[]>> groupedStats = new LinkedHashMap<>();

        // Initialize the map with all dates and counselors
        saleStats.stream().map(StatDto::getCalendarDate).distinct().forEach(date -> {
            Map<Long, int[]> dealerStats = new HashMap<>();
            memberDealerLists.forEach(dear ->
                    dealerStats.put(dear.getId(), new int[]{0, 0})
            );
            groupedStats.put(date, dealerStats);
        });

        // Fill in the actual stats
        for (StatDto stat : saleStats) {
            Map<Long, int[]> dateStats = groupedStats.get(stat.getCalendarDate());
            if (dateStats == null) {
                dateStats = new HashMap<>();
                groupedStats.put(stat.getCalendarDate(), dateStats);
            }

            int[] counselorStat = dateStats.get(stat.getId());
            if (counselorStat == null) {
                counselorStat = new int[2];
                dateStats.put(stat.getId(), counselorStat);
            }

            counselorStat[0] = stat.getCompletedCounsel();
            counselorStat[1] = stat.getCompletedTrade();
        }

        // Calculate totals for each counselor
        Map<Long, int[]> dealerTotals = new HashMap<>();
        memberDealerLists.forEach(dear -> {
            int totalCompletedCounsel = saleStats.stream()
                    .filter(stat -> stat.getId().equals(dear.getId()))
                    .mapToInt(StatDto::getCompletedCounsel)
                    .sum();
            int totalCompletedTrade = saleStats.stream()
                    .filter(stat -> stat.getId().equals(dear.getId()))
                    .mapToInt(StatDto::getCompletedTrade)
                    .sum();
            dealerTotals.put(dear.getId(), new int[]{totalCompletedCounsel, totalCompletedTrade});
        });

        model.addAttribute("groupedStats", groupedStats);
        model.addAttribute("condition", condition);
        model.addAttribute("memberDealerLists", memberDealerLists);
        model.addAttribute("dealerTotals", dealerTotals);

        return "stats/stats_dealer_sale";
    }


    /**
     * 관리자 | 통계 > 상담현황 > 구매
     *
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 상담원 > 구매",
            description = "내차 구매 상담원"
    )
    @GetMapping("/admin/stats/consult/purchase")
    public String searchConsultForPurchase(StatSearchDto condition, Model model) {

        // Pass the status list to your service layer to filter results
        List<ConsultDto> consultData = calendarService.searchConsultForPurchase(condition);
        ConsultSummaryDto summary = calendarService.summarizeConsultData(consultData);

        model.addAttribute("consultData", consultData);
        model.addAttribute("summary", summary);
        model.addAttribute("condition", condition);  // 검색 조건도 뷰에 전달

        return "stats/stats_consult_purchase";
    }

    /**
     * 관리자 | 통계 > 상담현황 > 판매
     *
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 상담원 > 판매",
            description = "내차 판매 상담원"
    )
    @GetMapping("/admin/stats/consult/sale")
    public String searchConsultForSale(StatSearchDto condition, Model model) {

        List<ConsultDto> consultData = calendarService.searchConsultForSale(condition);
        ConsultSummaryDto summary = calendarService.summarizeConsultData(consultData);

        model.addAttribute("consultData", consultData);
        model.addAttribute("summary", summary);
        model.addAttribute("condition", condition);  // 검색 조건도 뷰에 전달

        return "stats/stats_consult_sale";
    }

    /**
     * 관리자 | 통계 > 상담현황 > 판매 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 상담현황 > 구입 > 엑셀다운",
            description = "내차 구입 상담원 엑셀다운"
    )
    @GetMapping("/admin/stats/consult/purchase/excel")
    public void searchConsultPurchaseStatExcel(StatSearchDto condition, HttpServletResponse response) throws IOException {

        Workbook wb = calendarService.searchConsultPurchaseExcel(condition);

        String fileName = URLEncoder.encode("상담현황(내차 구입).xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }

    /**
     * 관리자 | 통계 > 상담현황 > 구입 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 통계 > 상담현황 > 팔기 > 엑셀다운",
            description = "내차 팔기 상담현황 엑셀다운"
    )
    @GetMapping("/admin/stats/consult/sale/excel")
    public void searchConsultSaleStatExcel(StatSearchDto condition, HttpServletResponse response) throws IOException {

        Workbook wb = calendarService.searchConsultSaleExcel(condition);

        String fileName = URLEncoder.encode("상담현황(내차 팔기).xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }
}
