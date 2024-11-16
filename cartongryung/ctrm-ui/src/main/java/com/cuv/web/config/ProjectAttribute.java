package com.cuv.web.config;

import com.cuv.domain.exhibition.ExhibitionService;
import com.cuv.domain.exhibition.dto.ExhibitionListDto;
import com.cuv.domain.exhibition.enumset.ExhibitionType;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.notification.NotificationService;
import com.cuv.domain.pick.PickService;
import com.cuv.domain.productviewshistory.ProductViewsHistoryService;
import com.cuv.domain.productviewshistory.dto.ProductViewsHistoryRecentCarListDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * 프로젝트 공통 속성
 */
@ControllerAdvice
@RequiredArgsConstructor
public class ProjectAttribute {

    private final ExhibitionService exhibitionService;
    private final ProductViewsHistoryService productViewsHistoryService;


    /**
     * 타임리프 URI 접근용
     *
     * @author SungHa
     */
    @ModelAttribute("requestValue")
    public HttpServletRequest getServletRequest(HttpServletRequest request) {
        return request;
    }

    /**
     * 타임리프 페이징
     *
     * @author SungHa
     */
    @ModelAttribute("urlBuilder")
    public ServletUriComponentsBuilder getServletUriComponentsBuilder(ServletUriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder;
    }

    /**
     * 헤더 > 상단 띠 배너
     *
     * @author SungHa
     */
    @ModelAttribute("topStripBanner")
    public List<ExhibitionListDto> getTopStripBanner() {
        return exhibitionService.searchAllExhibition(ExhibitionType.TOP_STRIP.getCode());
    }

    /**
     * aside > 최근 본 차량
     * @author 이상빈
     */
    @ModelAttribute("productViewsHistory")
    public List<ProductViewsHistoryRecentCarListDto> searchProductViewsHistory() {
        return productViewsHistoryService.searchProductViewsHistoryRecentCarList();

    }

}
