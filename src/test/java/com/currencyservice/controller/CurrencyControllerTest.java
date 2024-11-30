package com.currencyservice.controller;
import com.currencyservice.entity.Currency;
import com.currencyservice.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
@Import(CurrencyControllerTest.TestConfig.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyService currencyService;

    @Test
    void addCurrency_ShouldReturnCurrency() throws Exception {
        Currency currency = new Currency(1L, "USD", "US Dollar");
        when(currencyService.addCurrency("USD", "US Dollar")).thenReturn(currency);

        mockMvc.perform(post("/api/currencies")
                        .param("code", "USD")
                        .param("name", "US Dollar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.name").value("US Dollar"));
    }

    @Test
    void getAllCurrencies_ShouldReturnCurrencyList() throws Exception {
        List<Currency> currencies = List.of(
                new Currency(1L, "USD", "US Dollar"),
                new Currency(2L, "EUR", "Euro")
        );
        when(currencyService.getAllCurrencies()).thenReturn(currencies);

        mockMvc.perform(get("/api/currencies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].code").value("USD"));
    }

    static class TestConfig {
        @Bean
        public CurrencyService currencyService() {
            return Mockito.mock(CurrencyService.class);
        }
    }
}