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
package org.apache.taverna.mobile.ui.login;


import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.local.PreferencesHelper;
import org.apache.taverna.mobile.ui.DashboardActivity;
import org.apache.taverna.mobile.utils.ConnectionInfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment implements LoginMvpView, View.OnFocusChangeListener {

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

    private DataManager dataManager;
    private LoginPresenter mLoginPresenter;


    private ProgressDialog progressDialog;

    private final String myExperimentURL = "https://www.myexperiment.org/users/new";


    public static LoginFragment newInstance() {

        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        dataManager = new DataManager(new PreferencesHelper(getContext()));
        mLoginPresenter = new LoginPresenter(dataManager);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);
        mLoginPresenter.attachView(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mEditTextEmail.addTextChangedListener(new CustomTextWatcher(mEditTextEmail));


        mEditTextPassword.addTextChangedListener(new CustomTextWatcher(mEditTextPassword));

        mEditTextEmail.setOnFocusChangeListener(this);

        mEditTextPassword.setOnFocusChangeListener(this);

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Please wait");

    }


    private void validateEmail() {

        if (mEditTextEmail.getText().toString().trim().length() == 0) {

            mTextInputEmail.setError(getString(R.string.err_login_email));
        } else {

            mTextInputEmail.setError(null);
        }


    }


    private void validatePassword() {
        if (mEditTextPassword.getText().toString().trim().length() == 0) {

            mTextInputPassword.setError(getString(R.string.err_login_password));
        } else {

            mTextInputPassword.setError(null);
        }


    }

    @OnClick(R.id.bLogin)
    public void login(View v) {
        if (ConnectionInfo.isConnectingToInternet(getContext())) {
            if (mEditTextEmail.getText().toString().trim().length() > 0 && mEditTextPassword
                    .getText().toString().trim().length() > 0) {

                mLoginPresenter.login(mEditTextEmail.getText().toString().trim(),
                        mEditTextPassword.getText().toString().trim(), mCheckBoxRemember
                                .isChecked());

            } else {

                showError("Please enter valid credential");
            }
        } else {

            showError("NO Internet Connection");
        }
    }

    @OnClick(R.id.bRegister)
    public void register(View v) {
        if (Build.VERSION.SDK_INT < 15) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(myExperimentURL));
            startActivity(intent);
        } else {
            CustomTabsIntent.Builder customTabsIntentBuilder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = customTabsIntentBuilder.build();
            customTabsIntent.launchUrl(getActivity(), Uri.parse(myExperimentURL));
        }
    }

    @Override
    public void showError(String string) {
        final Snackbar snackbar = Snackbar.make(mEditTextPassword, string, Snackbar
                .LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }

    @Override
    public void showProgressDialog(boolean flag) {
        if (flag) {
            progressDialog.show();
        } else {
            progressDialog.cancel();
        }
    }

    @Override
    public void showDashboardActivity() {
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showCredentialError() {
        showError("Please enter valid credential");

        mTextInputEmail.setError(getString(R.string.err_login_email));
        mTextInputPassword.setError(getString(R.string.err_login_password));
        requestFocus(mEditTextEmail);
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
    public void onDestroyView() {
        super.onDestroyView();

        mLoginPresenter.detachView();
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
