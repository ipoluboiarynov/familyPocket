package com.ivan4usa.fp.fixer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = {"classpath:application.properties"})
public class RatesServiceTest {

    @Value("${rapid.fixer.host}")
    String host;

    @Value("${rapid.fixer.key}")
    String apiKey;

    @Test
    void loadRatesByDate() throws IOException, InterruptedException {
        List<String> currencies = new ArrayList<>();
        currencies.add("EUR");
        currencies.add("CAD");
        currencies.add("RUB");

        String date = "2021-01-10";

        StringBuilder symbols = new StringBuilder();
        for (String currency: currencies) {
            if (Objects.equals(currency, currencies.get(0))) {
                symbols.append(currency);
            } else {
                symbols.append("%2C").append(currency);
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://" + host + "/" + date + "?base=USD&symbols=" + symbols))
                .header("x-rapidapi-host", host)
                .header("x-rapidapi-key", apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Double> expectedRate = new HashMap<>();
        expectedRate.put("EUR", 0.820635);
        expectedRate.put("CAD", 1.27319);
        expectedRate.put("RUB", 74.4199);
        Rates result =  mapper.readValue(response.body(), Rates.class);
        assertEquals(expectedRate, result.getRates());
    }
}
