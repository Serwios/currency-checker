package com.currencyservice.service;

import com.currencyservice.entity.Currency;
import com.currencyservice.entity.ExchangeRate;
import com.currencyservice.repository.CurrencyRepository;
import com.currencyservice.repository.ExchangeRateRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    @Getter
    private final Map<String, BigDecimal> exchangeRatesMap = new ConcurrentHashMap<>();

    @Transactional
    public Currency addCurrency(String code, String name) {
        if (currencyRepository.findByCode(code).isPresent()) {
            throw new IllegalArgumentException("Currency with this code already exists.");
        }
        Currency currency = new Currency();
        currency.setCode(code);
        currency.setName(name);
        return currencyRepository.save(currency);
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public List<ExchangeRate> getExchangeRates(String code) {
        return exchangeRateRepository.findByCurrencyCodeOrderedByTimestamp(code);
    }

    @Transactional
    public void updateExchangeRates(String code, BigDecimal rate) {
        Currency currency = currencyRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Currency not found."));
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setCurrency(currency);
        exchangeRate.setRate(rate);
        exchangeRate.setTimestamp(LocalDateTime.now());

        exchangeRateRepository.save(exchangeRate);
        exchangeRatesMap.put(code, rate);
    }
}