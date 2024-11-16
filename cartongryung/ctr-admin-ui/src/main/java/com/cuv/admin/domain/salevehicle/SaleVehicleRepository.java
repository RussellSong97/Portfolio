package com.cuv.admin.domain.salevehicle;

import com.cuv.admin.domain.salevehicle.entity.SaleVehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleVehicleRepository extends JpaRepository<SaleVehicle, Long> {
}
