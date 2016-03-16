package com.grabmovie.apis;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grabmovie.utils.GmLogger;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by xialin on 16/3/16.
 *
 * Get Movie Details Request
 */
public class GetMovieRequest extends AsyncTask<Integer, Void, Movie> {

    private static final String TAG = "GetMovieHttpRequest";

    private GetMovieRequestListener mListener;

    public interface GetMovieRequestListener {
        void onSuccess(Movie movie);
        void onFailure(int err);
    }

    public GetMovieRequest(GetMovieRequestListener listener) {
        mListener = listener;
    }

    @Override
    protected Movie doInBackground(Integer... params) {
        try {
            Integer movieId = params[0];
            Response response = GmAPIs.getMovie(movieId);
            if (response.isSuccessful()) {
                String responseStr = response.body().string();
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(responseStr, Movie.class);

            } else {
                GmLogger.d(TAG, "GetMovie response: %s", response.body().toString());
                return null;
            }
        } catch (IOException e) {
            GmLogger.e(TAG, e, "Failed to parse response.");
            return null;
        }
    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        GmLogger.d(TAG, "Get Movie: %s", movie);

        if (mListener == null) return;

        if (movie != null) {
            mListener.onSuccess(movie);
        } else {
            mListener.onFailure(HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }
}
