package com.android.springboard.neednetwork.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.activities.NeedTabsActivity;
import com.android.springboard.neednetwork.activities.RegisterActivity;
import com.android.springboard.neednetwork.utils.ActivityUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    private EditText mUserNameEt;
    private EditText mPasswordEt;

    public LoginFragment() {
        // Required empty public constructor
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
        Button loginButton = (Button) view.findViewById(R.id.login_btn);
        loginButton.setOnClickListener(this);

        TextView registerTextView = (TextView) view.findViewById(R.id.register_tv);
        registerTextView.setOnClickListener(this);

        mUserNameEt = (EditText) view.findViewById(R.id.username_et);
        mPasswordEt = (EditText) view.findViewById(R.id.password_et);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.login_btn) {
            handleLoginClick();
        } else if(id == R.id.register_tv) {
            handleRegisterTvClick();
        }
    }

    private void handleRegisterTvClick() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }

    private void handleLoginClick() {
        String username = mUserNameEt.getText().toString();
        String password = mPasswordEt.getText().toString();

        ActivityUtil.startActivity(getActivity(), NeedTabsActivity.class);
    }
}
