package com.cuv.admin.domain.linkcolorinfo;


import com.cuv.admin.domain.linkcolorinfo.entity.LinkColorInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LinkColorInfoRepository extends
        JpaRepository<LinkColorInfo, Long>,
        QuerydslPredicateExecutor<LinkColorInfo>,
        LinkColorInfoRepositoryCustom {

}
