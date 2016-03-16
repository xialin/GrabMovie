package com.grabmovie.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.grabmovie.Constants;
import com.grabmovie.R;
import com.grabmovie.activities.MovieActivity;
import com.grabmovie.adapters.ImageAdapter;
import com.grabmovie.apis.GetMoviesRequest;
import com.grabmovie.apis.GetMoviesResponse;
import com.grabmovie.apis.MovieSummary;
import com.grabmovie.utils.GmLogger;

/**
 * Created by xialin on 15/3/16.
 *
 */
public class AllMoviesFragment extends Fragment {

    private static final String TAG = "AllMoviesFragment";

    private GridView mGridView;

    private ImageAdapter mGridAdapter;

    protected int mTotalPages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_all_movies, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.movie_grid);
        mGridAdapter = new ImageAdapter(getActivity());
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                GmLogger.d(TAG, "onLoadMore next page: %d/%d", page, mTotalPages);
                loadMovies(page);
                return page <= mTotalPages;
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                MovieSummary movie = (MovieSummary) parent.getItemAtPosition(position);

                Intent intent = new Intent(AllMoviesFragment.this.getActivity(), MovieActivity.class);
                intent.putExtra(Constants.INTENT_KEY_MOVIE_ID, movie.getId());
                startActivity(intent);
            }
        });

        loadMovies(1);

        return rootView;
    }

    protected void loadMovies(int page) {
        GmLogger.d(TAG, "loadMovies at page: %d", page);
        new GetMoviesRequest(new GetMoviesRequest.GetMoviesHttpRequestListener() {
            @Override
            public void onSuccess(GetMoviesResponse movies) {
                GmLogger.d(TAG, "loadMovies.onSuccess");
                updateGridView(movies);
            }

            @Override
            public void onFailure(int err) {
                GmLogger.d(TAG, "loadMovies.onFailure");
                // TODO: show empty view
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, page);
    }

    protected void updateGridView(GetMoviesResponse movies) {
        mGridAdapter.pushData(movies.getMovies());
        mTotalPages = movies.getTotalPages();
    }

    /**
     * Movie GridView Pagination
     *
     * Ref: https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews
     */
    private abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
        // The minimum amount of items to have below your current scroll position before loading more.
        private int visibleThreshold = 5;
        // The current offset index of data you have loaded
        private int currentPage = 0;
        // The total number of items in the dataset after the last load
        private int previousTotalItemCount = 0;
        // True if we are still waiting for the last set of data to load.
        private boolean loading = true;
        // Sets the starting page index
        private int startingPageIndex = 0;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        public EndlessScrollListener(int visibleThreshold, int startPage) {
            this.visibleThreshold = visibleThreshold;
            this.startingPageIndex = startPage;
            this.currentPage = startPage;
        }

        // This happens many times a second during a scroll, so be wary of the code you place here.
        // We are given a few useful parameters to help us work out if we need to load some more data,
        // but first we check if we are waiting for the previous load to finish.
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    this.loading = true;
                }
            }
            // If it's still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
                currentPage++;
            }

            // If it isn't currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                loading = onLoadMore(currentPage + 1, totalItemCount);
            }
        }

        // Defines the process for actually loading more data based on page
        // Returns true if more data is being loaded; returns false if there is no more data to load.
        public abstract boolean onLoadMore(int page, int totalItemsCount);

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // Don't take any action on changed
        }
    }
}
