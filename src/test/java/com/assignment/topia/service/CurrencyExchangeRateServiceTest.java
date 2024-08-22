package com.assignment.topia.service;


import com.assignment.topia.dto.CurrencyRate;
import com.assignment.topia.dto.CurrencyTimelineResponse;
import com.assignment.topia.dto.FrankfurterResponse;
import com.assignment.topia.model.CurrencyExchangeRate;
import com.assignment.topia.repository.CurrencyExchangeRateRepo;
import jdk.jfr.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
class CurrencyExchangeRateServiceTest {

    public final String CURRENCY_USD = "USD";

    @MockBean
    CurrencyExchangeRateRepo exchangeRateRepo;

    @Autowired
    CurrencyExchangeRateService service;


    @Test
    @Name("Get Generic currency value : Test")
    void getFxTest() throws Exception {
        FrankfurterResponse exchangeRateResponse = service.getFrankfurterResponse();
        assertEquals(4, exchangeRateResponse.getRates().keySet().size() );
    }

    @Test
    void getTimeLineForCurrency() throws Exception {
        CurrencyTimelineResponse timeLineResponse = service.getTimeSeriesResponseForCurrency("JPY");
        CurrencyTimelineResponse mockedTimeSeriesResponse = new CurrencyTimelineResponse();
        Map<String, CurrencyRate> hm = new HashMap<>();
        CurrencyRate cr1 = new CurrencyRate("JPY", "148.01");
        CurrencyRate cr2 = new CurrencyRate("JPY", "148.01");
        CurrencyRate cr3 = new CurrencyRate("JPY", "148.01");
        hm.put("2024-08-16", cr1);
        hm.put("2024-08-17", cr2);
        hm.put("2024-08-18", cr3);
        mockedTimeSeriesResponse.setRates(hm);
        mockedTimeSeriesResponse.setSource(CURRENCY_USD);

        assertEquals(timeLineResponse.getSource(), mockedTimeSeriesResponse.getSource());
        assertEquals(timeLineResponse.getRates().size(), mockedTimeSeriesResponse.getRates().size());
    }

    @Test
    void getFrankfurterResponseTest() {
        String value = "88.56";
        String currency = "INR";
        CurrencyExchangeRate exchangeRate = new CurrencyExchangeRate();
        exchangeRate.setCurrency(currency);
        exchangeRate.setDate("2024-08-24");
        exchangeRate.setValue(value);
        exchangeRate.setId(UUID.randomUUID());

        when(exchangeRateRepo.findDistinctFirstByDateAndCurrency(anyString(), anyString()))
                .thenReturn(exchangeRate);
        FrankfurterResponse frankfurterResponse = service.getFrankfurterResponse();
        assertEquals(value, String.valueOf(frankfurterResponse.getRates().get(currency)));
    }

}
