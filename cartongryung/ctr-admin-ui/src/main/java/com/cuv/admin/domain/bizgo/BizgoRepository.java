package com.cuv.admin.domain.bizgo;

import com.cuv.admin.domain.bizgo.entity.Bizgo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BizgoRepository extends JpaRepository<Bizgo, Long> {
    Bizgo findByCode(String code);
}
