package com.sydney.filmfinder;

import java.io.Serializable;

public class Movie implements Serializable {
    private String id;
    private String title;
    private double rating;
    public Movie(String id,String title,double rating){
        this.id=id;
        this.title=title;
        this.rating=rating;
    }

    public double getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

