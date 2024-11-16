package com.cuv.admin.domain.linkoptioninfo;


import com.cuv.admin.domain.linkoptioninfo.entity.LinkOptionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LinkOptionInfoRepository extends
        JpaRepository<LinkOptionInfo, Long>,
        QuerydslPredicateExecutor<LinkOptionInfo>,
        LinkOptionInfoRepositoryCustom {

}
