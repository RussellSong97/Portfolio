package com.cuv.domain.productviewshistory;

import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.productviewshistory.dto.ProductViewsHistoryMyPageRecentCarListDto;
import com.cuv.domain.productviewshistory.dto.ProductViewsHistoryRecentCarListDto;
import com.cuv.domain.productviewshistory.entity.ProductViewsHistory;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 최근 본 차량 조회, 저장, 삭제
 */
@Service
@RequiredArgsConstructor
@SessionScope
public class ProductViewsHistoryService {
    private final MemberRepository memberRepository;

    private final ProductViewsHistoryRepository productViewsHistoryRepository;

    private final HttpSession session;

    /**
     * 세션에서 최근 본 상품 조회
     *
     * @author Sangbin
     */
    public List<ProductViewsHistoryRecentCarListDto> getRecentCarsFromSession() {
        List<Long> recentCarIds = (List<Long>) session.getAttribute("recentCars");

        if (recentCarIds == null || recentCarIds.isEmpty()) {
            return Collections.emptyList();
        }

        return productViewsHistoryRepository.searchProductViewsHistoryRecentCarListByIds(recentCarIds);
    }

    /**
     * 최근 본 상품 조회 (로그인 상태 및 미로그인 상태 모두에서 호출)
     *
     * @author Sangbin
     */
    public List<ProductViewsHistoryRecentCarListDto> searchProductViewsHistoryRecentCarList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(),MemberStatus.NORMAL);

            if (member != null) {
                return productViewsHistoryRepository.searchProductViewsHistoryRecentCarList(member.getId());
            }
        }
        return getRecentCarsFromSession();
    }

    /**
     * 마이페이지 > 최근본차량 목록
     *
     * @author Sangbin
     */
    public List<ProductViewsHistoryMyPageRecentCarListDto> searchProductViewsHistoryMyPageRecentCarList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(),MemberStatus.NORMAL);

        return productViewsHistoryRepository.searchProductViewsHistoryMyPageRecentCarListByMemberId(member.getId());
    }

    /**
     * 햄버거 메뉴 > 최근본차량 수
     *
     * @author Sangbin
     */
    public Long searchProductViewsHistoryMyPageRecentCarCountByMemberId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return 0L;
        } else {
            String loginId = auth.getName();
            Member member = memberRepository.findByLoginIdAndStatusCode(loginId,MemberStatus.NORMAL);

            return productViewsHistoryRepository.searchProductViewsHistoryMyPageRecentCarCountByMemberId(member.getId());
        }
    }

    /**
     * 비로그인 상태에서 세션에 최근 본 상품 추가
     *
     * @author Sangbin
     */
    public void addRecentCarToSession(Long id) {
        List<Long> recentCars = (List<Long>) session.getAttribute("recentCars");

        if (recentCars == null) {
            recentCars = new ArrayList<>();
        }

        if (!recentCars.contains(id)) {
            recentCars.add(id);
            session.setAttribute("recentCars", recentCars);
        }
    }

    /**
     * 로그인 상태에서 productViewsHistory 테이블에 최근 본 상품 추가
     *
     * @author Sangbin
     */
    public void saveProductViewsHistory(Long id, UserDetails userDetails) {
        if (userDetails == null) {
            return;
        }

        Member member = memberRepository.findByLoginIdAndStatusCode(userDetails.getUsername(), MemberStatus.NORMAL);
        if (member == null) {
            return;
        }

        // 해당 멤버가 이미 이 상품을 본 이력이 있는지 확인
        boolean alreadyViewed = productViewsHistoryRepository.existsByProductIdAndMemberId(id, member.getId());

        if (!alreadyViewed) {
            ProductViewsHistory productViewsHistory = ProductViewsHistory.builder()
                    .productId(id)
                    .memberId(member.getId())
                    .build();

            try {
                productViewsHistoryRepository.save(productViewsHistory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 비로그인 상태에서 최근본차량 삭제
     *
     * @author Sangbin
     */
    public void deleteRecentCarFromSession(HttpSession session, Long id) {
        List<Long> recentCars = (List<Long>) session.getAttribute("recentCars");

        if (recentCars != null) {
            recentCars.remove(id);
            session.setAttribute("recentCars", recentCars);
        }
    }

    /**
     * 매일 자정에 생성된 지 14일 된 최근 본 차량 데이터 삭제 실행
     *
     * @author Sangbin
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteOldProductViewsHistory() {
        LocalDateTime dateToDelete = LocalDateTime.now().minusDays(14); // 현재 시각으로부터 14일 이전 시각 계산
        productViewsHistoryRepository.deleteOldRecords(dateToDelete);
    }
}
