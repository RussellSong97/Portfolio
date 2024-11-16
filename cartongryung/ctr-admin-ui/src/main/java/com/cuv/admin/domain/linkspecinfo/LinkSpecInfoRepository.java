package com.cuv.admin.domain.linkspecinfo;


import com.cuv.admin.domain.linkspecinfo.entity.LinkSpecInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LinkSpecInfoRepository extends
        JpaRepository<LinkSpecInfo, Long>,
        QuerydslPredicateExecutor<LinkSpecInfo>,
        LinkSpecInfoRepositoryCustom {

}
