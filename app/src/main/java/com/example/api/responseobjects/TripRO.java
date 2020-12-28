package com.example.api.responseobjects;

import com.example.api.model.trip.Hotel;

import java.util.List;

public class TripRO {

    private Object count;
    public List<Hotel> results;

    public Object getCount() {
        return count;
    }

    public void setCount(Object count) {
        this.count = count;
    }

    public List<Hotel> getResults() {
        return results;
    }

    public void setResults(List<Hotel> results) {
        this.results = results;
    }


    public TripRO() {
    }

}
