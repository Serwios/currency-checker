package com.currencyservice.repository;

import com.currencyservice.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    @Query("SELECT er FROM ExchangeRate er WHERE er.currency.code = ?1 ORDER BY er.timestamp DESC")
    List<ExchangeRate> findByCurrencyCodeOrderedByTimestamp(String currencyCode);
}