package com.grabmovie.apis;

import android.os.Parcel;
import android.os.Parcelable;

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
public class MovieSummary implements Parcelable {
    int id;
    String poster_path;
    String title;
    float vote_average;
    int vote_count;

    private MovieSummary(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(poster_path);
    }

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        poster_path = in.readString();
    }

    public static final Parcelable.Creator<MovieSummary> CREATOR = new Parcelable.Creator<MovieSummary>() {
        public MovieSummary createFromParcel(Parcel in) {
            return new MovieSummary(in);
        }

        public MovieSummary[] newArray(int size) {
            return new MovieSummary[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return poster_path;
    }
}
