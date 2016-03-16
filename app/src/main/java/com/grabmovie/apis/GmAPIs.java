package com.grabmovie.apis;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by xialin on 15/3/16.
 *
 * Base class for API calls
 */
public class GmAPIs {
    private static final String TAG = "GmAPIs";

    // URLs
    private static final String API_KEY = "0d2345b81e3af3cd114d88e6562d135e";

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String NOW_PLAYING_URL = BASE_URL + "now_playing";

    private static final String VIDEO_URL = BASE_URL + "%d/videos";

    private static final String SIMILAR_URL = BASE_URL + "%d/similar";

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";

    private static final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    // request params
    private static final String PARAM_API_KEY = "api_key";

    private static final String PARAM_PAGE = "page";

    // timeout constants
    private static final int CONNECTION_TIMEOUT = 30;

    private static final int WRITE_TIMEOUT = 30;

    private static final int READ_TIMEOUT = 30;

    // header type
    private static final String CONTENT_TYPE = "Content-Type";

    private static final String APPLICATION_JSON = "application/json; charset=utf-8";


    private final OkHttpClient mClient;

    private static GmAPIs sInstance;

    public static GmAPIs getInstance() {
        if (sInstance == null) {
            sInstance = new GmAPIs();
        }
        return sInstance;
    }

    public static OkHttpClient getClient() {
        return GmAPIs.getInstance().mClient;
    }

    private GmAPIs() {
        mClient = new OkHttpClient();
        mClient.setRetryOnConnectionFailure(true);
        mClient.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        mClient.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        mClient.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
    }

    public static String getPosterUrl(String path) {
        return POSTER_BASE_URL + path;
    }

    public static String getBackdropUrl(String path) {
        return BACKDROP_BASE_URL + path;
    }

    public static String getTrailerUrl(String key) {
        return YOUTUBE_BASE_URL + key;
    }

    /**
     * [GET] now-playing movie list
     *
     * @param page page number
     * @return movie list
     * @throws IOException
     */
    public static Response getMovies(int page) throws IOException {
        HttpUrl url = HttpUrl
                .parse(NOW_PLAYING_URL)
                .newBuilder()
                .addQueryParameter(PARAM_API_KEY, API_KEY)
                .addQueryParameter(PARAM_PAGE, String.valueOf(page))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        return GmAPIs.getClient().newCall(request).execute();
    }

    /**
     * [GET] movie details
     *
     * @param movieId movie ID
     * @return movie details
     * @throws IOException
     */
    public static Response getMovie(int movieId) throws IOException {
        HttpUrl url = HttpUrl.parse(BASE_URL + movieId)
                .newBuilder().addQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        return GmAPIs.getClient().newCall(request).execute();
    }

    /**
     * [GET] videos of selected movie
     * @param movieId movie ID
     * @return a list of trailers
     * @throws IOException
     */
    public static Response getMovieTrailers(int movieId) throws IOException {
        HttpUrl url = HttpUrl
                .parse(String.format(VIDEO_URL, movieId))
                .newBuilder()
                .addQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        return GmAPIs.getClient().newCall(request).execute();
    }

    /**
     * [GET] similar movies
     * @param movieId movie ID
     * @param page page number
     * @return similar movie list
     * @throws IOException
     */
    public static Response getMovieSimilars(int movieId, int page) throws IOException {
        HttpUrl url = HttpUrl
                .parse(String.format(SIMILAR_URL, movieId))
                .newBuilder()
                .addQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        return GmAPIs.getClient().newCall(request).execute();
    }
}
