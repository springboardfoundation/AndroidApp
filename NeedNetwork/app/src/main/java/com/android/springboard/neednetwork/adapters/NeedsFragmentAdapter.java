package com.android.springboard.neednetwork.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.listeners.OnListFragmentInteractionListener;
import com.android.springboard.neednetwork.models.Need;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a  and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class NeedsFragmentAdapter extends RecyclerView.Adapter<NeedsFragmentAdapter.ViewHolder> {

    private Context mContext;
    private List<Need> mUserList;
    private SparseBooleanArray mSelectedItemsIds;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public NeedsFragmentAdapter(Context context, List<Need> userList) {
        mContext = context;
        mUserList = userList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_needs_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mUserList.get(position);
        holder.mView.setTag(holder.mItem);

        if(holder.mItem.getTitle().length() > 15) {
            holder.mIdView.setText(holder.mItem.getTitle().substring(0, 15) + "...");
        } else {
            holder.mIdView.setText(holder.mItem.getTitle());
        }

        long targetDate = Long.valueOf(holder.mItem.getTargetDate());
        String formattedDate = mDateFormat.format(targetDate);
        holder.mTargetView.setText(formattedDate);
        holder.mContentView.setText(holder.mItem.getDescription());

/*        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });*/

        holder.itemView.setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
                        : Color.TRANSPARENT);
        ColorDrawable colorDrawable = new ColorDrawable(mSelectedItemsIds.get(position) ? 0x9934B5E4
                : Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.itemView.setForeground(colorDrawable);
        }
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mTargetView;
        public final TextView mContentView;
        public Need mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mTargetView = (TextView) view.findViewById(R.id.target);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public void updateData(List<Need> needList) {
        mUserList.clear();
        mUserList.addAll(needList);
        mSelectedItemsIds.clear();

        notifyDataSetChanged();
    }

    public void addData(Need need) {
        mUserList.add(need);
        mSelectedItemsIds.clear();

        notifyDataSetChanged();
    }

    /***
     * Methods required for do selections, remove selections, etc.
     */

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}
