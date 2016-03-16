package com.grabmovie.apis;

/**
 * Created by xialin on 15/3/16.
 *
 * {
 *    "adult": false,
 *    "backdrop_path": "/99qMHgawGX9QWx6zllecsMbbLkj.jpg",
 *    "id": 17169,
 *    "original_title": "Any Which Way You Can",
 *    "release_date": "1980-12-17",
 *    "poster_path": "/fcwYEVdBa251yqeuTfFQXHlMrHt.jpg",
 *    "popularity": 0.612450827242979,
 *    "title": "Any Which Way You Can",
 *    "vote_average": 6.6,
 *    "vote_count": 9
 * }
 */
public class Movie {
    int id;
    String poster_path;
    String title;
    float vote_average;
    int vote_count;

    boolean adult;
    float popularity;
    String backdrop_path;
    String original_title;
    String release_date;

    // additional fields in full details
    int[] genre_ids;
    String original_language;
    String overview;
    boolean video;

    public String getPosterPath() {
        return poster_path;
    }
}
