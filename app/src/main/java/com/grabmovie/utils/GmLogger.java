package com.grabmovie.utils;

import android.util.Log;

/**
 * Created by xialin on 15/3/16.
 *
 * Base class for logging
 *
 * If we want to send logs to other tracking channels later, we can just modify this class
 * For example, we can send warning and error logs to Crashlytics for crash investigation
 */
public class GmLogger {

    public static void v(String tag, String message, Object... args) {
        Log.v(tag, String.format("GM|V: " + message, args));
    }

    public static void d(String tag, String message, Object... args) {
        Log.d(tag, String.format("GM|D: " + message, args));
    }


    public static void i(String tag, String message, Object... args) {
        Log.i(tag, String.format("GM|I: " + message, args));
    }


    public static void w(String tag, String message, Object... args) {
        Log.w(tag, String.format("GM|W: " + message, args));
    }


    public static void e(String tag, String message, Object... args) {
        Log.e(tag, String.format("GM|E: " + message, args));
    }

    public static void e(String tag, Throwable t, String message, Object... args) {
        Log.e(tag, String.format("GM|E: " + message, args), t);
    }
}
