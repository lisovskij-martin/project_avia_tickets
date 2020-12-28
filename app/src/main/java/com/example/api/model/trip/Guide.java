package com.example.api.model.trip;

import java.io.Serializable;

public class Guide implements Serializable {

    private String first_name;
    private String id;
    private Avatar avatar;
    private String url;
    private double rating;

    public Guide(String first_name, String id, Avatar avatar, String url, double rating) {
        this.first_name = first_name;
        this.id = id;
        this.avatar = avatar;
        this.url = url;
        this.rating = rating;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getId() {
        return id;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public String getUrl() {
        return url;
    }

    public double getRating() {
        return rating;
    }

}
