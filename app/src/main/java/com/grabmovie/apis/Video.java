package com.grabmovie.apis;

/**
 * Created by xialin on 15/3/16.
 * {
 *    "id": "533ec654c3a36854480003eb",
 *    "iso_639_1": "en",
 *    "key": "SUXWAEX2jlg",
 *    "name": "Trailer 1",
 *    "site": "YouTube",
 *    "size": 720,
 *    "type": "Trailer"
 * }
 */
public class Video {
    String id;
    String iso_639_1;
    String type;
    String site; // host
    String key; // path
    String name;
    int size;
}
