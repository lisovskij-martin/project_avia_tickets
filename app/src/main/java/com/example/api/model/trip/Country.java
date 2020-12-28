package com.example.api.model.trip;

import java.io.Serializable;

public class Country implements Serializable {

    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
