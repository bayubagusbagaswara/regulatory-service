package com.bayu.regulatory.repository;

import com.bayu.regulatory.model.SecuritiesISINCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecuritiesISINCodeRepository extends JpaRepository<SecuritiesISINCode, Long> {
}
