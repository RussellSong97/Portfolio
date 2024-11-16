package com.cuv.web.controller.mypage;

import com.cuv.common.JSONResponse;
import com.cuv.domain.member.MemberService;
import com.cuv.domain.member.dto.MemberInfoDto;
import com.cuv.domain.pick.PickService;
import com.cuv.domain.pick.dto.PickListDto;
import com.cuv.domain.pick.entity.Pick;
import com.cuv.domain.productviewshistory.ProductViewsHistoryService;
import com.cuv.domain.productviewshistory.dto.ProductViewsHistoryMyPageRecentCarListDto;
import com.cuv.domain.purchaseinquiry.PurchaseInquiryService;
import com.cuv.domain.purchaseinquiry.dto.PurchaseInquiryListDto;
import com.cuv.domain.saleinquiry.SaleInquiryService;
import com.cuv.domain.saleinquiry.dto.SaleInquiryListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MypageController {

    private final PickService pickService;
    private final PurchaseInquiryService purchaseInquiryService;
    private final SaleInquiryService saleInquiryService;
    private final MemberService memberService;
    private final ProductViewsHistoryService productViewsHistoryService;

    /**
     * 햄버거 메뉴 > 찜, 최근, 상담, 방문, 내 차 목록 수
     *
     * @author SungHa
     */
    @GetMapping("/mypage/info")
    @ResponseBody
    public JSONResponse<?> getMyInfo() {
        Map<String, Long> myInfo = new HashMap<>();

        // 찜
        Long pickCount = pickService.searchPicksMemberIdCount();

        // 최근
        Long recentCount = productViewsHistoryService.searchProductViewsHistoryMyPageRecentCarCountByMemberId();

        // 상담
        Long inquiryCount = purchaseInquiryService.searchPurchaseInquiryCountByMemberId();

        // 구매 - 방문
        Long purchaseVisitCount = purchaseInquiryService.searchVisitReservationCountByMemberId();

        // 판매 - 방문, 내 차 목록
        Long saleVisitCount = saleInquiryService.searchSaleInquiryCountByMemberId();

        myInfo.put("pickCount", pickCount);
        myInfo.put("recentCount", recentCount);
        myInfo.put("inquiryCount", inquiryCount);
        myInfo.put("visitCount", purchaseVisitCount + saleVisitCount);
        myInfo.put("vehicleCount", saleVisitCount);

        return new JSONResponse<>(200, "SUCCESS", myInfo);
    }

    /**
     * 내정보 수정 페이지
     * @param userDetails
     * @param model
     * @return
     */
    @GetMapping("/mypage/myinfo")
    public String mypageMyInfo(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        MemberInfoDto dto = memberService.searchMemberInfo(userDetails.getUsername());
        model.addAttribute("info", dto);
        return "sub/myinfo";
    }

    /**
     * 탈퇴 페이지
     * @return
     */
    @GetMapping("/mypage/withdrawal")
    public String mypageWithdraw() {

        return "sub/withdrawal";
    }

    /**
     * 회원탈퇴 처리
     * @param userDetails
     * @param map
     * @return
     */
    @PostMapping("/api/member/withdrawal")
    @ResponseBody
    public JSONResponse<?> apiMemberWithdrawal(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = memberService.saveMemberWithdrawal(userDetails.getUsername(), map);
        } catch (Exception e) {
            return new JSONResponse<>(500, "회원탈퇴에 실패하였습니다.");
        }

        return response;
    }

    @GetMapping("/mypage/password/reset")
    public String mypagePasswordReset() {

        return "sub/password_reset";
    }

    /**
     * 읽음 처리하기 후
     * 픽 및 문건 정보 등 가져오기
     *
     * @author 박성민
     */
    @GetMapping("/mypage/pick")
    public String apiProductPickList(Model model) {
        // 가져오기 1
        List<PickListDto> pickList = pickService.searchPickListNoPageList();

        // 반복문으로 읽음 처리로 바꾸기
        pickService.updateReadYn(pickList);


        model.addAttribute("pickList", pickList);

        return "sub/wish_list";
    }

    /**
     * 더보기 > 상담
     *
     * @author SungHa
     */
    @GetMapping("/mypage/inquiry")
    public String myPageInquiry(Model model) {
        List<PurchaseInquiryListDto> purchaseInquiryLists = purchaseInquiryService.searchPurchaseInquiryByMemberId();

        model.addAttribute("purchaseInquiryLists", purchaseInquiryLists);

        return "sub/inquiry_list";
    }

    /**
     * 더보기 > 내 차 살 때
     *
     * @author SungHa
     */
    @GetMapping("/mypage/purchase")
    public String myPagePurchase(Model model) {
        List<PurchaseInquiryListDto> purchaseInquiryLists = purchaseInquiryService.searchVisitReservationByMemberId();

        model.addAttribute("purchaseInquiryLists", purchaseInquiryLists);

        return "sub/visit_purchase";
    }

    /**
     * 더보기 > 방문 > 내 차 팔 때
     *
     * @author SungHa
     */
    @GetMapping("/mypage/sale")
    public String myPageSale(Model model) {
        List<SaleInquiryListDto> saleInquiryLists = saleInquiryService.searchAllSaleInquiryLists();

        model.addAttribute("saleInquiryLists", saleInquiryLists);

        return "sub/visit_sale";
    }

    /**
     * 마이페이지 비밀번호 변경
     * @param map
     * @param userDetails
     * @return
     */
    @PostMapping("/mypage/password/reset")
    @ResponseBody
    public JSONResponse<?> mypagePasswordReset(@RequestBody Map<String, Object> map,@AuthenticationPrincipal UserDetails userDetails) {
        JSONResponse<?> response;
        try {
            response = memberService.searchMypageFindPassword(map, userDetails);
        } catch (Exception e) {
            return new JSONResponse<>(500, "FALSE");
        }
        return response;
    }

    /**
     * 더보기 > 최근 본 차량
     *
     * @author SangBin
     */
    @GetMapping("/mypage/recent")
    public String myPageRecent(Model model) {
        List<ProductViewsHistoryMyPageRecentCarListDto> myRecentCarLists = productViewsHistoryService.searchProductViewsHistoryMyPageRecentCarList();

        model.addAttribute("myRecentCarLists", myRecentCarLists);

        return "sub/recent_list";
    }
}
