package com.android.springboard.neednetwork.activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.fragments.NeedFragment;

public class NeedActivity extends AppCompatActivity {

    private NeedFragment mNeedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        mNeedFragment = (NeedFragment) fragmentManager.findFragmentById(R.id.add_need_fragment);
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
            case R.id.add_need:
                mNeedFragment.addNeed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
