package com.cuv.admin.domain.linkvehicleinfo;


import com.cuv.admin.domain.linkvehicleinfo.entity.LinkVehicleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface LinkVehicleInfoRepository extends
        JpaRepository<LinkVehicleInfo, Long>,
        QuerydslPredicateExecutor<LinkVehicleInfo> {

    List<LinkVehicleInfo> findByCarGradeNbr(Long carGradeNbr);
}
