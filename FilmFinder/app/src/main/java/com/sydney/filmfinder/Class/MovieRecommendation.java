package com.sydney.filmfinder.Class;

public class MovieRecommendation {
    private String movieTitle;

    public MovieRecommendation(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    @Override
    public String toString() {
        return movieTitle;
    }
}
