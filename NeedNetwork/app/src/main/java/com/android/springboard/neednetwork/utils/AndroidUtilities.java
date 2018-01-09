package com.android.springboard.neednetwork.utils;

import android.content.Context;

/**
 * Created by Shouib on 9/23/2017.
 */

public class AndroidUtilities {

    public static int dp(float value, Context context) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * value);
    }
}
