package com.android.springboard.neednetwork.receivers;

import android.content.Context;
import android.util.Log;

import com.deltadna.android.sdk.BuildConfig;
import com.deltadna.android.sdk.notifications.EventReceiver;
import com.deltadna.android.sdk.notifications.NotificationInfo;
import com.deltadna.android.sdk.notifications.PushMessage;

public class FireBaseReceiver extends EventReceiver {

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.d(BuildConfig.LOG_TAG, "onRegistered with: " + registrationId);
    }

    @Override
    protected void onRegistrationFailed(Context context, Throwable reason) {
        Log.w(BuildConfig.LOG_TAG, "onRegistrationFailed with: " + reason);
    }

    @Override
    protected void onMessageReceived(Context context, PushMessage message) {
        Log.d(BuildConfig.LOG_TAG, "onMessageReceived with: " + message);
    }

    @Override
    protected void onNotificationPosted(Context context, NotificationInfo info) {
        Log.d(BuildConfig.LOG_TAG, "onNotificationPosted with: " + info);
    }

    @Override
    protected void onNotificationOpened(Context context, NotificationInfo info) {
        Log.d(BuildConfig.LOG_TAG, "onNotificationOpened with: " + info);
    }

    @Override
    protected void onNotificationDismissed(Context context, NotificationInfo info) {
        Log.d(BuildConfig.LOG_TAG, "onNotificationDismissed with: " + info);
    }
}
