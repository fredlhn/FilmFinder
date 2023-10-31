package com.sydney.filmfinder.Class;

import java.io.Serializable;

public class Movie implements Serializable {
    private String id;
    private String title;
    private double rating;

    private String cast;
    private String director;
    private String imdbId;
    private String moviePoster;
    private String ratingString;
    private String runtime;
    private String shortSummary;
    private String summary;
    private String writers;
    private String year;
    private String youTubeTrailer;

    // Existing constructor
    public Movie(String id, String title, double rating) {
        this.id = id;
        this.title = title;
        this.rating = rating;
    }

    // New constructor
    public Movie(String id, String title, double rating, String cast, String director, String imdbId, String moviePoster, String ratingString,
                 String runtime, String shortSummary, String summary, String writers, String year, String youTubeTrailer) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.cast = cast;
        this.director = director;
        this.imdbId = imdbId;
        this.moviePoster = moviePoster;
        this.ratingString = ratingString;
        this.runtime = runtime;
        this.shortSummary = shortSummary;
        this.summary = summary;
        this.writers = writers;
        this.year = year;
        this.youTubeTrailer = youTubeTrailer;
    }

    public double getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    // Getters and setters for new attributes
    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getRatingString() {
        return ratingString;
    }

    public void setRatingString(String ratingString) {
        this.ratingString = ratingString;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getShortSummary() {
        return shortSummary;
    }

    public void setShortSummary(String shortSummary) {
        this.shortSummary = shortSummary;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYouTubeTrailer() {
        return youTubeTrailer;
    }

    public void setYouTubeTrailer(String youTubeTrailer) {
        this.youTubeTrailer = youTubeTrailer;
    }

    public String getIMDB_ID() {
        return this.imdbId;
    }
}
