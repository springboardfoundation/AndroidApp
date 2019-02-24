package com.android.springboard.neednetwork.fragments;


import android.app.Fragment;
import android.view.View;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.models.Need;

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
    protected void initMyViews(View view) {
        mTargetAmountEditText = view.findViewById(R.id.target_amt_et);
        mTargetAmountEditText.setVisibility(View.VISIBLE);
        return;
    }

    @Override
    protected void populateMyNeed(Need need) {
        mTargetAmountEditText.setText(need.getTargetAmount());
    }
}
