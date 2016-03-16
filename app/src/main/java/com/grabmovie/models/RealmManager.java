package com.grabmovie.models;

import android.content.Context;
import android.support.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by xialin on 16/3/16.
 *
 * For initialization and obtaining Realm instance
 */
public class RealmManager {
    private static final int DATABASE_SCHEMA_VERSION = 1;
    private static boolean mInitialized = false;

    private RealmManager() {
    }

    public synchronized static void initialize(@NonNull Context context) {
        if (!mInitialized) {
            RealmConfiguration config = new RealmConfiguration
                    .Builder(context)
                    .schemaVersion(DATABASE_SCHEMA_VERSION)
                    .build();
            Realm.setDefaultConfiguration(config);
            mInitialized = true;
        }
    }

    /**
     * @return default instance of Realm that was initialized in initialize() method.
     */
    public static Realm getInstance() {
        if (!mInitialized) {
            throw new IllegalStateException("Need to call initialize() before accessing instance");
        }

        return Realm.getDefaultInstance();
    }
}
