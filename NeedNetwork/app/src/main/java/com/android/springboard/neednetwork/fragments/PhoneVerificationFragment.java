package com.android.springboard.neednetwork.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.constants.ActivityConstants;
import com.android.springboard.neednetwork.managers.NeedManager;
import com.android.springboard.neednetwork.managers.UserManager;
import com.android.springboard.neednetwork.models.User;
import com.android.springboard.neednetwork.utils.ActivityUtil;
import com.android.springboard.neednetwork.utils.SharedPrefsUtils;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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

    private void registerUser(final String mobileNumber) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String deviceId = task.getResult().getToken();
                        User user = new User();
                        user.setMobileNumber(mobileNumber);
                        user.setUsername(mobileNumber);
                        user.setDeviceID(deviceId);
                        mUserManager.registerUser(user, mRegisterResponseListener, mRegisterErrorListener);
                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        //Log.d(TAG, msg);
                        //Toast.makeText(getActivity(), token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Response.Listener mRegisterResponseListener = new Response.Listener() {
        @Override
        public void onResponse(Object response) {
            Log.i("shoeb", "" + response);
            onRegistrationSuccess();
        }
    };

    private Response.ErrorListener mRegisterErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("shoeb", "" + error);
            if(error != null && error instanceof ParseError) {
                ParseError parseError = (ParseError) error;
                if(parseError.networkResponse.statusCode == 208) {
                    onRegistrationSuccess();
                }
            } else {
                Toast.makeText(getActivity(), R.string.text_network_error, Toast.LENGTH_LONG).show();
            }
        }
    };

    private void onRegistrationSuccess() {
        SharedPrefsUtils.setStringPreference(getActivity(), ActivityConstants.PREF_MOBILE_NUMBER, mMobileNumber);
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

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
