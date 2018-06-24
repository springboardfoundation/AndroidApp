package com.android.springboard.neednetwork.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.android.springboard.neednetwork.models.Need;
import com.android.springboard.neednetwork.receivers.FireBaseReceiver;
import com.deltadna.android.sdk.DDNA;
import com.deltadna.android.sdk.notifications.DDNANotifications;

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

        DDNA.initialise(new DDNA.Configuration(
                this,
                "07575004106474324897044893014183",
                "http://collect3347ndrds.deltadna.net/collect/api",
                "http://engage3347ndrds.deltadna.net"));

        // only needs to be called if targeting API 26 or higher
        DDNANotifications.setReceiver(FireBaseReceiver.class);
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
