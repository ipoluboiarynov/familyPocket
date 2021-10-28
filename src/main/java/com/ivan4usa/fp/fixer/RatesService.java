package com.ivan4usa.fp.fixer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan4usa.fp.entity.Currency;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class RatesService {

    @Value("${rapid.fixer.host}")
    @Getter
    private String host;

    @Value("${rapid.fixer.key}")
    @Getter
    private String apiKey;

    @Value("${fixer.host}")
    private String fixerHost;

    @Value("${fixer.key}")
    private String fixerKey;

    public Rates loadRatesByDateFixer(String date) throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(fixerHost + date + "?access_key=" + fixerKey + "&format=1")
                .method("GET", null)
                .get()
                .build();

        Response response = client.newCall(request).execute();



        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body().string(), Rates.class);
    }

    /** Method receives List of strings of currency names and string of data
     * and returns exchange rates from rapid api service and converts to the Rate format
     *
     * @param currencies list of currency names (EUR, GBP etc.)
     * @param date string format of data (f.e. 2021-03-25)
     * @return Rate
     */
    public Rates loadRatesByDate(List<Currency> currencies, String date) throws IOException, InterruptedException {
        StringBuilder symbols = new StringBuilder();
        for (Currency currency: currencies) {
            if (Objects.equals(currency, currencies.get(0))) {
                symbols.append(currency.getName());
            } else {
                symbols.append("%2C").append(currency.getName());
            }
        }
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://" + host + "/" + date + "?base=USD&symbols=" + symbols.toString())
                .get()
                .addHeader("x-rapidapi-host", host)
                .addHeader("x-rapidapi-key", apiKey)
                .build();

        Response response = client.newCall(request).execute();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body().string(), Rates.class);
    }
}
