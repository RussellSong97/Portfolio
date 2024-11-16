package com.cuv.web.controller.main;

import com.cuv.domain.boardnotice.BoardNoticeService;
import com.cuv.domain.boardnotice.dto.BoardNoticeDetailDto;
import com.cuv.domain.exhibition.ExhibitionService;
import com.cuv.domain.exhibition.dto.ExhibitionListDto;
import com.cuv.domain.exhibition.enumset.ExhibitionType;
import com.cuv.domain.linkcode.LinkCodeService;
import com.cuv.domain.linkcode.dto.LinkCodeListDto;
import com.cuv.domain.linkcode.entity.LinkCode;
import com.cuv.domain.popularkeyword.PopularKeywordService;
import com.cuv.domain.popularkeyword.dto.PopularKeywordListDto;
import com.cuv.domain.product.ProductService;
import com.cuv.domain.product.dto.ProductBestValueRecommendListDto;
import com.cuv.domain.product.dto.ProductCuvRecommendListDto;
import com.cuv.domain.product.dto.ProductRecentCarListDto;
import com.cuv.domain.review.ReviewService;
import com.cuv.domain.review.dto.ReviewListDto;
import com.cuv.domain.terms.TermsService;
import com.cuv.domain.terms.enumset.TermsType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;
    private final PopularKeywordService popularKeywordService;
    private final ExhibitionService exhibitionService;
    private final BoardNoticeService boardNoticeService;
    private final ReviewService reviewService;
    private final LinkCodeService linkCodeService;

    /**
     * 메인 리다이렉트
     *
     * @author 송다운
     */
    @RequestMapping(value = {"", "/", "/main"})
    public String userMainRedirect() {
        return "redirect:/main";
    }

    /**
     * 메인
     *
     * @author SungHa
     */
    @GetMapping(value = "/")
    public String home(Model model) {
        // 판매 차량 개수
        Long productCount = productService.searchProductCount();

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

        // 공지사항
        BoardNoticeDetailDto boardNotice = boardNoticeService.searchBoardNoticeByMain();

        //올라 온 지 얼마 안된 매물
        List<ProductRecentCarListDto> recentCarList = productService.searchProductRecentCarList();

        //카통령 추천 차량
        List<ProductCuvRecommendListDto> cuvRecommendList = productService.searchProductCuvRecommendList();

        //가성비 추천 차량
        List<ProductBestValueRecommendListDto> bestValueRecommendList = productService.searchProductBestValueRecommendList();

        // 구매후기
        List<ReviewListDto> reviewList = reviewService.searchAllReviewNopageList();

        List<LinkCodeListDto> firstCategoryList = linkCodeService.searchFirstCategoryList();

        model.addAttribute("productCount", productCount);
        model.addAttribute("popularKeywordLists", popularKeywordLists);
        model.addAttribute("mainTopBannerLists", mainTopBannerLists);
        model.addAttribute("mainSubBannerLists", mainSubBannerLists);
        model.addAttribute("eventLists", eventLists);
        model.addAttribute("popupLists", popupLists);
        model.addAttribute("boardNotice", boardNotice);
        model.addAttribute("recentCarList", recentCarList);
        model.addAttribute("cuvRecommendList", cuvRecommendList);
        model.addAttribute("bestValueRecommendList", bestValueRecommendList);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("firstCategoryList", firstCategoryList);

        return "main/index";
    }
}
