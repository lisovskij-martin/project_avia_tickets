package com.example.api.model;

import java.util.Date;

public class Price {
    private Double value;
    private String return_date;
    private String gate;
    private String depart_date;
    private int number_of_changes;

    public Double getValue() {
        return value;
    }

    public void setValue(Double val) {
        value = val;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String ret_dat) {
        return_date = ret_dat;
    }

    public int getNumber_of_changes() {
        return number_of_changes;
    }

    public void setNumber_of_changes(int number_of_changes) {
        this.number_of_changes = number_of_changes;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public String getDepart_date() {
        return depart_date;
    }

    public void setDepart_date(String dep_dat) { depart_date = dep_dat;
    }
}
