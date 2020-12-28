package com.example.api.responseobjects;

import com.example.api.model.trip.City;

import java.util.List;

public class CityRO {

    private Object count;
    public List<City> results;

    public Object getCount() {
        return count;
    }

    public void setCount(Object count) {
        this.count = count;
    }

    public List<City> getResults() {
        return results;
    }

    public void setResults(List<City> results) {
        this.results = results;
    }

    public CityRO() {
    }

}
