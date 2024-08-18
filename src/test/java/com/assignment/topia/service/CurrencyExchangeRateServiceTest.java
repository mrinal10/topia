package com.assignment.topia.service;


import com.assignment.topia.dto.CurrencyRate;
import com.assignment.topia.dto.CurrencyTimelineResponse;
import com.assignment.topia.dto.FrankfurterResponse;
import com.assignment.topia.model.CurrencyExchangeRate;
import com.assignment.topia.repository.CurrencyExchangeRateRepo;
import com.assignment.topia.util.AppConstants;
import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static com.assignment.topia.util.AppConstants.DATE_PATTERN;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class CurrencyExchangeRateServiceTest {

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
        mockedTimeSeriesResponse.setSource(AppConstants.CURRENCY_USD);

        assertEquals(timeLineResponse.getSource(), mockedTimeSeriesResponse.getSource());
        assertEquals(timeLineResponse.getRates().size(), mockedTimeSeriesResponse.getRates().size());
    }

}
