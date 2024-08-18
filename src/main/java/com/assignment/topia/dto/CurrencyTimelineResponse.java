package com.assignment.topia.dto;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CurrencyTimelineResponse {

    Map<String, CurrencyRate> rates;
    String source;
}
