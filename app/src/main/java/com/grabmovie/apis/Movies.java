package com.grabmovie.apis;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by xialin on 15/3/16.
 *
 */
public class Movies {
    int page;
    int total_pages;
    int total_results;
    Movie[] results;

    public ArrayList<Movie> getMovies() {
        return new ArrayList<>(Arrays.asList(results));
    }

    public int getTotalPages() {
        return total_pages;
    }
}
