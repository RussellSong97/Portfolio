package com.cuv.web.controller.main;

import com.cuv.common.JSONResponse;
import com.cuv.domain.boardnotice.BoardNoticeService;
import com.cuv.domain.boardnotice.dto.BoardNoticeDetailDto;
import com.cuv.domain.boardreview.BoardReviewService;
import com.cuv.domain.boardreview.dto.BoardReviewListDto;
import com.cuv.domain.exhibition.ExhibitionService;
import com.cuv.domain.exhibition.dto.ExhibitionListDto;
import com.cuv.domain.exhibition.enumset.ExhibitionType;
import com.cuv.domain.firebase.FcmService;
import com.cuv.domain.linkcode.LinkCodeService;
import com.cuv.domain.member.MemberService;
import com.cuv.domain.notification.NotificationService;
import com.cuv.domain.pick.PickService;
import com.cuv.domain.popularkeyword.PopularKeywordService;
import com.cuv.domain.popularkeyword.dto.PopularKeywordListDto;
import com.cuv.domain.product.ProductService;
import com.cuv.domain.product.dto.ProductBestValueRecommendListDto;
import com.cuv.domain.product.dto.ProductCuvRecommendListDto;
import com.cuv.domain.product.dto.ProductHitCarListDto;
import com.cuv.domain.product.dto.ProductRecentCarListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Tag(
        name = "사용자 - 메인",
        description = "사용자 - 메인"
)
@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;
    private final PopularKeywordService popularKeywordService;
    private final ExhibitionService exhibitionService;
    private final BoardNoticeService boardNoticeService;
    private final BoardReviewService boardReviewService;
    private final LinkCodeService linkCodeService;
    private final FcmService fcmService;
    private final PickService pickService;
    private final NotificationService notificationService;
    private final MemberService memberService;

    /**
     * 메인 리다이렉트
     *
     * @author 송다운
     */
    @Operation(
            summary = "사용자 - 메인",
            description = "메인 리다이렉트"
    )
    @RequestMapping(value = {"", "/", "/main"})
    public String userMainRedirect() {
        return "redirect:/main";
    }

    /**
     * 메인
     * @author SungHa
     */
    @Operation(
            summary = "사용자 - 메인",
            description = "메인"
    )
    @GetMapping(value = "/")
    public String home(Model model) {
        // 판매 차량 개수
//        Long productCount = productService.searchProductCount();

        // 파이어베이스 실행(토큰 발급 위해)
        fcmService.init();

        // 샵 검색어
        List<PopularKeywordListDto> popularKeywordLists = popularKeywordService.searchAllPopularKeyword();

        // 메인 탑 배너
        List<ExhibitionListDto> mainTopBannerLists = exhibitionService.searchAllExhibition(ExhibitionType.MAIN_TOP.getCode());

        // 메인 서브 배너
        List<ExhibitionListDto> mainSubBannerLists = exhibitionService.searchAllExhibition(ExhibitionType.MAIN_SUB.getCode());

        // 이벤트
        List<ExhibitionListDto> eventLists = exhibitionService.searchAllExhibition(ExhibitionType.EVENT.getCode());

        // 팝업
        List<ExhibitionListDto> popupLists = exhibitionService.searchAllExhibition(ExhibitionType.POPUP.getCode());

        // 제휴금융사
        List<ExhibitionListDto> financialList = exhibitionService.searchAllExhibition(ExhibitionType.FINANCIAL.getCode());

        // 공지사항
        BoardNoticeDetailDto boardNotice = boardNoticeService.searchBoardNoticeByMain();

        //실시간 인기 차량
        List<ProductHitCarListDto> hitsCarList = productService.searchProductHitCarList();

        //올라 온 지 얼마 안된 매물
        List<ProductRecentCarListDto> recentCarList = productService.searchProductRecentCarList();

        //카통령 추천 차량
        List<ProductCuvRecommendListDto> cuvRecommendList = productService.searchProductCuvRecommendList();

        //가성비 추천 차량
        List<ProductBestValueRecommendListDto> bestValueRecommendList = productService.searchProductBestValueRecommendList();

        // 구매후기
        List<BoardReviewListDto> reviewList = boardReviewService.searchAllReviewNopageList();


//        model.addAttribute("productCount", productCount);
        model.addAttribute("popularKeywordLists", popularKeywordLists);
        model.addAttribute("mainTopBannerLists", mainTopBannerLists);
        model.addAttribute("mainSubBannerLists", mainSubBannerLists);
        model.addAttribute("eventLists", eventLists);
        model.addAttribute("popupLists", popupLists);
        model.addAttribute("financialList", financialList);
        model.addAttribute("boardNotice", boardNotice);
        model.addAttribute("hitsCarList", hitsCarList);
        model.addAttribute("recentCarList", recentCarList);
        model.addAttribute("cuvRecommendList", cuvRecommendList);
        model.addAttribute("bestValueRecommendList", bestValueRecommendList);
        model.addAttribute("reviewList", reviewList);

        return "main/index";
    }

    /**
     * 사용자 메인 차량 제조사,모델,세부모델
     * @param map 제조사,모델,세부 모델 값
     * @return
     */
    @Operation(
            summary = "사용자 - 메인",
            description = "사용자 메인 차량 제조사,모델,세부모델"
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
    @PostMapping("/api/search/main/category")
    @ResponseBody
    public JSONResponse<?> searchMainCategory(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = linkCodeService.searchApiMainCategory(map);
        } catch (Exception e) {
            return new JSONResponse<>(500, "FALSE");
        }
        return response;
    }

    /**
     * 찜 여부 확인
     * @param authentication 로그인 정보
     * @return
     */
    @Operation(
            summary = "사용자 - 헤더",
            description = "찜 여부 확인"
    )
    @PostMapping("/api/pick/check")
    @ResponseBody
    public Long apiPickCheck(Authentication authentication) {
        Long response;
        try {
            response = pickService.searchPicksMemberIdReadYn(authentication);
        } catch (Exception e) {
            return 0L;
        }
        return response;
    }

    /**
     * 알름 여부 확인
     * @param authentication 로그인 정보
     * @return
     */
    @Operation(
            summary = "사용자 - 헤더",
            description = "알름 여부 확인"
    )
    @PostMapping("/api/notification/check")
    @ResponseBody
    public Long apiNotificationCheck(Authentication authentication) {
        Long response;
        try {
            response = notificationService.searchNotificationsMemberIdReadYn(authentication);
        } catch (Exception e) {
            return 0L;
        }
        return response;
    }

    /**
     * 파이어 베이스 토큰 저장 (푸쉬 알림)
     * @param map 푸쉬 알림 토큰
     * @return
     */
    @Operation(
            summary = "사용자 - 메인",
            description = "파이어 베이스 토큰 저장 (푸쉬 알림)"
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
    @PostMapping("/token/save")
    @ResponseBody
    public JSONResponse<?> saveToken (@RequestBody Map<String, Object> map){
        JSONResponse<?> response;
        try {
            response = memberService.saveToken(map);
        }catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }
}
