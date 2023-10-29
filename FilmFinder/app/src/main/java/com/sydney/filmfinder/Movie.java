package com.sydney.filmfinder;

public class Movie {
    private String Cast;
    private String Director;
    private String IMDB_ID;
    private String MoviePoster;
    private String Rating;
    private String Runtime;
    private String ShortSummary;
    private String Summary;
    private String Title;
    private String Writers;
    private String Year;
    private String YouTubeTrailer;

    public Movie() {
        // Empty constructor needed for Firestore
    }

    // Getter and setter methods for each field
    public String getCast() {
        return Cast;
    }

    public void setCast(String cast) {
        Cast = cast;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getIMDB_ID() {
        return IMDB_ID;
    }

    public void setIMDB_ID(String IMDB_ID) {
        this.IMDB_ID = IMDB_ID;
    }

    public String getMoviePoster() {
        return MoviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        MoviePoster = moviePoster;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        Runtime = runtime;
    }

    public String getShortSummary() {
        return ShortSummary;
    }

    public void setShortSummary(String shortSummary) {
        ShortSummary = shortSummary;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getWriters() {
        return Writers;
    }

    public void setWriters(String writers) {
        Writers = writers;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getYouTubeTrailer() {
        return YouTubeTrailer;
    }

    public void setYouTubeTrailer(String youTubeTrailer) {
        YouTubeTrailer = youTubeTrailer;
    }
}
