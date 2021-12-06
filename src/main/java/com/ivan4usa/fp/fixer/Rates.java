package com.ivan4usa.fp.fixer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Rates class with currencies information
 */
public class Rates {

    /**
     * Date of rates
     */
    @JsonProperty("date")
    private String date;

    /**
     * Status
     */
    @JsonProperty("success")
    private boolean success;

    /**
     * Map of rates
     */
    @JsonProperty("rates")
    private Map<String, Double> rates;

    /**
     * Is it historical or current
     */
    @JsonProperty("historical")
    private boolean historical;

    /**
     * Timestamp of information
     */
    @JsonProperty("timestamp")
    private int timestamp;

    /**
     * Base currency
     */
    @JsonProperty("base")
    private String base;

    /**
     * Setter for date
     * @param date of rates
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter for date
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for Status
     * @param success status
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Getter for status
     * @return success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Getter for rates
     * @return rates
     */
    public Map<String, Double> getRates() {
        return rates;
    }

    /**
     * Setter for rates
     * @param rates of currencies
     */
    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    /**
     * Setter for historical
     * @param historical boolean value
     */
    public void setHistorical(boolean historical) {
        this.historical = historical;
    }

    /**
     * Getter for historical
     * @return historical boolean value
     */
    public boolean isHistorical() {
        return historical;
    }

    /**
     * Setter for timestamp
     * @param timestamp of rates
     */
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Getter for timestamp
     * @return timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Setter for base
     * @param base currency
     */
    public void setBase(String base) {
        this.base = base;
    }

    /**
     * Getter for base
     * @return base
     */
    public String getBase() {
        return base;
    }

    /**
     * to String method for class
     * @return string of parameters
     */
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
