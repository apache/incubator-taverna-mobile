package org.apache.taverna.mobile.activities;

/*
* Apache Taverna Mobile
* Copyright 2015 The Apache Software Foundation

* This product includes software developed at
* The Apache Software Foundation (http://www.apache.org/).

* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*/

import org.apache.taverna.mobile.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class LoginFragment extends Fragment implements View.OnClickListener {

        private View rootView;
        private Button loginButton;
        private EditText email, password;
        private boolean logginRemain;
        private CheckBox loginCheck;

        public LoginFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_login, container, false);
            email = (EditText) rootView.findViewById(R.id.editTextUsername);
            password = (EditText) rootView.findViewById(R.id.edittextPassword);
            loginCheck = (CheckBox) rootView.findViewById(R.id.rememberCheckbox);
            loginButton = (Button) rootView.findViewById(R.id.loginbutton);
            loginButton.setOnClickListener(this);
            return rootView;
        }

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.loginbutton) {
                logginRemain = loginCheck.isChecked();
                if (logginRemain) {
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                            .putBoolean("pref_logged_in", true).apply();
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                            .putBoolean("pref_logged_in", false).apply();
                }
                if (email.getText().toString().isEmpty()) {
                    email.setError(getString(R.string.emailerr));
                } else if (password.getText().toString().isEmpty()) {
                    password.setError(getString(R.string.passworderr));
                } else {
                    // login request
                    new LoginTask(getActivity()).execute(email.getText().toString(), password
                            .getText().toString());
                }

            }
        }

        private class LoginTask extends AsyncTask<String, Void, String> {
            String cookie;
            String userurl;
            private Context context;
            private ProgressDialog pd;

            private LoginTask(Context context) {
                this.context = context;
                pd = new ProgressDialog(this.context);
                pd.setMessage("Logging in");
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected String doInBackground(String... params) {
                //http://sandbox.myexperiment.org/users

                String whoAmI = "http://www.myexperiment.org/whoami.xml";

                String response = null;
                HttpURLConnection con = null;
                try {
                    URL url = new URL(whoAmI);
                    con = (HttpURLConnection) url.openConnection();
                    String userName = params[0];
                    String password = params[1];
                    boolean redirect = false;

                    String authentication = userName + ":" + password;
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Authorization", "Basic " + Base64.encodeToString
                            (authentication.getBytes(Charset.forName("UTF-8")), Base64.DEFAULT));
                    con.setInstanceFollowRedirects(true);
                    HttpURLConnection.setFollowRedirects(true);
                    con.connect();
                    int status = con.getResponseCode();
                    response = String.valueOf(status);
                    if (status != HttpURLConnection.HTTP_OK
                            && (status == HttpURLConnection.HTTP_MOVED_PERM ||
                            status == HttpURLConnection.HTTP_MOVED_TEMP ||
                            status == HttpURLConnection.HTTP_SEE_OTHER || status == 307)) {

                        redirect = true;
                    }


                    Log.d(TAG, "Status code: " + status);
                    if (redirect) {
                        // get redirect url from "location" header field
                        String newUrl = con.getHeaderField("Location");
                        this.userurl = newUrl;
                        // get the cookie needed, for login
                        String cookies = con.getHeaderField("Set-Cookie");
                        this.cookie = cookies;
                        // open the new connection again
                        con = (HttpURLConnection) new URL(newUrl).openConnection();
                        con.setRequestProperty("Cookie", cookies);
                        Log.d(TAG, "Redirect to URL : " + newUrl);
                        con.connect();
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(con
                            .getInputStream(), "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String s = "";
                    while ((s = br.readLine()) != null) {
                        sb.append(s);
                    }
                    br.close();
                    Log.d(TAG, "data: " + sb.toString());

                    con.disconnect();

                    return response;

                } catch (MalformedURLException e) {
                    Log.e(TAG, "doInBackground: ", e);
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: ", e);
                }

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                pd.dismiss();
                if (response != null) {
                    switch (Integer.parseInt(response)) {
                        case 401:
                            Toast.makeText(getActivity(), getActivity().getString(R.string
                                    .auth_err), Toast.LENGTH_LONG).show();
                            break;
                        case 200:
                        case 307:
                            this.context.startActivity(new Intent(this.context,
                                    DashboardMainActivity.class));
                            getActivity().overridePendingTransition(R.anim.abc_slide_in_bottom, R
                                    .anim.abc_slide_out_top);
                            getActivity().finish();
                            break;
                    }
                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.servererr),
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
