package com.bayu.regulatory.repository;

import com.bayu.regulatory.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    Optional<ExchangeRate> findByCode(String code);

    Boolean existsByCode(String code);

}
