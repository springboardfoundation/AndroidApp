package com.android.springboard.neednetwork.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.activities.NeedActivity;
import com.android.springboard.neednetwork.application.NeedNetApplication;
import com.android.springboard.neednetwork.constants.ActivityConstants;
import com.android.springboard.neednetwork.listeners.OnListFragmentInteractionListener;
import com.android.springboard.neednetwork.models.Need;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MyNeedsListFragment extends NeedsListFragment {

    public static int MY_NEEDS_LIST_FRAGMENT_REQUEST_CODE = 101;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyNeedsListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyNeedsListFragment newInstance(int columnCount) {
        MyNeedsListFragment fragment = new MyNeedsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_needs_list, container, false);
        return view;
    }




    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

/*        if(requestCode == MY_NEEDS_LIST_FRAGMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Need need = data.getParcelableExtra(ActivityConstants.INTENT_EXTRA_NEED);
            if (mNeedsFragmentAdapter == null) {
                List<Need> needs = new ArrayList<>();
                needs.add(need);
                loadAdapter(needs);
            } else {
                mNeedsFragmentAdapter.addData(need);
            }

        }*/

    }

    public void addNeed() {
        Need need = NeedNetApplication.getNeed();

        if(need == null) {
            return;
        }

        if (mNeedsFragmentAdapter == null) {
            List<Need> needs = new ArrayList<>();
            needs.add(need);
            loadAdapter(needs);
        } else {
            mNeedsFragmentAdapter.addData(need);
        }
    }

    @Override
    protected void handleOnClick(View view) {
        Need need = (Need) view.getTag();
        Intent intent = new Intent();
        intent.setClass(getContext(), NeedActivity.class);
        intent.putExtra(ActivityConstants.INTENT_EXTRA_NEED, need);
        NeedNetApplication.setNeed(null);
        startActivityForResult(intent, MY_NEEDS_LIST_FRAGMENT_REQUEST_CODE);
    }
}
