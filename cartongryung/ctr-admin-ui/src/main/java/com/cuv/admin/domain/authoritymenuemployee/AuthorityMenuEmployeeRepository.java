package com.cuv.admin.domain.authoritymenuemployee;

import com.cuv.admin.domain.authoritymenuemployee.entity.AuthorityMenuEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorityMenuEmployeeRepository extends JpaRepository<AuthorityMenuEmployee, Long> {

    @Modifying
    @Query("DELETE FROM AuthorityMenuEmployee ame " +
            "WHERE ame.loginId = :loginId")
    void deleteAllByLoginId(@Param("loginId") String loginId);

    @Query("SELECT ame FROM AuthorityMenuEmployee ame " +
            "WHERE ame.loginId = :loginId")
    List<AuthorityMenuEmployee> findAuthorityMenuEmployeeByLoginId(@Param("loginId") String loginId);

}
