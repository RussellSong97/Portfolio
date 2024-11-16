package com.cuv.domain.pick;

import com.cuv.common.JSONResponse;
import com.cuv.common.YN;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.pick.dto.PickCountDto;
import com.cuv.domain.pick.dto.PickListDto;
import com.cuv.domain.pick.entity.Pick;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 찜 목록 조회, 수정, 삭제
 */
@Service
@RequiredArgsConstructor
public class PickService {
    private final MemberRepository memberRepository;

    private final PickRepository pickRepository;

    /**
     * 찜 | memberId로 찜 가져오기
     *
     * @author 박성민
     */
    public List<Pick> getPicks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email, MemberStatus.NORMAL); // 이메일로 맴버 객체 가져옴

        Long memberId = member.getId();

        return pickRepository.getPicksById(memberId);
    }

    /**
     * 찜 | getPicks메소드로 가져온 pick들의 readYn 변경
     *
     * @author 박성민
     */
    public void updateReadYn(List<PickListDto> picks) {
        for (PickListDto pickDto : picks) {
            Pick pick = pickRepository.findPickById(pickDto.getPickId());
            pick.setReadYn(YN.Y);
            pickRepository.save(pick);
        }
    }

    /**
     * 찜 | 찜 디비에 저장한 거 및 아이템 정보 등을 memberId로 가져오기
     *
     * @author 박성민
     */
    public List<PickListDto> searchPickListNoPageList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email,MemberStatus.NORMAL); // 이메일로 맴버 객체 가져옴

        Long memberId = member.getId();

        return pickRepository.searchPickListNoPageList(memberId);
    }

    /**
     * 찜 | 찜 디비에 저장한 거 pickId들로 제거
     *
     * @author 박성민
     */
    public void updateDelYn(Long pickId) {

        Pick pick = pickRepository.findPickById(pickId);
        pick.setDelYn(YN.Y);
        pickRepository.save(pick);
    }

    public Long searchPickReadYnCount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email,MemberStatus.NORMAL); // 이메일로 맴버 객체 가져옴

        Long memberId = member.getId();

        return pickRepository.countByMemberIdAndReadYn(memberId, YN.N);
    }

    /**
     * 찜 여부 확인 용
     *
     * @author 송다운
     */
    public Long searchPicksMemberIdReadYn(Authentication authentication) {
        if (authentication == null) {
            return 0L;
        } else {
            String loginId = authentication.getName();
            Member member = memberRepository.findByLoginIdAndStatusCode(loginId,MemberStatus.NORMAL);

            return pickRepository.searchPicksMemberIdReadYn(member.getId());
        }
    }

    /**
     * 찜 개수
     *
     * @author 송다운
     */
    public Long searchPicksMemberIdCount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return 0L;
        } else {
            String loginId = auth.getName();
            Member member = memberRepository.findByLoginIdAndStatusCode(loginId,MemberStatus.NORMAL);

            return pickRepository.searchPicksMemberIdCount(member.getId());
        }
    }

    /**
          * 찜 | 찜 디비 저장 및 찜 취소
          *
          * @author 송다운
          */
    public JSONResponse<?> insertPickV2(Map<String, Object> map, Authentication authentication) {
        if (authentication == null) {
            return new JSONResponse<>(500, "로그인이 필요한 기능입니다.");
        }

        Long productId;
        try {
            productId = Long.parseLong((String) map.get("productId"));
        } catch (NumberFormatException e) {
            return new JSONResponse<>(500, "Invalid productId format");
        }

        Member member;
        try {
            member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(),MemberStatus.NORMAL);
        } catch (Exception e) {
            return new JSONResponse<>(500, "회원 정보 조회 중 오류 발생");
        }

        List<Pick> existingPicks = pickRepository.findByMemberIdAndProductId(member.getId(), productId);

        if (existingPicks != null && !existingPicks.isEmpty()) {
            for (Pick pick : existingPicks) {
                if (pick.getDelYn() == YN.Y) {
                    pick.setDelYn(YN.N);
                    pick.setReadYn(YN.N);
                } else {
                    pick.setDelYn(YN.Y);
                }

                try {
                    pickRepository.save(pick);
                } catch (Exception e) {
                    return new JSONResponse<>(500, "기존 pick 데이터 업데이트 중 에러: " + e.getMessage());
                }
            }
        } else {
            Pick newPick = Pick.builder()
                    .memberId(member.getId())
                    .productId(productId)
                    .readYn(YN.N)
                    .delYn(YN.N)
                    .build();
            try {
                pickRepository.save(newPick);
            } catch (Exception e) {
                return new JSONResponse<>(500, "pick 테이블 insert 중 에러: " + e.getMessage());
            }

        }

        return new JSONResponse<>(200, "SUCCESS");
    }

    public List<Long> getProductIdOfPick() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email, MemberStatus.NORMAL); // 이메일로 맴버 객체 가져옴

        if (member == null) {
            return Collections.emptyList();
        }

        Long memberId = member.getId();
        return pickRepository.findPickedProductIdsByMemberId(memberId);
    }


    public List<Long> getDelYNOfPick(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email, MemberStatus.NORMAL); // 이메일로 맴버 객체 가져옴

        Long memberId = member.getId();

        return pickRepository.findDelYNByMemberId(memberId, productId);
    }

    public PickCountDto searchPickSummary() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email, MemberStatus.NORMAL);

        if (member == null)
            return new PickCountDto(Collections.emptyList(), 0L, 0L);

        return pickRepository.searchPickSummary(member.getId());
    }

}
