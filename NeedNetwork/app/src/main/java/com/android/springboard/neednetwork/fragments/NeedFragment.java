package com.android.springboard.neednetwork.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.springboard.neednetwork.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NeedFragment extends Fragment implements Validator.ValidationListener {

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
    }

    public void addNeed() {
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
