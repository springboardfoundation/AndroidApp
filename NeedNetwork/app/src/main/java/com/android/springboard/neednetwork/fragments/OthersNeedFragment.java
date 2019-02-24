package com.android.springboard.neednetwork.fragments;


import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.activities.ContactPickerActivity;
import com.android.springboard.neednetwork.constants.ActivityConstants;
import com.android.springboard.neednetwork.managers.NeedManager;
import com.android.springboard.neednetwork.models.Need;
import com.android.springboard.neednetwork.utils.Address;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.picture.ContactPictureType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.android.springboard.neednetwork.activities.ContactPickerActivity.RESULT_CONTACT_DATA;

/**
 * A simple {@link Fragment} subclass.
 */
public class OthersNeedFragment extends Fragment implements View.OnClickListener{

    private static final int REQUEST_CONTACT = 0;
    private TextView mTitleTv;
    private TextView mDescTv;
    private TextView mTargetDateTv;
    private TextView mLocationTv;
    private FloatingActionButton mFloatingActionButton;

    private NeedManager mNeedManager;
    private Need mNeed;
    private List<String> mUsersList = new ArrayList<>();


    public OthersNeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNeedManager = new NeedManager(getActivity());
        Intent intent = getActivity().getIntent();
        mNeed = intent.getParcelableExtra(ActivityConstants.INTENT_EXTRA_NEED);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_others_need, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mTitleTv = (TextView) view.findViewById(R.id.title_et);
        mDescTv = (TextView) view.findViewById(R.id.description_et);
        mTargetDateTv = (TextView) view.findViewById(R.id.target_date_et);
        mLocationTv = (TextView) view.findViewById(R.id.location_et);

        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_connections_btn);
        mFloatingActionButton.setOnClickListener(this);

        populateNeed(mNeed);
    }

    private void populateNeed(Need need) {
        if( need == null) {
            return;
        }

        mTitleTv.setText(need.getTitle());
        mDescTv.setText(need.getDescription());
        mLocationTv.setText(need.getLocation());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(need.getTargetDate()));
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mTargetDateTv.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.add_connections_btn) {
            //handleAddConnectionsClick();
            new TedPermission(getActivity())
                    .setPermissionListener(mPermissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.READ_CONTACTS)
                    .check();
        }
    }

    private boolean isAnythingChanged() {
        if(mNeed == null) {
            return true;
        }

        if(!mNeed.getUsers().containsAll(mUsersList)) {
            return true;
        }

        return false;
    }

    PermissionListener mPermissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            handleAddConnectionsClick();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private void handleAddConnectionsClick() {
        Intent intent = new Intent(getActivity(), ContactPickerActivity.class)
                .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE,
                        ContactPictureType.ROUND.name())

                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION,
                        ContactDescription.ADDRESS.name())
                .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
                .putExtra(ContactPickerActivity.EXTRA_SELECT_CONTACTS_LIMIT, 0)
                .putExtra(ContactPickerActivity.EXTRA_ONLY_CONTACTS_WITH_PHONE, false)

                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE,
                        ContactsContract.CommonDataKinds.Email.TYPE_WORK)



                .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER,
                        ContactSortOrder.AUTOMATIC.name());

/*        if(!mPreSelectedContactIds.isEmpty()) {
            intent.putExtra(ContactPickerActivity.EXTRA_PRESELECTED_CONTACTS,
                    mPreSelectedContactIds);
        }*/

        startActivityForResult(intent, REQUEST_CONTACT);
    }

    private Response.Listener mNeedResponseListener = new Response.Listener() {
        @Override
        public void onResponse(Object response) {
            Log.i("shoeb", "" + response);
            getActivity().finish();
        }
    };

    private Response.ErrorListener mNeedErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("shoeb", "" + error);
            Toast.makeText(getActivity(), R.string.text_network_error, Toast.LENGTH_LONG).show();
        }
    };

    public void addUsers() {
        if(mNeedManager == null || !isAnythingChanged()) {
            getActivity().finish();
            return;
        }

        mNeedManager.updateUsers(mNeed, mNeedResponseListener, mNeedErrorListener);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null || data.getSerializableExtra(RESULT_CONTACT_DATA) == null) {
            return;
        }

        List<Contact> contacts = (List<Contact>) data.getSerializableExtra(RESULT_CONTACT_DATA);

        if(contacts.size() > 0) {
            Set<String> users = new HashSet<>();
            for (Contact contact : contacts) {
                Address address = Address.fromExternal(getActivity(), contact.getPhoneNumber());
                users.add(address.toString());
            }

            if(mNeed == null) {
                mNeed = new Need();
            }

            Log.i("shoeb", Arrays.toString(users.toArray()));
            mUsersList.clear();
            mUsersList.addAll(users);
            mNeed.setUsers(mUsersList);
        }


    }

}
