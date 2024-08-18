package com.assignment.topia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class FrankfurterResponse {

    @JsonProperty("amount")
    Double amount;

    @JsonProperty("base")
    String base;

    @JsonProperty("date")
    Date date;

    @JsonProperty("rates")
    Map<String, Double> rates;
}
