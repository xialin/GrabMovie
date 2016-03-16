package com.grabmovie.apis;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grabmovie.utils.GmLogger;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by xialin on 15/3/16.
 *
 */
public class GetMoviesHttpRequest extends AsyncTask<Integer, Void, Movies> {

    private static final String TAG = "GetMoviesHttpRequest";

    private GetMoviesHttpRequestListener mListener;

    public interface GetMoviesHttpRequestListener {
        void onSuccess(Movies movies);
        void onFailure(int err);
    }

    public GetMoviesHttpRequest(GetMoviesHttpRequestListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //
    }

    @Override
    protected Movies doInBackground(Integer... params) {
        try {
            Integer page = params[0];
            Response response = GmAPIs.getMovies(page);
            String responseStr = response.body().string();
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(responseStr, Movies.class);

        } catch (IOException e) {
            GmLogger.e(TAG, e, "Failed to parse response.");
            return null;
        }
    }

    @Override
    protected void onPostExecute(Movies movies) {
        super.onPostExecute(movies);
        if (mListener == null) return;

        if (movies == null) {
            mListener.onFailure(HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        }

        mListener.onSuccess(movies);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        //
    }
}
