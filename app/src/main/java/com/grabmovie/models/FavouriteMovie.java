package com.grabmovie.models;

import io.realm.RealmObject;

/**
 * Created by xialin on 16/3/16.
 *
 */
public class FavouriteMovie extends RealmObject {
    private int movieId;
    private String posterPath;

    public FavouriteMovie() {

    }

    public FavouriteMovie(int movieId, String posterPath) {
        this.movieId = movieId;
        this.posterPath = posterPath;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
