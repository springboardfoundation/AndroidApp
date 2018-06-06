package com.android.springboard.neednetwork.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.android.springboard.neednetwork.models.Need;

import java.util.List;

/**
 * Created by Shouib on 8/5/2017.
 */

public class NeedNetApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static volatile Context applicationContext;
    private static Need mNeed;
    private static List<Need> mMyNeedList;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();
    }

    public static Need getNeed() {
        return mNeed;
    }

    public static void setNeed(Need mNeed) {
        NeedNetApplication.mNeed = mNeed;
    }

    public static void setMyNeedList(List<Need> myNeedList) {
        mMyNeedList = myNeedList;
    }

    public static List<Need> getMyNeedList() {
        return mMyNeedList;
    }
}
