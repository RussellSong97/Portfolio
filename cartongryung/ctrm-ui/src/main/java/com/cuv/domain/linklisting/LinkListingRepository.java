package com.cuv.domain.linklisting;

import com.cuv.domain.linklisting.dto.LinkListingListDto;
import com.cuv.domain.linklisting.entity.LinkListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface LinkListingRepository extends JpaRepository<LinkListing, Long>, LinkListingRepositoryCustom, QuerydslPredicateExecutor<LinkListing> {
}
