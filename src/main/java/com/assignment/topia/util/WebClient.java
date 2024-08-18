package com.assignment.topia.util;

import com.assignment.topia.dto.FrankfurterResponse;

import java.net.ConnectException;
import java.net.URISyntaxException;

public interface WebClient {

    public FrankfurterResponse getExchangeResponse(String url) throws URISyntaxException, ConnectException;
}
