package com.example.android.imagesviewer.utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Project name ImagesViewer
 * Created by kenneth on 16/10/2017.
 */

public class FirebaseInstance {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
