package com.ivan4usa.fp.fixer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Rates {

    @JsonProperty("date")
    private String date;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("rates")
    private Map<String, Double> rates;

    @JsonProperty("historical")
    private boolean historical;

    @JsonProperty("timestamp")
    private int timestamp;

    @JsonProperty("base")
    private String base;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) { this.rates = rates; }

    public void setHistorical(boolean historical) {
        this.historical = historical;
    }

    public boolean isHistorical() {
        return historical;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getBase() {
        return base;
    }

    @Override
    public String toString() {
        return "Rates{" +
                "date='" + date + '\'' +
                ", success=" + success +
                ", rates=" + rates +
                ", historical=" + historical +
                ", timestamp=" + timestamp +
                ", base='" + base + '\'' +
                '}';
    }
}
