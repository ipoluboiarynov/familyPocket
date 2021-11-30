package com.ivan4usa.fp.fixer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan4usa.fp.services.AccountService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = {"classpath:application.properties"})
@ContextConfiguration(classes = RatesService.class)
public class RatesServiceTest {
    @Value("${fixer.host}")
    private String fixerHost;

    @Value("${fixer.key}")
    private String fixerKey;

    @Test
    void loadRatesByDateFixer() throws IOException {
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

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(fixerHost + date + "?access_key=" + fixerKey + "&format=1")
                .method("GET", null)
                .get()
                .build();

        Response response = client.newCall(request).execute();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Double> expectedRate = new HashMap<>();
        expectedRate.put("EUR", 0.820635);
        expectedRate.put("CAD", 1.27319);
        expectedRate.put("RUB", 74.4199);
        Rates result =  mapper.readValue(response.body().string(), Rates.class);
        assertNotNull(result.getRates());

    }
}
