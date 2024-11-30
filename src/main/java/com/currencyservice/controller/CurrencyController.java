package com.currencyservice.controller;
import com.currencyservice.entity.Currency;
import com.currencyservice.entity.ExchangeRate;
import com.currencyservice.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
@Tag(name = "Currency API", description = "API for managing currencies and exchange rates.")
public class CurrencyController {
    private final CurrencyService currencyService;

    @PostMapping
    @Operation(
            summary = "Add a new currency",
            description = "Adds a new currency to the system by providing its code and name."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency added successfully",
                    content = @Content(schema = @Schema(implementation = Currency.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content)
    })
    public ResponseEntity<Currency> addCurrency(
            @RequestParam String code,
            @RequestParam String name) {
        return ResponseEntity.ok(currencyService.addCurrency(code, name));
    }

    @GetMapping
    @Operation(
            summary = "Get all currencies",
            description = "Fetches a list of all currencies available in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of currencies retrieved",
                    content = @Content(schema = @Schema(implementation = Currency.class))),
            @ApiResponse(responseCode = "404", description = "No currencies found",
                    content = @Content)
    })
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping("/{code}/rates")
    @Operation(
            summary = "Get exchange rates for a specific currency",
            description = "Fetches the exchange rates of a currency based on its code."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchange rates retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ExchangeRate.class))),
            @ApiResponse(responseCode = "404", description = "Currency not found",
                    content = @Content)
    })
    public ResponseEntity<List<ExchangeRate>> getExchangeRates(@PathVariable String code) {
        return ResponseEntity.ok(currencyService.getExchangeRates(code));
    }

    @GetMapping("/rates/in-memory")
    @Operation(
            summary = "Get in-memory exchange rates",
            description = "Fetches exchange rates stored in memory as a map of currency codes and their rates."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "In-memory exchange rates retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, BigDecimal>> getInMemoryExchangeRates() {
        return ResponseEntity.ok(currencyService.getExchangeRatesMap());
    }
}