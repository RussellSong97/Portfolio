package com.cuv.admin.domain.linklisting;


import com.cuv.admin.domain.linklisting.entity.LinkListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LinkListingRepository extends
        JpaRepository<LinkListing, Long>,
        QuerydslPredicateExecutor<LinkListing>,
        LinkListingRepositoryCustom {

}
