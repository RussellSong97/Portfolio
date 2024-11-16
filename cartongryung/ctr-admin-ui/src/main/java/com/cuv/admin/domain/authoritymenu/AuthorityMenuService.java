package com.cuv.admin.domain.authoritymenu;

import com.cuv.admin.domain.authoritymenu.entity.AuthorityMenu;
import com.cuv.admin.domain.memberadmin.MemberAdminRepository;
import com.cuv.admin.domain.memberadmin.entity.MemberAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 메뉴 권한 Service
 */
@Service
@RequiredArgsConstructor
public class AuthorityMenuService {

    private final AuthorityMenuRepository authorityMenuRepository;

    private final MemberAdminRepository memberAdminRepository;

    /**
     * 관리자 | 기타 > 딜러 관리, 상담사 관리 > 작성 폼, 상세 > 메뉴 권한 목록
     *
     * @author SungHa
     */
    @Transactional(readOnly = true)
    public List<AuthorityMenu> findAllAuthorityMenuByManageYn() {
        return authorityMenuRepository.findAllAuthorityMenuByManageYn();
    }

    /**
     * 관리자 | 기타 > 딜러 관리, 상담사 관리 > 상세 > 선택된 메뉴 권한 목록
     *
     * @param id 임원 시퀀스
     * @author SungHa
     */
    @Transactional(readOnly = true)
    public List<AuthorityMenu> findAllAuthorityMenuByLoginId(Long id) {
        MemberAdmin memberAdmin = memberAdminRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        return authorityMenuRepository.findAllAuthorityMenuByLoginId(memberAdmin.getLoginId());
    }

}
