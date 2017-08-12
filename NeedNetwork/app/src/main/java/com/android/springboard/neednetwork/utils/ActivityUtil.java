package com.android.springboard.neednetwork.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Shouib on 7/1/2017.
 */

public class ActivityUtil {

    public static void startActivity(Activity currentActivity, Class<?> klass) {
        Intent intent = new Intent();
        intent.setClass(currentActivity, klass);
        currentActivity.startActivity(intent);
    }
}
