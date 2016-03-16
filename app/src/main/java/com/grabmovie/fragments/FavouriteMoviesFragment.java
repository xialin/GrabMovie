package com.grabmovie.fragments;

import android.os.AsyncTask;

import com.grabmovie.apis.GetMoviesRequest;
import com.grabmovie.apis.GetMoviesResponse;
import com.grabmovie.utils.GmLogger;

/**
 * Created by xialin on 15/3/16.
 *
 */
public class FavouriteMoviesFragment extends AllMoviesFragment {

    private static final String TAG = "FavouriteMoviesFragment";

    @Override
    public void loadMovies(int page) {
        GmLogger.d(TAG, "load favourite movies at page: %d", page);
        new GetMoviesRequest(new GetMoviesRequest.GetMoviesHttpRequestListener() {
            @Override
            public void onSuccess(GetMoviesResponse movies) {
                GmLogger.d(TAG, "loadMovies.onSuccess");
                updateGridView(movies);
                mTotalPages = 1;
            }

            @Override
            public void onFailure(int err) {
                GmLogger.d(TAG, "loadMovies.onFailure");
                // TODO: show empty view
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, page);
    }
}
