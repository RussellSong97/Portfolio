package com.cuv.domain.salevehicle;

import com.cuv.domain.salevehicle.entity.SaleVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SaleVehicleRepository extends
        JpaRepository<SaleVehicle, Long>,
        QuerydslPredicateExecutor<SaleVehicle> {

    @Query("SELECT sv FROM SaleVehicle sv WHERE sv.carPlateNumber = :carPlateNumber AND sv.delYn = 'N'")
    Optional<SaleVehicle> findByCarPlateNumber(@Param("carPlateNumber") String carPlateNumber);
}
