package com.assignment.topia.util.impl;

import com.assignment.topia.dto.FrankfurterResponse;
import com.assignment.topia.util.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WebClientImpl implements WebClient {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WebClientImpl.class);
    @Override
    public FrankfurterResponse getExchangeResponse(String uri) throws URISyntaxException, ConnectException {
        HttpRequest request = HttpRequest.newBuilder().uri(new URI(uri)).GET().build();
        FrankfurterResponse frankfurterResponse;
        log.info("getting response from external api");
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            frankfurterResponse = new ObjectMapper().readValue(response.body(), FrankfurterResponse.class);

        } catch (IOException | InterruptedException e) {
            log.info("getting response from external api : Failed");
            throw new ConnectException(e.getMessage());
        }
        log.info("getting response from external api : Success");
        return frankfurterResponse;
    }
}
