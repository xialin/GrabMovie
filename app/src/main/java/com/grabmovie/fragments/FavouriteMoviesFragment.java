package com.grabmovie.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.grabmovie.Constants;
import com.grabmovie.apis.MovieSummary;
import com.grabmovie.models.FavouriteMovie;
import com.grabmovie.models.RealmManager;
import com.grabmovie.utils.GmLogger;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by xialin on 15/3/16.
 *
 */
public class FavouriteMoviesFragment extends AllMoviesFragment {

    private static final String TAG = "FavouriteMoviesFragment";

    private Realm mRealm;

    private BroadcastReceiver mBroadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = RealmManager.getInstance();
        GmLogger.d(TAG, "Load Realm instance");

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                GmLogger.d(TAG, "receive broadcast event");
                loadMovies(1);
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                mBroadcastReceiver, new IntentFilter(Constants.BROADCAST_EVENT_FAVOURITE_UPDATE));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close(); // important to close the instance
        }
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void loadMovies(int page) {
        GmLogger.d(TAG, "load favourite movies");
        if (mRealm == null) return;

        mRealm.beginTransaction();
        RealmResults<FavouriteMovie> movies = mRealm.where(FavouriteMovie.class).findAll();
        ArrayList<MovieSummary> movieSummaries = new ArrayList<>();
        for (FavouriteMovie movie : movies) {
            movieSummaries.add(new MovieSummary(movie));
        }
        mRealm.commitTransaction();

        if (mGridAdapter != null) {
            mGridAdapter.resetData(movieSummaries);
        }
    }

    @Override
    protected void enableEndlessScroll() {
        GmLogger.d(TAG, "disable endless scroll in favourite page");
    }
}
