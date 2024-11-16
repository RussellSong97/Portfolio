package com.cuv.admin.web.controller.sale;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.inquirydetail.InquiryDetailService;
import com.cuv.admin.domain.inquirydetail.dto.InquiryDetailListDto;
import com.cuv.admin.domain.inquirydetail.enumset.TradeType;
import com.cuv.admin.domain.member.enumset.MemberRole;
import com.cuv.admin.domain.memberadmin.MemberAdminService;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminListDto;
import com.cuv.admin.domain.saleinquiry.SaleInquiryService;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquiryDetailDto;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquiryListDto;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquirySaveDto;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquirySearchDto;
import com.cuv.admin.domain.salevehicle.dto.SaleVehicleDetailDto;
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
        name = "관리자 -> 내 차 팔기",
        description = "관리자 -> 내 차 팔기"
)
@Controller
@RequiredArgsConstructor
public class SaleInquiryController {

    private final SaleInquiryService saleInquiryService;

    private final MemberAdminService memberAdminService;

    private final InquiryDetailService inquiryDetailService;

    /**
     * 관리자 | 내 차 팔기 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 팔기 > 목록",
            description = "관리자 내 차 팔기 목록"
    )
    @GetMapping("/admin/sale/inquiry")
    public String adminSaleInquiryList(SaleInquirySearchDto condition, Model model) {
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

        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by(direction, orderColumn));
        Page<SaleInquiryListDto> saleInquiryLists = saleInquiryService.searchAllSaleInquiryLists(condition, request);

        // 검색 조건 - 딜러
        List<MemberAdminListDto> memberDealerLists = memberAdminService.searchAllMemberDealer(MemberRole.DEALER.getRole());

        model.addAttribute("condition", condition);
        model.addAttribute("saleInquiryLists", saleInquiryLists);
        model.addAttribute("memberDealerLists", memberDealerLists);

        return "sale/sale_list";
    }

    /**
     * 관리자 | 내 차 팔기 > 삭제
     *
     * @param idList 삭제할 시퀀스 값의 배열
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 팔기 > 삭제",
            description = "관리자 내 차 팔기 삭제"
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
    @DeleteMapping("/admin/sale/inquiry/delete")
    @ResponseBody
    public JSONResponse<?> adminSaleInquiryDeleteProc(@RequestParam("id[]") List<Long> idList) {
        try {
            saleInquiryService.adminSaleInquiryDeleteProc(idList);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", idList);
    }

    /**
     * 관리자 | 내 차 팔기 > 목록 > 차량 팝업
     *
     * @param id 판매 차량 시퀀스
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 팔기 > 목록 > 차량 팝업",
            description = "관리자 내 차 팔기 차량 팝업"
    )
    @GetMapping("/admin/sale/vehicle/{id}")
    public String adminSaleVehicle(@PathVariable("id") Long id, Model model) {
        SaleVehicleDetailDto saleVehicle = saleInquiryService.searchSaleVehicleById(id);

        model.addAttribute("saleVehicle", saleVehicle);

        return "popup/vehicle_view";
    }

    /**
     * 관리자 | 내 차 팔기 > 상세
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 팔기 > 상세",
            description = "관리자 내 차 팔기 상세"
    )
    @GetMapping("/admin/sale/inquiry/{id}")
    public String adminSaleInquiryDetail(@PathVariable("id") Long id, Model model) {
        // 상담 기록
        List<InquiryDetailListDto> inquiryDetailLists = inquiryDetailService.searchAllInquiryDetailListsByTradeType(id, TradeType.SELL.getCode());

        // 문의 내용
        SaleInquiryDetailDto saleInquiry = saleInquiryService.searchSaleInquiryById(id);

        // 배정 딜러
        List<MemberAdminListDto> memberDealerLists = memberAdminService.searchAllMemberDealer(MemberRole.DEALER.getRole());

        model.addAttribute("inquiryDetailLists", inquiryDetailLists);
        model.addAttribute("saleInquiry", saleInquiry);
        model.addAttribute("memberDealerLists", memberDealerLists);

        return "sale/sale_view";
    }

    /**
     * 관리자 | 내 차 팔기 > 상세 > 저장
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 내 차 팔기 > 상세 > 저장",
            description = "관리자 내 차 팔기 저장"
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
    @PostMapping("/admin/sale/inquiry/edit")
    @ResponseBody
    public JSONResponse<?> adminSaleInquiryEditProc(SaleInquirySaveDto requestDto) {
        try {
            saleInquiryService.adminSaleInquiryEditProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    /**
     * 관리자 | 내 차 팔기 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author Sangbin
     */
    @Operation(
            summary = "관리자 | 내 차 팔기 > 엑셀",
            description = "관리자 내 차 팔기 엑셀"
    )
    @GetMapping("/admin/sale/all/excel")
    public void adminSaleAllExcel(SaleInquirySearchDto condition, HttpServletResponse response) throws IOException {
        Workbook wb = saleInquiryService.searchAllSaleInquiryExcelLists(condition);

        String fileName = URLEncoder.encode("내 차 팔기.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 파일명과 콘텐츠 타입 지정
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //엑셀 출력
        wb.write(response.getOutputStream());
        wb.close();
    }
}
