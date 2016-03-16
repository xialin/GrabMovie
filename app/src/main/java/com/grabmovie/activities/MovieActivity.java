package com.grabmovie.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import com.grabmovie.models.FavouriteMovie;
import com.grabmovie.models.RealmManager;
import com.grabmovie.utils.GmLogger;
import com.nostra13.universalimageloader.core.ImageLoader;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Movie screen showing movie details
 *
 */
public class MovieActivity extends Activity {

    private static final String TAG = "MovieActivity";

    private Realm mRealm;

    private View mRootView, mContainer, mEmptyView;
    private ImageView mBackdropView, mPosterView;
    private TextView mTitleView, mTaglineView, mRatingView, mOverviewView;
    private GridView mSimilarView;

    @Override
    public void onStart() {
        super.onStart();
        mRealm = RealmManager.getInstance();
        GmLogger.d(TAG, "Load Realm instance");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRealm != null) {
            mRealm.close(); // important to close the instance
        }
    }

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
        mRatingView = (TextView) findViewById(R.id.rating);
        mTaglineView = (TextView) findViewById(R.id.tagline);

        mOverviewView = (TextView) findViewById(R.id.overview);

        new GetMovieRequest(new GetMovieRequest.GetMovieRequestListener() {
            @Override
            public void onSuccess(final Movie movie) {
                GmLogger.d(TAG, "onSuccess: %s", movie.getTitle());

                if (movie.getBackdropPath() == null) {
                    mBackdropView.setVisibility(View.GONE);
                } else {
                    ImageLoader.getInstance().displayImage(
                            GmAPIs.getBackdropUrl(movie.getBackdropPath()), mBackdropView);
                }

                if (movie.getPosterPath() == null) {
                    mPosterView.setVisibility(View.GONE);
                } else {
                    ImageLoader.getInstance().displayImage(
                            GmAPIs.getBackdropUrl(movie.getPosterPath()), mPosterView);
                }

                mTitleView.setText(movie.getTitle());
                mRatingView.setText(String.format("%.1f/10 (%d votes)",
                        movie.getVoteAverage(), movie.getVoteCount()));
                mTaglineView.setText(movie.getTagline());
                mOverviewView.setText(movie.getOverview());

                loadButtons(movieId, GmAPIs.getBackdropUrl(movie.getPosterPath()));

                fadeInView(false);
            }

            @Override
            public void onFailure(int err) {
                GmLogger.d(TAG, "onFailure: %d", err);
                fadeInView(true);
            }
        }).execute(movieId);
    }

    /**
     * Load Info | Related | Favor buttons
     * @param movieId movie ID
     * @param moviePosterUrl movie poster URL
     */
    private void loadButtons(final int movieId, final String moviePosterUrl) {
        final View infoBtn = findViewById(R.id.info_btn);

        final View similarBtn = findViewById(R.id.similar_btn);

        final View favorBtn = findViewById(R.id.favor_btn);

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GmLogger.d(TAG, "click info button");
                // TODO: scroll to trailers section
            }
        });
        similarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GmLogger.d(TAG, "click similar button");
                mRootView.scrollTo(0, mSimilarView.getBottom());
            }
        });
        favorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavourite = v.isSelected();
                v.setSelected(!isFavourite);
                favorMovie(movieId, moviePosterUrl, !isFavourite);
            }
        });
        if (mRealm != null) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    FavouriteMovie movie = realm.where(FavouriteMovie.class)
                            .equalTo("movieId", movieId).findFirst();
                    if (movie != null) {
                        favorBtn.setSelected(true);
                    }
                }
            });
        }
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
    private void favorMovie(final int movieId, final String url, boolean isFavourite) {
        if (mRealm == null) return;

        if (isFavourite) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    FavouriteMovie movie = realm.where(FavouriteMovie.class)
                            .equalTo("movieId", movieId).findFirst();
                    if (movie == null) {
                        movie = new FavouriteMovie(movieId, url);
                        GmLogger.d(TAG, "add favourite movie %s (%d)", url, movieId);
                        realm.copyToRealm(movie);
                    }
                }
            });
        } else {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<FavouriteMovie> movies = realm.where(FavouriteMovie.class)
                            .equalTo("movieId", movieId).findAll();
                    GmLogger.d(TAG, "delete favourite movie %s (%d)", url, movieId);
                    movies.clear();
                }
            });
        }

        broadcastFavouriteUpdate();
    }

    private void broadcastFavouriteUpdate() {
        GmLogger.d(TAG, "send broadcast event");
        LocalBroadcastManager.getInstance(this).sendBroadcast(
                new Intent(Constants.BROADCAST_EVENT_FAVOURITE_UPDATE));
    }
}
