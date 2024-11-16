package com.cuv.admin.domain.authoritymenu;

import com.cuv.admin.domain.authoritymenu.entity.AuthorityMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 메뉴 권한 Repository
 */
public interface AuthorityMenuRepository extends JpaRepository<AuthorityMenu, Long> {

    @Query("SELECT am FROM AuthorityMenu am WHERE am.manageYn = 'Y'")
    List<AuthorityMenu> findAllAuthorityMenuByManageYn();

    @Query("SELECT am FROM AuthorityMenu am " +
            "JOIN AuthorityMenuEmployee ame " +
            "ON ame.authorityMenuId = am.id " +
            "WHERE ame.loginId = :loginId")
    List<AuthorityMenu> findAllAuthorityMenuByLoginId(@Param("loginId") String loginId);

}
