package com.android.springboard.neednetwork.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.activities.NeedTabsActivity;
import com.android.springboard.neednetwork.constants.ActivityConstants;
import com.android.springboard.neednetwork.managers.NeedManager;
import com.android.springboard.neednetwork.managers.UserManager;
import com.android.springboard.neednetwork.models.User;
import com.android.springboard.neednetwork.utils.ActivityUtil;
import com.android.springboard.neednetwork.utils.SharedPrefsUtils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhoneVerificationFragment extends Fragment implements Validator.ValidationListener {

    @NotEmpty
    private EditText mSecurityCodeEt;
    private Validator mValidator;
    private String mMobileNumber;
    private NeedManager mNeedManager;
    private UserManager mUserManager;

    public PhoneVerificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();
        mMobileNumber = activity.getIntent().getStringExtra(ActivityConstants.INTENT_EXTRA_MOBILE_NUMBER);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mNeedManager = new NeedManager(activity);
        mUserManager = new UserManager(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phone_verification, container, false);
        initViews(view);

        return view;
    }

    private void registerUser(String mobileNumber) {
        User user = new User();
        user.setMobileNumber(mobileNumber);
        user.setUsername(mobileNumber);
        mUserManager.registerUser(user, mRegisterResponseListener, mRegisterErrorListener);
    }

    private Response.Listener mRegisterResponseListener = new Response.Listener() {
        @Override
        public void onResponse(Object response) {
            Log.i("shoeb", "" + response);
            SharedPrefsUtils.setStringPreference(getActivity(), ActivityConstants.PREF_MOBILE_NUMBER, mMobileNumber);
            ActivityUtil.startActivity(getActivity(), NeedTabsActivity.class);
        }
    };

    private Response.ErrorListener mRegisterErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("shoeb", "" + error);
        }
    };

    private void initViews(View view) {
        mSecurityCodeEt = (EditText) view.findViewById(R.id.security_code_et);
    }

    public void handleOk() {
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        registerUser(mMobileNumber);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        ActivityUtil.handleEditTextValidationError(getActivity(), errors);
    }
}
