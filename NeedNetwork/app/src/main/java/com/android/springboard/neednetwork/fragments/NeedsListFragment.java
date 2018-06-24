package com.android.springboard.neednetwork.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.adapters.NeedsFragmentAdapter;
import com.android.springboard.neednetwork.callbacks.ToolbarActionModeCallback;
import com.android.springboard.neednetwork.listeners.OnActivityInteractionListener;
import com.android.springboard.neednetwork.listeners.OnListFragmentInteractionListener;
import com.android.springboard.neednetwork.listeners.RecyclerClickListener;
import com.android.springboard.neednetwork.listeners.RecyclerTouchListener;
import com.android.springboard.neednetwork.models.Need;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class NeedsListFragment extends Fragment implements RecyclerClickListener, OnListFragmentInteractionListener {

    // TODO: Customize parameter argument names
    protected static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters

    protected RecyclerView mRecyclerView;
    protected int mColumnCount = 1;
    protected NeedsFragmentAdapter mNeedsFragmentAdapter;
    protected ActionMode mActionMode;
    protected List<Need> mNeedList;
    protected OnActivityInteractionListener mListener;

    protected abstract void handleOnClick(View view);


    public NeedsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    public boolean isAdapterInitialized() {
        return mNeedsFragmentAdapter != null;
    }

    public void loadAdapter(List<Need> needList) {
        if(needList == null || needList.isEmpty()) {
            return;
        }

        if(mNeedsFragmentAdapter == null) {
            View view = getView();
            mNeedList = needList;
            // Set the adapter
            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                mRecyclerView = (RecyclerView) view;
                if (mColumnCount <= 1) {
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                } else {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                }
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mNeedsFragmentAdapter = new NeedsFragmentAdapter(getActivity(), mNeedList);
                mRecyclerView.setAdapter(mNeedsFragmentAdapter);
                mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerView, this));
                if(this instanceof MyNeedsListFragment) {
                    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            mListener.onRecyclerViewScroll(dx, dy);
                        }
                    });
                }
            }
        } else {
            mNeedsFragmentAdapter.updateData(needList);
        }

    }


    @Override
    public void onClick(View view, int position) {
        if (mActionMode == null || mNeedsFragmentAdapter.getSelectedCount() <= 0) {
            handleOnClick(view);
        } else {
            onListItemSelect(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActivityInteractionListener) {
            mListener = (OnActivityInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onLongClick(View view, int position) {
        //Select item on long click
        onListItemSelect(position);
    }

    //List item select method
    private void onListItemSelect(int position) {
        if(this instanceof CurrentNeedsListFragment) {
            return;
        }

        mNeedsFragmentAdapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = mNeedsFragmentAdapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ToolbarActionModeCallback(getActivity(),mNeedsFragmentAdapter, mNeedList, this));
        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null) {
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(mNeedsFragmentAdapter
                    .getSelectedCount()) + " selected");

            boolean hasCheckedMoreThanOne = mNeedsFragmentAdapter.getSelectedCount() > 1;
            Menu menu = mActionMode.getMenu();
            MenuItem deleteMenuItem = menu.findItem(R.id.action_delete);
            deleteMenuItem.setVisible(!hasCheckedMoreThanOne);
            MenuItem shareMenuItem = menu.findItem(R.id.action_share);
            shareMenuItem.setVisible(!hasCheckedMoreThanOne);
        }
    }
    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    //Delete selected rows
    public void deleteRows() {
        SparseBooleanArray selected = mNeedsFragmentAdapter
                .getSelectedIds();//Get selected ids

        //Loop all selected ids
/*        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                //If current id is selected remove the item via key
                mNeedList.remove(selected.keyAt(i));
                mNeedsFragmentAdapter.notifyDataSetChanged();//notify adapter

            }
        }*/
        Toast.makeText(getActivity(), selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
        mActionMode.finish();//Finish action mode after use

    }

    @Override
    public void clearActionMode() {
        setNullToActionMode();
    }

    @Override
    public void deleteSelectedItems() {
        deleteRows();
    }
}
