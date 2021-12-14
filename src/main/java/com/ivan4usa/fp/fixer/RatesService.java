package com.ivan4usa.fp.fixer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * Service for loading data on the exchange rate
 */
@Service
public class RatesService {

    @Value("${fixer.host}")
    private String fixerHost;

    @Value("${fixer.key}")
    private String fixerKey;

    /**
     * A method that calls an external api service (FIxer.io) to load data on the exchange rate
     * for the date specified in the received parameter
     * @param date date of String type ("yyyy-mm-dd")
     * @return rates of currencies
     * @throws IOException any exception
     */
    public Rates loadRatesByDateFixer(String date) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(fixerHost + date + "?access_key=" + fixerKey + "&format=1")
                .method("GET", null)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        ObjectMapper mapper = new ObjectMapper();
        Rates rates =  mapper.readValue(response.body().string(), Rates.class);
        Map<String, Double> list = rates.getRates();
        Double base = list.get("USD");
        for (Map.Entry<String, Double> entry : list.entrySet()) {
            entry.setValue(entry.getValue() / base);
        }
        rates.setRates(list);
        return rates;
    }
}
