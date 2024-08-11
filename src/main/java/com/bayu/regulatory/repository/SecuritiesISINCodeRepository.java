package com.bayu.regulatory.repository;

import com.bayu.regulatory.model.SecuritiesISINCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecuritiesISINCodeRepository extends JpaRepository<SecuritiesISINCode, Long> {

    @Query(value = "FROM SecuritiesISINCode s WHERE s.externalCode2 = :externalCode")
    Optional<SecuritiesISINCode> findByExternalCode(@Param("externalCode") String externalCode);

}
