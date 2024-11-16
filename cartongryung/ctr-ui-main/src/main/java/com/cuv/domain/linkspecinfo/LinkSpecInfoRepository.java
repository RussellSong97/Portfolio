package com.cuv.domain.linkspecinfo;


import com.cuv.domain.linkspecinfo.entity.LinkSpecInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LinkSpecInfoRepository extends
        JpaRepository<LinkSpecInfo, Long>,
        QuerydslPredicateExecutor<LinkSpecInfo>,
        LinkSpecInfoRepositoryCustom {

}
