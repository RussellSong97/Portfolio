package com.cuv.web.config;

import com.cuv.domain.exhibition.ExhibitionService;
import com.cuv.domain.exhibition.dto.ExhibitionListDto;
import com.cuv.domain.exhibition.enumset.ExhibitionType;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.notification.NotificationService;
import com.cuv.domain.pick.PickService;
import com.cuv.domain.productviewshistory.ProductViewsHistoryService;
import com.cuv.domain.productviewshistory.dto.ProductViewsHistoryRecentCarListDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class ProjectAttribute {

    private final ExhibitionService exhibitionService;
    private final PickService pickService;
    private final NotificationService notificationService;
    private final ProductViewsHistoryService productViewsHistoryService;
    private final MemberRepository memberRepository;


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
     * 헤더 > 찜
     *
     *  @author 송다운
     */
    @ModelAttribute("picksCheck")
    public Long searchPicksMemberIdReadYn(@AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails == null) {
            return 0L;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        if(request.getRequestURI().startsWith("/api")) return 0L;

        return pickService.searchPicksMemberIdReadYn(userDetails);
    }

    /**
     * 헤더 > 알림
     *
     *  @author 박성민
     */
    @ModelAttribute("notificationsCheck")
    public Long searchNotificationsMemberIdReadYn() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        if(request.getRequestURI().startsWith("/api")) return 0L;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return notificationService.searchNotificationsMemberIdReadYn(auth);
    }

    /**
     * aside > 최근 본 차량
     * @author 이상빈
     */
    @ModelAttribute("productViewsHistory")
    public List<ProductViewsHistoryRecentCarListDto> searchProductViewsHistory() {
        return productViewsHistoryService.searchProductViewsHistoryRecentCarList();

    }

    /**
     * 회원 이름 가져오기
     */

    @ModelAttribute("memberName")
    public String memberName(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        return memberRepository.findByEmail(userDetails.getUsername()).getName();
    }

    /**
     * 해피톡에 사용자 정보 가져오기
     *
     *  @author 이상빈
     */
    @ModelAttribute
    public void addChatAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Member member = memberRepository.findByEmail(email);
            if (member != null) {
                model.addAttribute("username", member.getRealName());
                model.addAttribute("phone", member.getMobileNumber());
                model.addAttribute("email", member.getEmail());
            }
        }
    }
}
