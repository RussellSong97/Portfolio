package com.cuv.domain.productviewshistory;

import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
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
            Member member = memberRepository.findByEmail(authentication.getName());

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
        Member member = memberRepository.findByEmail(authentication.getName());

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
            Member member = memberRepository.findByEmail(loginId);

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
            session.setAttribute("recentCars", recentCars);
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

        Member member = memberRepository.findByEmail(userDetails.getUsername());
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

    // 매일 자정에 실행되도록 설정
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteOldProductViewsHistory() {
        LocalDateTime dateToDelete = LocalDateTime.now().minusDays(14); // 현재 시각으로부터 14일 이전 시각 계산
        productViewsHistoryRepository.deleteOldRecords(dateToDelete);
    }
}
