package com.android.springboard.neednetwork.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.android.springboard.neednetwork.models.Need;

/**
 * Created by Shouib on 8/5/2017.
 */

public class ApplicationLoader extends Application {

    @SuppressLint("StaticFieldLeak")
    public static volatile Context applicationContext;
    private static Need mNeed;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();
    }

    public static Need getNeed() {
        return mNeed;
    }

    public static void setNeed(Need mNeed) {
        ApplicationLoader.mNeed = mNeed;
    }
}
