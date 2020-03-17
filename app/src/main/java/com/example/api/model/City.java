package com.example.api.model;

import androidx.annotation.NonNull;

public class City {
    private String code;
    private String name;
    private String country_name;

    @NonNull
    @Override
    public String toString() {
        String answer="";
        if (code!=null) answer=answer.concat(code+" ");
        if (name!=null) answer=answer.concat(name+" ");
        if (country_name!=null) answer=answer.concat(country_name);
        return answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
}
