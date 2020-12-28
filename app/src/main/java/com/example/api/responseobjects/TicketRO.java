package com.example.api.responseobjects;

import com.example.api.model.aviaticket.Price;

import java.util.ArrayList;
import java.util.List;

public class TicketRO {
    private List<Price> prices;
    private Object errors;

    public TicketRO () { }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public ArrayList<Double> getPriceAllDouble() {
        ArrayList<Double> result= new ArrayList<Double>();
        for(Price price : prices){
            result.add(price.getValue());
        }
        return result;
    }

    public Double getPriceDoble(){
        return getPrices().get(0).getValue();
    }
}
