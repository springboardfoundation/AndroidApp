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
import com.android.springboard.neednetwork.constants.ActivityConstants;
import com.android.springboard.neednetwork.managers.NeedManager;
import com.android.springboard.neednetwork.models.User;
import com.android.springboard.neednetwork.utils.ActivityUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhoneVerificationFragment extends Fragment implements Validator.ValidationListener{

    @NotEmpty
    private EditText mSecurityCodeEt;
    private Validator mValidator;
    private String mMobileNumber;
    private NeedManager mNeedManager;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phone_verification, container, false);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        mSecurityCodeEt = (EditText) view.findViewById(R.id.security_code_et);
    }

    public void handleOk() {
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        //ActivityUtil.startActivity(getActivity(), NeedTabsActivity.class);
        User user = new User();
        user.setMobileNumber("12345");
        mNeedManager.login(user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("shoeb", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("shoeb", error.toString());
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        ActivityUtil.handleEditTextValidationError(getActivity(), errors);
    }
}
