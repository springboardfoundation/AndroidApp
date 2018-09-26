package com.android.springboard.neednetwork.activities;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.fragments.LoginFragment;
import com.android.springboard.neednetwork.utils.SharedPrefsUtils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import static com.android.springboard.neednetwork.constants.ActivityConstants.PREF_MOBILE_NUMBER;

public class LoginActivity extends BaseActivity {

    private LoginFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String mobileNumber = SharedPrefsUtils.getStringPreference(this, PREF_MOBILE_NUMBER);
        if (mobileNumber != null && !mobileNumber.isEmpty()) {
            Intent intent = new Intent(this, NeedTabsActivity.class);
            startActivity(intent);

            finish();
            return;
        }

        FragmentManager fragmentManager = getFragmentManager();
        mFragment = (LoginFragment) fragmentManager.findFragmentById(R.id.fragment);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new TedPermission(this)
                .setPermissionListener(mPermissionlistener)
                .setDeniedMessage("If you reject permission, app cannot read received code via SMS")
                .setPermissions(Manifest.permission.READ_SMS)
                .check();
    }

    PermissionListener mPermissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(LoginActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_need_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.done:
                mFragment.handleOk();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
