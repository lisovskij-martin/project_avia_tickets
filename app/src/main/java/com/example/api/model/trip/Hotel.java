package com.example.api.model.trip;

import java.io.Serializable;
import java.util.ArrayList;

public class Hotel implements Serializable {
    private int id;
    private String title;
    private String tagline;
    private String url;
    private int max_persons;
    private double duration;
    private double rating;
    private Guide guide;
    private Price price;
    private String priceString;
    private City city;
    private ArrayList<Picture> photos;

    public Picture getPhotos() {
        return photos.get(0);
    }

    public City getCity() {
        return city;
    }

    public Guide getGuide() {
        return guide;
    }

    public Guide getGuider(){
        return guide;
    }

    public Price getPrice() {
        return price;
    }

    public String getPriceString() {
        return price.getValue_string();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTagline() {
        return tagline;
    }

    public String getUrl() {
        return url;
    }

    public int getMax_persons() {
        return max_persons;
    }

    public double getDuration() {
        return duration;
    }

    public double getRating() {
        return rating;
    }

    public String toString(){
        String str = +getId() + "\n"
                + getTitle() + "\n"
                + getPriceString();
        return str;
    }





}
