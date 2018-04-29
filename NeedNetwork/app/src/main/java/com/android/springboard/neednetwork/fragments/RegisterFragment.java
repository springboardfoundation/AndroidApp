package com.android.springboard.neednetwork.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.managers.UserManager;
import com.android.springboard.neednetwork.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener{


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        Button button = (Button) view.findViewById(R.id.register_button);
        button.setOnClickListener(this);
    }

    public void onRegisterClick() {
        UserManager userManager = new UserManager(getContext());
        //userManager.registerUser(createUser());
    }

    private User createUser() {
        User user = new User();
        user.setUsername("9538987275");
        user.setMobileNumber("9538987275");

        return user;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.register_button:
                onRegisterClick()
                ;
                break;
        }
    }
}
