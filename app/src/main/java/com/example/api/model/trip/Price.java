package com.example.api.model.trip;

import java.io.Serializable;

public class Price implements Serializable {

    private double value;
    private String currency;
    private String price_from;
    private String unit_string;
    private String value_string;
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice_from() {
        return price_from;
    }

    public void setPrice_from(String price_from) {
        this.price_from = price_from;
    }

    public String getUnit_string() {
        return unit_string;
    }

    public void setUnit_string(String unit_string) {
        this.unit_string = unit_string;
    }

    public String getValue_string() {
        return value_string;
    }

    public void setValue_string(String value_string) {
        this.value_string = value_string;
    }

    public Price() {}

}
