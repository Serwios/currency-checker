package com.currencyservice.scheduler;

import com.currencyservice.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateUpdater {
    private final CurrencyService currencyService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${API_URL}")
    private String fixerApiUrl;

    @Value("${API_KEY}")
    private String fixerApiKey;

    @Scheduled(fixedRate = 3600000)
    public void fetchAndUpdateRates() {
        log.info("Execution currency update rates...");
        try {
            currencyService.getAllCurrencies().forEach(currency -> {
                String url = String.format("%s?access_key=%s&symbols=%s", fixerApiUrl, fixerApiKey, currency.getCode());
                Map<String, Object> response = restTemplate.getForObject(url, Map.class);

                if (response != null && (Boolean) response.get("success")) {
                    Map<String, BigDecimal> rates = (Map<String, BigDecimal>) response.get("rates");
                    BigDecimal rate = rates.get(currency.getCode());
                    if (rate != null) {
                        currencyService.updateExchangeRates(currency.getCode(), rate);
                    }
                }
            });
        } catch (Exception e) {
            log.error("Unexpected exception during update of exchange rates", e);
        }
    }
}