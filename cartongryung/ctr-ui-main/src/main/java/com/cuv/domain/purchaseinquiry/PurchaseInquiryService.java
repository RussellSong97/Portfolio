package com.cuv.domain.purchaseinquiry;

import com.cuv.domain.bizgo.AlimTalkService;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.purchaseinquiry.dto.PurchaseInquiryListDto;
import com.cuv.domain.purchaseinquiry.dto.PurchaseInquirySaveDto;
import com.cuv.domain.purchaseinquiry.entity.PurchaseInquiry;
import com.cuv.domain.purchaseinquiry.enumset.ConsultationType;
import com.cuv.domain.purchaseinquiry.enumset.InquiryType;
import com.cuv.domain.wishlist.WishlistRepository;
import com.cuv.domain.wishlist.entity.Wishlist;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class PurchaseInquiryService {

    private final PurchaseInquiryRepository purchaseInquiryRepository;
    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final AlimTalkService alimTalkService;

    /**
     * 상품 상세 > 방문 상담 예약
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void purchaseInquiryWriteProc(PurchaseInquirySaveDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName());

        if (!hasText(requestDto.getCalendar()) || !hasText(requestDto.getDay()))
            throw new IllegalArgumentException("상담 날짜를 선택해주세요.");

        if (!hasText(requestDto.getHour()) || !hasText(requestDto.getMinute()))
            throw new IllegalArgumentException("상담 시간을 선택해주세요.");

        // 비회원일 경우
        if (member == null) {
            if (!hasText(requestDto.getRealName()))
                throw new IllegalArgumentException("성함을 입력해주세요.");

            if (!hasText(requestDto.getMobileNumber()))
                throw new IllegalArgumentException("전화번호를 입력해주세요.");

            if (!hasText(requestDto.getUseYn()))
                throw new IllegalArgumentException("개인정보 수집 이용.동의에 체크해주세요.");

            if (!hasText(requestDto.getProvideYn()))
                throw new IllegalArgumentException("개인정보 제공 동의에 체크해주세요.");
        }

        // 방문 예약 일시 포맷팅
        LocalDateTime visitReservationAt = null;
        if (hasText(requestDto.getCalendar()) && hasText(requestDto.getDay()) && hasText(requestDto.getHour()) && hasText(requestDto.getMinute())) {
            String year = requestDto.getCalendar().split("\\.")[0];
            String month = requestDto.getCalendar().split("\\.")[1];
            String date = year + "-" + month + "-" + requestDto.getDay();
            String dateTimeString = date + "T" + requestDto.getHour() + ":" + requestDto.getMinute();
            visitReservationAt = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        }

        // 구매 상담 정보
        PurchaseInquiry purchaseInquiry = PurchaseInquiry.builder()
                .memberId(member != null ? member.getId() : null)
                .inquiryTypeCode(InquiryType.VISIT_RESERVATION)
                .consultationTypeCode(ConsultationType.VISIT)
                .realName(requestDto.getRealName())
                .mobileNumber(requestDto.getMobileNumber())
                .visitReservationAt(visitReservationAt)
                .build();

        purchaseInquiryRepository.save(purchaseInquiry);

        // 구매 희망 차량
        Wishlist wishlist = Wishlist.builder()
                .purchaseInquiryId(purchaseInquiry.getId())
                .productId(requestDto.getProductId())
                .build();

        wishlistRepository.save(wishlist);

        // 방문예약 완료 후 알림톡 전송
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("#{day}", visitReservationAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        // 알림톡 전송
        alimTalkService.sendAlimTalk("code04", placeholders, requestDto.getMobileNumber());
    }

    /**
     * 햄버거 메뉴 > 상담 수
     *
     * @author SungHa
     */
    public Long searchPurchaseInquiryCountByMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName());

        return purchaseInquiryRepository.searchPurchaseInquiryCountByMemberId(member.getId());
    }

    /**
     * 햄버거 메뉴 > 구매문의 - 방문 예약 수
     *
     * @author SungHa
     */
    public Long searchVisitReservationCountByMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName());

        return purchaseInquiryRepository.searchVisitReservationCountByMemberId(member.getId());
    }

    /**
     * 더보기 > 상담
     *
     * @author SungHa
     */
    public List<PurchaseInquiryListDto> searchPurchaseInquiryByMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName());

        return purchaseInquiryRepository.searchPurchaseInquiryByMemberId(member.getId());
    }

    /**
     * 더보기 > 방문 > 내 차 살 때
     *
     * @author SungHa
     */
    public List<PurchaseInquiryListDto> searchVisitReservationByMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName());

        return purchaseInquiryRepository.searchVisitReservationByMemberId(member.getId());
    }

    /**
     * aside > 전화상담, 채팅상담 클릭 시 연락처 없는 문의 등록
     *
     * @param requestDto 판매 문의 정보를 담은 DTO
     * @param consultationTypeCode 상담 구분
     * @author Sangbin
     */
    @Transactional
    public void saveCallPurchaseInquiry(PurchaseInquirySaveDto requestDto, ConsultationType consultationTypeCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = null;

        if (authentication != null && authentication.isAuthenticated()) {
            member = memberRepository.findByEmail(authentication.getName());
        }

        InquiryType inquiryType;
        if (member != null) {
            inquiryType = InquiryType.CONTACTABLE;
        } else {
            inquiryType = InquiryType.WITHOUT_CONTACT;
        }

        // 판매 문의
        PurchaseInquiry purchaseInquiry = PurchaseInquiry.builder()
                .memberId(member != null ? member.getId() : null)
                .inquiryTypeCode(inquiryType)
                .consultationTypeCode(consultationTypeCode)
                .realName(requestDto.getRealName())
                .mobileNumber(requestDto.getMobileNumber())
                .connectionIp(requestDto.getConnectionIp())
                .build();

        purchaseInquiryRepository.save(purchaseInquiry);

        // 구매 희망 차량
        if (requestDto.getProductId() != null) {
            Wishlist wishlist = Wishlist.builder()
                    .purchaseInquiryId(purchaseInquiry.getId())
                    .productId(requestDto.getProductId())
                    .build();

            wishlistRepository.save(wishlist);
        }
    }
}
