package com.grabmovie.apis;

/**
 * Created by xialin on 15/3/16.
 *
 */
public class GetVideosResponse {
    int id;
    Video[] results;

    public Video[] getVideos() {
        return results;
    }
}
