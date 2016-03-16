package com.grabmovie.apis;

/**
 * Created by xialin on 16/3/16.
 *
 */
public class Movie {
    int id;
    String imdb_id;
    boolean adult;
    String backdrop_path;
    int budget;
    Genre[] genres;
    String homepage;
    String original_language;
    String original_title;
    String overview;
    float popularity;
    String poster_path;
    Company[] production_companies;
    String release_date;
    int revenue;
    int runtime;
    Language[] spoken_languages;
    String status;
    String tagline;
    String title;
    boolean video;
    float vote_average;
    int vote_count;

    public int getId() {
        return id;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getTitle() {
        return title;
    }

    public String getTagline() {
        return tagline;
    }

    public String getOverview() {
        return overview;
    }

    public float getVoteAverage() {
        return vote_average;
    }

    public int getVoteCount() {
        return vote_count;
    }

    private class Genre {
        int id;
        String name;
    }

    private class Company {
        int id;
        String name;
    }

    private class Language {
        String iso_639_1;
        String name;
    }
}
