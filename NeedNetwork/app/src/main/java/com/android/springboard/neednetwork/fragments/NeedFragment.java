package com.android.springboard.neednetwork.fragments;


import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.utils.ActivityUtil;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.picture.ContactPictureType;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NeedFragment extends Fragment implements Validator.ValidationListener, View.OnClickListener {

    private static final int REQUEST_CONTACT = 0;

    @NotEmpty
    private EditText mTitleEditText;
    @NotEmpty
    private EditText mDescEditText;
    @NotEmpty
    private EditText mGoalEditText;
    @NotEmpty
    private EditText mDurationEditText;
    private EditText mLocationEditText;

    private Validator mValidator;


    public NeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_need, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mTitleEditText = (EditText) view.findViewById(R.id.title_et);
        mDescEditText = (EditText) view.findViewById(R.id.description_et);
        mGoalEditText = (EditText) view.findViewById(R.id.goal_et);
        mDurationEditText = (EditText) view.findViewById(R.id.duration_et);
        mLocationEditText = (EditText) view.findViewById(R.id.location_et);

        FloatingActionButton actionConnectionsButton = (FloatingActionButton) view.findViewById(R.id.add_connections_btn);
        actionConnectionsButton.setOnClickListener(this);
    }

    public void addNeed() {
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        ActivityUtil.handleEditTextValidationError(getActivity(), errors);
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
        startActivityForResult(intent, REQUEST_CONTACT);
    }
}
