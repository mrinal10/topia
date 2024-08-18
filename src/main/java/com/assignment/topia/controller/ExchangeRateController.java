package com.assignment.topia.controller;

import com.assignment.topia.dto.CurrencyTimelineResponse;
import com.assignment.topia.dto.FrankfurterResponse;
import com.assignment.topia.service.CurrencyExchangeRateService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fx")
public class ExchangeRateController {

    private final CurrencyExchangeRateService exchangeRateService;

    public ExchangeRateController(CurrencyExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public FrankfurterResponse getForeignExchangeRate() {
        return exchangeRateService.getFrankfurterResponse();
    }

    @GetMapping(path = "/{targetCurrency}")
    public CurrencyTimelineResponse getForeignExchangeRateSeries(@PathVariable(name = "targetCurrency") String targetCurrency){
        return exchangeRateService.getTimeSeriesResponseForCurrency(targetCurrency);
    }
}
