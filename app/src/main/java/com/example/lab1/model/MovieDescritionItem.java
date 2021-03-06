package com.example.lab1.model;

public class MovieDescritionItem {

    private String Title;
    private String Rated;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Language;
    private String Country;
    private String Awards;
    private String Year;
    private String imdbRating;
    private String imdbVotes;
    private String imdbID;
    private String Type;
    private String Poster;
    private String Production;

    public String getTitle() {
        return Title;
    }

    public String getRated() {
        return Rated;
    }

    public String getReleased() {
        return Released;
    }

    public String getRuntime() {
        return Runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public String getDirector() {
        return Director;
    }

    public String getWriter() {
        return Writer;
    }

    public String getActors() {
        return Actors;
    }

    public String getPlot() {
        return Plot;
    }

    public String getLanguage() {
        return Language;
    }

    public String getCountry() {
        return Country;
    }

    public String getAwards() {
        return Awards;
    }

    public String getYear() {
        return Year;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getType() {
        return Type;
    }

    public String getPoster() {
        return Poster;
    }

    public String getProduction() {
        return Production;
    }

    public String toString() {
        return "Title: " + Title +
                "\nYear: " + Year +
                "\nGenre: " + Genre +
                "\n\nDirector: " + Director +
                "\nActors: " + Actors +
                "\n\nCountry: " + Country +
                "\nLanguage: " + Language +
                "\nProduction: " + Production +
                "\nReleased: " + Released +
                "\nRuntime: " + Runtime +
                "\n\nAwards: " + Awards +
                "\nRating: " + Rated +
                "\nPlot: " + Plot;
    }
}
