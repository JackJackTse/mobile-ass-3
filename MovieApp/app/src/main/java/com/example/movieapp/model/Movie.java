package com.example.movieapp.model;

import java.io.Serializable;

public class Movie implements Serializable {
    // Attributes
    private String imdbID;
    private String poster;
    private String title;
    private String year;
    private String director;
    private String genre;
    private String plot;
    private String imdbRating;
    private String runtime;

    // Constructor for search results
    public Movie(String imdbID, String poster, String title, String year) {
        this.imdbID = imdbID;
        this.poster = poster;
        this.title = title;
        this.year = year;
    }

    // Constructor for movie details
    public Movie(String imdbID, String poster, String title, String year, String director, String genre, String plot, String imdbRating, String runtime) {
        this.imdbID = imdbID;
        this.poster = poster;
        this.title = title;
        this.year = year;
        this.director = director;
        this.genre = genre;
        this.plot = plot;
        this.imdbRating = imdbRating;
        this.runtime = runtime;
    }

    // Constructor for create Instance
    public Movie(){}

    // Getters and setters
    public String getImdbID(){return imdbID;}
    public void setImdbID(String imdbID){this.imdbID = imdbID;}
    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getDescription() { return plot; }
    public void setDescription(String plot) { this.plot = plot; }
    public String getImdbRating() { return imdbRating; }
    public void setImdbRating(String imdbRating) { this.imdbRating = imdbRating; }
    public String getRuntime(){return runtime;}
    public void setRuntime(String runtime) {this.runtime=runtime;}

}