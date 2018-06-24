package com.android.springboard.neednetwork.activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.fragments.OthersNeedFragment;

public class OthersNeedActivity extends AppCompatActivity {

    private OthersNeedFragment mOthersNeedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_need);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        mOthersNeedFragment = (OthersNeedFragment) fragmentManager.findFragmentById(R.id.others_need_fragment);
    }

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
                mOthersNeedFragment.addUsers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
