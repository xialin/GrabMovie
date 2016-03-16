package com.grabmovie.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.grabmovie.Constants;
import com.grabmovie.R;
import com.grabmovie.adapters.ImageAdapter;
import com.grabmovie.apis.GetMovieRequest;
import com.grabmovie.apis.GetMoviesResponse;
import com.grabmovie.apis.GetSimilarRequest;
import com.grabmovie.apis.GmAPIs;
import com.grabmovie.apis.Movie;
import com.grabmovie.apis.MovieSummary;
import com.grabmovie.utils.GmLogger;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Movie screen showing movie details
 *
 */
public class MovieActivity extends Activity {

    private static final String TAG = "MovieActivity";

    private View mRootView, mContainer, mEmptyView;
    private ImageView mBackdropView, mPosterView;
    private TextView mTitleView, mTaglineView, mOverviewView;
    private GridView mSimilarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mRootView = findViewById(R.id.root_view);

        mEmptyView = findViewById(R.id.empty_view);

        mContainer = findViewById(R.id.container);

        int movieId = 0;
        if (getIntent() != null) {
            movieId = getIntent().getIntExtra(Constants.INTENT_KEY_MOVIE_ID, 0);
        }

        loadView(movieId);
    }

    private void loadView(int movieId) {
        GmLogger.d(TAG, "load movie: %d", movieId);

        if (movieId <= 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            return;
        }

        loadMovieInfo(movieId);

        loadRelatedMovies(movieId);
    }

    private void loadMovieInfo(final int movieId) {
        mBackdropView = (ImageView) findViewById(R.id.backdrop_img);

        mPosterView = (ImageView) findViewById(R.id.poster_img);
        mTitleView = (TextView) findViewById(R.id.title);
        mTaglineView = (TextView) findViewById(R.id.tagline);

        mOverviewView = (TextView) findViewById(R.id.overview);

        View infoBtn = findViewById(R.id.info_btn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GmLogger.d(TAG, "click info button");
                // TODO: scroll to trailers section
            }
        });
        View similarBtn = findViewById(R.id.similar_btn);
        similarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GmLogger.d(TAG, "click similar button");
                mRootView.scrollTo(0, mSimilarView.getBottom());
            }
        });
        View favorBtn = findViewById(R.id.favor_btn);
        favorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavourite = v.isSelected();
                v.setSelected(!isFavourite);
                favorMovie(movieId, !isFavourite);
            }
        });

        new GetMovieRequest(new GetMovieRequest.GetMovieRequestListener() {
            @Override
            public void onSuccess(Movie movie) {
                GmLogger.d(TAG, "onSuccess: %s", movie.getTitle());
                ImageLoader.getInstance().displayImage(GmAPIs.getBackdropUrl(movie.getBackdropPath()), mBackdropView);
                ImageLoader.getInstance().displayImage(GmAPIs.getBackdropUrl(movie.getPosterPath()), mPosterView);
                mTitleView.setText(movie.getTitle());
                mTaglineView.setText(movie.getTagline());
                mOverviewView.setText(movie.getOverview() + movie.getOverview() + movie.getOverview());
                fadeInView(false);
            }

            @Override
            public void onFailure(int err) {
                GmLogger.d(TAG, "onFailure: %d", err);
                fadeInView(true);
            }
        }).execute(movieId);
    }

    private void loadRelatedMovies(int movieId) {
        final ImageAdapter gridAdapter = new ImageAdapter(this);
        mSimilarView = (GridView) findViewById(R.id.related_movies);
        mSimilarView.setAdapter(gridAdapter);
        mSimilarView.setEmptyView(findViewById(R.id.empty_grid_view));
        mSimilarView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                MovieSummary movie = (MovieSummary) parent.getItemAtPosition(position);

                Intent intent = new Intent(MovieActivity.this, MovieActivity.class);
                intent.putExtra(Constants.INTENT_KEY_MOVIE_ID, movie.getId());
                startActivity(intent);
            }
        });

        new GetSimilarRequest(new GetSimilarRequest.GetSimilarRequestListener() {
            @Override
            public void onSuccess(GetMoviesResponse data) {
                gridAdapter.pushData(data.getMovies());
            }

            @Override
            public void onFailure(int err) {

            }
        }).execute(movieId);
    }

    /**
     * Display movie info
     * TODO: with fade in animation
     * @param isEmpty if true, show empty view e.g. Movie not found.
     */
    private void fadeInView(boolean isEmpty) {
        if (!isEmpty) {
            mContainer.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mContainer.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Mark movie as favourite
     * @param movieId movie ID
     * @param isFavourite true if mark as favourite, false if un-mark
     */
    private void favorMovie(int movieId, boolean isFavourite) {

    }
}
