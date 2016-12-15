/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.taverna.mobile.ui.playerlogin;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.local.PreferencesHelper;
import org.apache.taverna.mobile.utils.ConnectionInfo;
import org.apache.taverna.mobile.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;


public class PlayerLoginFragment extends Fragment implements PlayerLoginMvpView, View
        .OnFocusChangeListener {

    @BindView(R.id.etEmail)
    EditText mEditTextEmail;

    @BindView(R.id.etPassword)
    EditText mEditTextPassword;

    @BindView(R.id.input_layout_email)
    TextInputLayout mTextInputEmail;

    @BindView(R.id.input_layout_password)
    TextInputLayout mTextInputPassword;

    @BindView(R.id.cbRemember)
    CheckBox mCheckBoxRemember;
    OnSuccessful mCallback;
    private DataManager dataManager;
    private PlayerLoginPresenter mPlayerLoginPresenter;
    private Subscription mSubscriptions;

    public static PlayerLoginFragment newInstance() {

        Bundle args = new Bundle();

        PlayerLoginFragment fragment = new PlayerLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataManager = new DataManager(new PreferencesHelper(getContext()));
        mPlayerLoginPresenter = new PlayerLoginPresenter(dataManager);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_player_login_layout, container, false);
        ButterKnife.bind(this, rootView);
        mPlayerLoginPresenter.attachView(this);
        String email = dataManager.getPreferencesHelper().getPlayerUserEmail();
        String pw = dataManager.getPreferencesHelper().getPlayerUserPassword();
        mEditTextEmail.setText(email);
        mEditTextPassword.setText(pw);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEditTextEmail.addTextChangedListener(new CustomTextWatcher(mEditTextEmail));
        mEditTextEmail.setOnFocusChangeListener(this);

        mEditTextPassword.addTextChangedListener(new CustomTextWatcher(mEditTextPassword));
        mEditTextPassword.setOnFocusChangeListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPlayerLoginPresenter.detachView();
    }


    @OnClick(R.id.bLogin)
    public void login(View v) {
        if (ConnectionInfo.isConnectingToInternet(getContext())) {
            String workflowURL = getActivity().getIntent().getStringExtra(Constants.WORKFLOW_URL);
            mPlayerLoginPresenter.playerLogin(workflowURL, mEditTextEmail.getText().toString().trim(),
                        mEditTextPassword.getText().toString().trim(), mCheckBoxRemember
                                .isChecked());

//            if (!mEditTextEmail.getText().toString().trim().isEmpty() && !mEditTextPassword
//                    .getText().toString().trim().isEmpty()) {
//
//                mPlayerLoginPresenter.playerLogin(mEditTextEmail.getText().toString().trim(),
//                        mEditTextPassword.getText().toString().trim(), mCheckBoxRemember
//                                .isChecked());
//
//            } else {
//
//                showError(R.string.error_vaild_credential);
//            }
        } else {

            showError(R.string.no_internet_connection);
        }
    }

    @Override
    public void showError(int stringID) {
        final Snackbar snackbar = Snackbar.make(mEditTextPassword, getString(stringID), Snackbar
                .LENGTH_LONG);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }

    @Override
    public void showCredentialError() {
        mTextInputEmail.setError(getString(R.string.err_login_email));
        mTextInputPassword.setError(getString(R.string.err_login_password));
        requestFocus(mEditTextPassword);
    }

    @Override
    public void runLocation(String runID) {
        mCallback.onRunStart(runID);
    }

    private void validateEmail() {

        if (mEditTextEmail.getText().toString().trim().isEmpty()) {
            mTextInputEmail.setError(getString(R.string.err_login_email));
        } else {
            mTextInputEmail.setError(null);
        }


    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void validatePassword() {
        if (mEditTextPassword.getText().toString().trim().isEmpty()) {
            mTextInputPassword.setError(getString(R.string.err_login_password));
        } else {
            mTextInputPassword.setError(null);
        }


    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.etEmail:
                if (!v.hasFocus()) {
                    validateEmail();
                }
                break;
            case R.id.etPassword:
                if (!v.hasFocus()) {
                    validatePassword();
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnSuccessful) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnSuccessful");
        }
    }


    public interface OnSuccessful {
        void onRunStart(String runID);
    }

    private class CustomTextWatcher implements TextWatcher {

        private View view;

        private CustomTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.etEmail:
                    validateEmail();
                    break;
                case R.id.etPassword:
                    validatePassword();
                    break;
            }
        }
    }
}
