package com.ivan4usa.fp.fixer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan4usa.fp.entities.Currency;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class RatesService {

    @Value("${fixer.host}")
    private String fixerHost;

    @Value("${fixer.key}")
    private String fixerKey;

    @Value("${rapid.host}")
    private String rapidHost;

    @Value("${rapid.key}")
    private String rapidKey;

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

    public Rates loadRatesByDateRapid(String date) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://" + rapidHost + "/" + date + "?base=USD")
                .get()
                .addHeader("x-rapidapi-host", rapidHost)
                .addHeader("x-rapidapi-key", rapidKey)
                .build();
        Response response = client.newCall(request).execute();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body().string(), Rates.class);
    }
}
