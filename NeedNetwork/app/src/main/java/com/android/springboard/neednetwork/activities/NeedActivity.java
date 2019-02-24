package com.android.springboard.neednetwork.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.fragments.FinancialNeedFragment;
import com.android.springboard.neednetwork.fragments.NeedFragment;
import com.android.springboard.neednetwork.fragments.NonFinancialNeedFragment;

import static com.android.springboard.neednetwork.constants.ActivityConstants.INTENT_EXTRA_NEED_TYPE;
import static com.android.springboard.neednetwork.constants.NeedConstants.FINANCIAL_NEED;
import static com.android.springboard.neednetwork.constants.NeedConstants.NON_FINANCIAL_NEED;

public class NeedActivity extends BaseActivity {

    private NeedFragment mNeedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent != null) {
            int needType = intent.getIntExtra(INTENT_EXTRA_NEED_TYPE, NON_FINANCIAL_NEED);
            initFragment(needType);
        }

        final View activityRootView = findViewById(R.id.root_layout);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if ((keyboardShown(activityRootView.getRootView()))) {
                    // keyboard is up
                    mNeedFragment.hideActionButton();
                } else {
                    // keyboard is down
                    mNeedFragment.showActionButton();
                }
            }
        });
    }

    private void initFragment(int needType) {
        if(needType == NON_FINANCIAL_NEED) {
            mNeedFragment = new NonFinancialNeedFragment();
            addFragment(R.id.root_layout,mNeedFragment,
                    NonFinancialNeedFragment.FRAGMENT_TAG);
        } else if(needType == FINANCIAL_NEED) {
            mNeedFragment = new FinancialNeedFragment();
            addFragment(R.id.root_layout,
                    mNeedFragment,
                    FinancialNeedFragment.FRAGMENT_TAG);
        }
    }

    private boolean keyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
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
                mNeedFragment.addOrUpdateNeed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
