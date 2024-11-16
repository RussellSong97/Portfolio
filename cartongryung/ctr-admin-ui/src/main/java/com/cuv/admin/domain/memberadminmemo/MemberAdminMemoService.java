package com.cuv.admin.domain.memberadminmemo;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.member.dto.MemberAdminMemoListDto;
import com.cuv.admin.domain.memberadminmemo.entity.MemberAdminMemo;
import com.cuv.admin.web.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 회원 메모 (저장, 조회)
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberAdminMemoService {
    private final MemberAdminMemoRepository memberAdminMemoRepository;


    /**
     * 관리자 | 회원상세 -> 회원 메모 리스트
     * @param id 회원 아이디
     * @return
     * @author 송다운
     */
    public List<MemberAdminMemoListDto> searchMemberAdminMemoList(Long id) {
        return memberAdminMemoRepository.searchMemberAdminMemoList(id);
    }


    /**
     * 관리자 | 회원상세 -> 회원 메모 저장
     * @param map 회원 메모 정보
     * @param principalDetails 로그인 정보
     * @return
     * @author 송다운
     */
    public JSONResponse<?> saveMemberAdminMemo(Map<String, Object> map, PrincipalDetails principalDetails) {
        String content = map.get("content").toString();
        Long memberId = Long.parseLong(map.get("memberId").toString());
        Long memberAdminId = principalDetails.getUser().getId();
        MemberAdminMemo memberAdminMemo = MemberAdminMemo.builder()
                .content(content)
                .memberId(memberId)
                .memberAdminId(memberAdminId)
                .build();

        memberAdminMemoRepository.save(memberAdminMemo);
        return new JSONResponse<>(200, "SUCCESS");
    }
}
