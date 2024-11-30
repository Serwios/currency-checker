package com.currencyservice.service;

import com.currencyservice.entity.Currency;
import com.currencyservice.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCurrency_ShouldReturnNewCurrency() {
        Currency currency = new Currency(1L,"USD", "US Dollar");
        when(currencyRepository.save(any())).thenReturn(currency);

        Currency result = currencyService.addCurrency("USD", "US Dollar");

        assertNotNull(result);
        assertEquals("USD", result.getCode());
        verify(currencyRepository, times(1)).save(any());
    }

    @Test
    void getAllCurrencies_ShouldReturnList() {
        List<Currency> currencies = List.of(
                new Currency(1L, "USD", "US Dollar"),
                new Currency(2L,"EUR", "Euro")
        );
        when(currencyRepository.findAll()).thenReturn(currencies);

        List<Currency> result = currencyService.getAllCurrencies();

        assertEquals(2, result.size());
        assertEquals("USD", result.get(0).getCode());
        verify(currencyRepository, times(1)).findAll();
    }
}