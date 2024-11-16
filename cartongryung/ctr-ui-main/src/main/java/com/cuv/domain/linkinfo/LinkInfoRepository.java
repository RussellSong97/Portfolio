package com.cuv.domain.linkinfo;


import com.cuv.domain.linkinfo.entity.LinkInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LinkInfoRepository extends
        JpaRepository<LinkInfo, Long>,
        QuerydslPredicateExecutor<LinkInfo>,
        LinkInfoRepositoryCustom {

    @Query("SELECT li FROM LinkInfo li WHERE li.vhrno = :vhrno AND li.delYn = 'N'")
    Optional<LinkInfo> findByVhrno(@Param("vhrno") String vhrno);

}
