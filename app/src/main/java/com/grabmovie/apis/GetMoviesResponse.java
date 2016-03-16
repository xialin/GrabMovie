package com.grabmovie.apis;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by xialin on 15/3/16.
 *
 */
public class GetMoviesResponse {
    int page;
    int total_pages;
    int total_results;
    MovieSummary[] results;

    public ArrayList<MovieSummary> getMovies() {
        return new ArrayList<>(Arrays.asList(results));
    }

    public int getTotalPages() {
        return total_pages;
    }
}
