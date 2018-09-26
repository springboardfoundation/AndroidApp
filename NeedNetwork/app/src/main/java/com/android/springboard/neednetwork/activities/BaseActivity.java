package com.android.springboard.neednetwork.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.deltadna.android.sdk.BuildConfig;
import com.deltadna.android.sdk.DDNA;
import com.deltadna.android.sdk.notifications.DDNANotifications;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DDNA.instance().startSdk();

        final String id = DDNA.instance().getUserId();
        Log.d(BuildConfig.LOG_TAG, "User id: " + id);
/*        ((TextView) findViewById(R.id.user_id)).setText(getString(
                R.string.user_id,
                id));*/
        DDNANotifications.register(this);
        showRegistrationToken();
    }

    @Override
    public void onDestroy() {
        DDNA.instance().stopSdk();
        DDNANotifications.unregister();

        super.onDestroy();
    }

    public void onUploadEvents(View view) {
        DDNA.instance().upload();
    }

    /**
     * Register for push notifications.
     * <p>
     * If you would like to handle the retrieval of the GCM registration token
     * manually then you can call {@link DDNA#setRegistrationId(String)}
     * instead.
     */
    public void onRegister(View view) {
        DDNANotifications.register(this);

        showRegistrationToken();
    }

    /**
     * Unregister from push notifications.
     * <p>
     * If you would like to handle the retrieval of the GCM registration token
     * manually then you can call {@link DDNA#clearRegistrationId()} instead.
     */
    public void onUnregister(View view) {
        DDNANotifications.unregister();
    }

    public void onStopSdk(View view) {
        DDNA.instance().stopSdk();
    }

    public void onStartSdk(View view) {
        DDNA.instance().startSdk();
    }

    private void showRegistrationToken() {
        final String token = DDNA.instance().getRegistrationId();
        Log.d(BuildConfig.LOG_TAG, "Registration token: " + token);
/*        ((TextView) findViewById(R.id.registration_token)).setText(getString(
                R.string.registration_token,
                token));*/
    }

    public void showProgressDialog(String message) {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(message);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (!isFinishing() && mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
