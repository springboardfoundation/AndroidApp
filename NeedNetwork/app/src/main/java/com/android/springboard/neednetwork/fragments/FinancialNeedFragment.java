package com.android.springboard.neednetwork.fragments;


import android.app.Fragment;
import android.view.View;

import com.android.springboard.neednetwork.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinancialNeedFragment extends NeedFragment {

    public static final String FRAGMENT_TAG = FinancialNeedFragment.class.getSimpleName();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_need;
    }

    @Override
    protected int initMyViews(View view) {
        return 0;
    }
}
