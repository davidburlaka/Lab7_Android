package com.example.lab1.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class MovieEntity {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "search")
    private String search;

    @ColumnInfo(name = "title")
    private String Title;

    @ColumnInfo(name = "year")
    private String Year;

    @ColumnInfo(name = "imdbid")
    private String imdbID;

    @ColumnInfo(name = "type")
    private String Type;

    @ColumnInfo(name = "poster")
    private String Poster;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public MovieItem toItem() {
        MovieItem result = new MovieItem();
        result.setTitle(Title);
        result.setYear(Year);
        result.setType(Type);
        result.setPoster(Poster);
        result.setImdbID(imdbID);
        return result;
    }

    public String toString() {
        return "{\"Search\":\"" + search + "\"," +
                "\"Title\":\"" + Title + "\"," +
                "\"Year\":\"" + Year + "\"," +
                "\"imdbID\": \""+ imdbID + "\"," +
                "\"Type\":\"" + Type + "\"," +
                "\"Poster\":\"" + Poster + "\"}";
    }
}
