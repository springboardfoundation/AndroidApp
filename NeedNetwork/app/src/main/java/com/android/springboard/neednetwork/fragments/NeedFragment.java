package com.android.springboard.neednetwork.fragments;


import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.activities.ContactPickerActivity;
import com.android.springboard.neednetwork.managers.NeedManager;
import com.android.springboard.neednetwork.models.Need;
import com.android.springboard.neednetwork.utils.ActivityUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.picture.ContactPictureType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.android.springboard.neednetwork.activities.ContactPickerActivity.RESULT_CONTACT_DATA;

/**
 * A simple {@link Fragment} subclass.
 */
public class NeedFragment extends Fragment implements Validator.ValidationListener, View.OnClickListener, View.OnFocusChangeListener {

    private static final int REQUEST_CONTACT = 0;

    @NotEmpty
    private EditText mTitleEditText;
    @NotEmpty
    private EditText mDescEditText;
    @NotEmpty
    private EditText mGoalEditText;
    @NotEmpty
    private EditText mTargetDateEditText;
    private EditText mLocationEditText;

    private Validator mValidator;
    private DatePickerDialog mDatePickerDialog;
    private NeedManager mNeedManager;
    private Need mNeed = new Need();


    public NeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        Calendar calendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(getContext(), mDateSetListener,
                calendar.get(Calendar.YEAR) , calendar.get(Calendar.MONTH) , calendar.get(Calendar.DAY_OF_MONTH));
        mNeedManager = new NeedManager(getContext());
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
        mTargetDateEditText = (EditText) view.findViewById(R.id.target_date_et);
        mTargetDateEditText.setOnClickListener(this);
        mTargetDateEditText.setOnFocusChangeListener(this);
        mTargetDateEditText.setKeyListener(null);
        mLocationEditText = (EditText) view.findViewById(R.id.location_et);

        FloatingActionButton actionConnectionsButton = (FloatingActionButton) view.findViewById(R.id.add_connections_btn);
        actionConnectionsButton.setOnClickListener(this);
    }

    public void addNeed() {
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        mNeed.setTitle(mTitleEditText.getText().toString());
        mNeed.setDescription(mDescEditText.getText().toString());
        mNeed.setGoal(mGoalEditText.getText().toString());
        DatePicker datePicker = mDatePickerDialog.getDatePicker();
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date() {

            @Override
            public String toString() {
                return dateFormat.format(calendar.getTime());
            }
        };
        mNeed.setTargetDate(date);
        mNeed.setLocation(mLocationEditText.getText().toString());
        mNeedManager.createNeed(mNeed, mNeedResponseListener, mNeedErrorListener);
    }

    private Response.Listener mNeedResponseListener = new Response.Listener() {
        @Override
        public void onResponse(Object response) {
            Log.i("shoeb", "" + response);
        }
    };

    private Response.ErrorListener mNeedErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("shoeb", "" + error);
        }
    };

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
        } else if(id == R.id.target_date_et && !mDatePickerDialog.isShowing()) {
            mDatePickerDialog.show();
        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year,
                              int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
            String date = format.format(calendar.getTime());
            mTargetDateEditText.setText(date);
        }
    };

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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId() == R.id.target_date_et && hasFocus && !mDatePickerDialog.isShowing()) {
            mDatePickerDialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Contact> contacts = (List<Contact>) data.getSerializableExtra(RESULT_CONTACT_DATA);

        Set<String> users = new HashSet<>();
        for (Contact contact : contacts) {
            for (String number : contact.getPhone()) {
                users.add(number);
            }
        }

        Log.i("shoeb", Arrays.toString(users.toArray()));
        mNeed.setUsers(new ArrayList<>(users));
    }
}
