package com.android.springboard.neednetwork.application;

import android.app.Application;

import com.android.springboard.neednetwork.models.Need;

/**
 * Created by Shouib on 8/5/2017.
 */

public class NeedApplication extends Application {

    private static Need mNeed;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Need getNeed() {
        return mNeed;
    }

    public static void setNeed(Need mNeed) {
        NeedApplication.mNeed = mNeed;
    }
}
