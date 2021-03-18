package com.example.lab1.model;

import android.media.Image;

public class MovieItem {
    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;

    public void setTitle(String title) {
        Title = title;
    }

    public void setYear(String year) {
        Year = year;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getType() {
        return Type;
    }

    public String getPoster() {
        return Poster;
    }

    public String getImdbID() { return imdbID; }

    public String toString() {
        return "{\"Title\":\"" + Title + "\"," +
                "\"Year\":\"" + Year + "\"," +
                "\"imdbID\": \""+ imdbID + "\"," +
                "\"Type\":\"" + Type + "\"," +
                "\"Poster\":\"" + Poster + "\"}";
    }

    public MovieEntity toEntity(String search) {
        MovieEntity result = new MovieEntity();
        result.setSearch(search);
        result.setTitle(Title);
        result.setYear(Year);
        result.setType(Type);
        result.setPoster(Poster);
        result.setImdbID(imdbID);
        System.out.println("Converting to entity " + search);
        return result;
    }
}
