package com.ivan4usa.fp.fixer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;

@Service
public class RatesService {

    @Value("${rapid.fixer.host}")
    private String host;

    public String getHost() {
        return host;
    }

    @Value("${rapid.fixer.key}")
    @Getter
    private String apiKey;

    /** Method receives List of strings of currency names and string of data
     * and returns exchange rates from rapid api service and converts to the Rate format
     *
     * @param currencies list of currency names (EUR, GBP etc.)
     * @param date string format of data (f.e. 2021-03-25)
     * @return Rate
     */
    public Rates loadRatesByDate(List<String> currencies, String date) throws IOException, InterruptedException {
        StringBuilder symbols = new StringBuilder();
        for (String currency: currencies) {
            if (Objects.equals(currency, currencies.get(0))) {
                symbols.append(currency);
            } else {
                symbols.append("%2C").append(currency);
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://" + host + "/" + date + "?base=USD&symbols=" + symbols.toString()))
                .header("x-rapidapi-host", host)
                .header("x-rapidapi-key", apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), Rates.class);
    }
}
