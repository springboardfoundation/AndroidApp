package com.android.springboard.neednetwork.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.activities.PhoneVerificationActivity;
import com.android.springboard.neednetwork.application.NeedNetApplication;
import com.android.springboard.neednetwork.constants.ActivityConstants;
import com.android.springboard.neednetwork.utils.ActivityUtil;
import com.hbb20.CountryCodePicker;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, Validator.ValidationListener {

    private CountryCodePicker mCountryCodePicker;
    @NotEmpty
    private EditText mMobileNumberEt;

    private Validator mValidator;

    public LoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mCountryCodePicker = (CountryCodePicker) view.findViewById(R.id.country_name_picker);
        setDefaultCountryCode();

        mMobileNumberEt = (EditText) view.findViewById(R.id.mobile_number_et);
        mCountryCodePicker.registerCarrierNumberEditText(mMobileNumberEt);
    }

    private void setDefaultCountryCode() {
        String country = null;

        try {
            TelephonyManager telephonyManager = (TelephonyManager) NeedNetApplication.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                country = telephonyManager.getSimCountryIso().toUpperCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (country != null) {
            mCountryCodePicker.setDefaultCountryUsingNameCode(country);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();


    }

    public void handleOk() {
        if(!mCountryCodePicker.isValidFullNumber()) {
            mMobileNumberEt.setError("Invalid number");
            return;
        }

        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        ActivityUtil.startActivity(getActivity(), PhoneVerificationActivity.class, ActivityConstants.INTENT_EXTRA_MOBILE_NUMBER,
                mCountryCodePicker.getFullNumberWithPlus());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        ActivityUtil.handleEditTextValidationError(getActivity(), errors);
    }
}
