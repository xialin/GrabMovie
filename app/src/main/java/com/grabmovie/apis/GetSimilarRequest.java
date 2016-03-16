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
 * Get related movies
 */
public class GetSimilarRequest extends AsyncTask <Integer, Void, GetMoviesResponse> {

    private static final String TAG = "GetSimilarRequest";

    private GetSimilarRequestListener mListener;

    public interface GetSimilarRequestListener {
        void onSuccess(GetMoviesResponse movies);
        void onFailure(int err);
    }

    public GetSimilarRequest(GetSimilarRequestListener listener) {
        mListener = listener;
    }

    @Override
    protected GetMoviesResponse doInBackground(Integer... params) {
        try {
            Integer movieId = params[0];
            Response response = GmAPIs.getMovieSimilars(movieId, 1); // TODO: currently only get some related movies
            if (response.isSuccessful()) {
                String responseStr = response.body().string();
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(responseStr, GetMoviesResponse.class);

            } else {
                GmLogger.d(TAG, "GetSimilarRequest response: %s", response.body().toString());
                return null;
            }
        } catch (IOException e) {
            GmLogger.e(TAG, e, "Failed to parse response.");
            return null;
        }
    }

    @Override
    protected void onPostExecute(GetMoviesResponse movies) {
        super.onPostExecute(movies);

        if (mListener == null) return;

        if (movies != null) {
            mListener.onSuccess(movies);
        } else {
            mListener.onFailure(HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }
}
