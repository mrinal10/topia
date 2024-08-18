package com.assignment.topia.service;

import com.assignment.topia.dto.CurrencyRate;
import com.assignment.topia.dto.CurrencyTimelineResponse;
import com.assignment.topia.dto.FrankfurterResponse;
import com.assignment.topia.model.CurrencyExchangeRate;
import com.assignment.topia.repository.CurrencyExchangeRateRepo;
import com.assignment.topia.util.AppConstants;
import com.assignment.topia.util.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.assignment.topia.util.AppConstants.*;

@Service
@Slf4j
public class CurrencyExchangeRateService {

    private final CurrencyExchangeRateRepo exchangeRateRepo;
    private static final String ALLOWED_FOREIGN_CURRENCIES = "EUR,GBP,JPY,CZK";
    private final WebClient webClient;

    public CurrencyExchangeRateService(CurrencyExchangeRateRepo exchangeRateRepo,
                                       WebClient webClient) {
        this.exchangeRateRepo = exchangeRateRepo;
        this.webClient = webClient;
    }

    public FrankfurterResponse getFrankfurterResponse() {
        List<String> allowedCurrencies = List.of(ALLOWED_FOREIGN_CURRENCIES.split(COMMA));
        var currentDate = new SimpleDateFormat(DATE_PATTERN).format(Date.from(Instant.now()));
        FrankfurterResponse frankfurterResponse = new FrankfurterResponse();
        for(String currency : allowedCurrencies) {
            try {
                CurrencyExchangeRate exchangeRate =
                        exchangeRateRepo.findDistinctFirstByDateAndCurrency(currentDate, currency);
                Map<String, Double> map;
                if(exchangeRate != null) {
                    map = frankfurterResponse.getRates();
                    frankfurterResponse.setAmount(1.0);
                    DateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
                    Date startDate = formatter.parse(exchangeRate.getDate());

                    frankfurterResponse.setBase(CURRENCY_USD);
                    frankfurterResponse.setDate(startDate);
                    if(map == null) {
                        map = new HashMap<>();
                    }
                    map.put(exchangeRate.getCurrency(), Double.parseDouble(exchangeRate.getValue()));
                } else {
                    FrankfurterResponse response = getFrankfurterResponse(currency, currentDate);

                    frankfurterResponse.setAmount(response.getAmount());
                    frankfurterResponse.setBase(response.getBase());
                    frankfurterResponse.setDate(response.getDate());
                    map = frankfurterResponse.getRates();
                    if(map == null) {
                        map=new HashMap<>();
                    }
                    Map<String, Double> rates = response.getRates();
                    map.put(currency, rates.get(currency));

                    CurrencyExchangeRate currencyExchangeRate = new CurrencyExchangeRate();
                    currencyExchangeRate.setCurrency(currency);
                    currencyExchangeRate.setDate(currentDate);
                    currencyExchangeRate.setValue(String.valueOf(rates.get(currency)));

                    exchangeRateRepo.save(currencyExchangeRate);
                }
                frankfurterResponse.setRates(map);
            } catch (Exception e) {
                log.error("Exception while getting api response for {}", currency);
            }
        }
        return frankfurterResponse;
    }

    public CurrencyTimelineResponse getTimeSeriesResponseForCurrency(String targetCurrency) {
        Map<String, CurrencyRate> timeSeries = new HashMap<>();
        CurrencyTimelineResponse currencyTimelineResponse = new CurrencyTimelineResponse();
        try {
            for(int i = 0 ; i <= 2; i++){
                var currentDate = new SimpleDateFormat(DATE_PATTERN).format(Date.from(Instant.now().minus(i, ChronoUnit.DAYS)));
                List<CurrencyExchangeRate> data = new ArrayList<>();
                CurrencyExchangeRate currencyExchangeRate = exchangeRateRepo.findDistinctFirstByDateAndCurrency(currentDate, targetCurrency);
                if(currencyExchangeRate == null){
                    data = getExchangeRateFromApi(targetCurrency, currentDate);
                } else {
                    data.add(currencyExchangeRate);
                }

                data.forEach(dat -> {
                    CurrencyRate currencyRate = new CurrencyRate();
                    currencyRate.setValue(dat.getValue());
                    currencyRate.setTarget(dat.getCurrency());
                    timeSeries.put(currentDate, currencyRate);
                });
            }
            currencyTimelineResponse.setRates(timeSeries);
            currencyTimelineResponse.setSource(AppConstants.CURRENCY_USD);

        } catch(Exception e) {
            log.error("Exception while exchange rate timeline ");
        }
        return currencyTimelineResponse;
    }

    private List<CurrencyExchangeRate> getExchangeRateFromApi(String target, String date) throws ConnectException, URISyntaxException {
        FrankfurterResponse data =  getFrankfurterResponse(target, date);
        List<CurrencyExchangeRate> response = new ArrayList<>();
        data.getRates().forEach((k, v) -> {
            CurrencyExchangeRate rate = new CurrencyExchangeRate();
            rate.setDate(date);
            rate.setCurrency(k);
            rate.setValue(v.toString());
            response.add(rate);
            saveResponseData(date, k, v.toString());
        });
        return response;
    }

    private void saveResponseData(String date, String currency, String value) {
        CurrencyExchangeRate exchangeRates = new CurrencyExchangeRate();
        exchangeRates.setDate(date);
        exchangeRates.setCurrency(currency);
        exchangeRates.setValue(value);
        exchangeRateRepo.save(exchangeRates);
    }

    private FrankfurterResponse getFrankfurterResponse(String target, String date) throws URISyntaxException, ConnectException {
        String targetUri = String.format("https://api.frankfurter.app/%s?from=USD&to=%s",
                date == null ? "latest" : date, target);
        return webClient.getExchangeResponse(targetUri);
    }

}
