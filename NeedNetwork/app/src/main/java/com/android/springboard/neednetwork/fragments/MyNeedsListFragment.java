package com.android.springboard.neednetwork.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.application.NeedNetApplication;
import com.android.springboard.neednetwork.listeners.OnListFragmentInteractionListener;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MyNeedsListFragment extends NeedsListFragment {

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


    public void loadAdapter() {
        loadAdapter(NeedNetApplication.getMyNeedList());
    }
}
