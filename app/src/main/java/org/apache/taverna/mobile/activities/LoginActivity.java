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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
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

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.tavernamobile.TavernaPlayerAPI;
import org.apache.taverna.mobile.tavernamobile.User;
import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.apache.taverna.mobile.utils.HttpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
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
    public static class LoginFragment extends Fragment implements View.OnClickListener{

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
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("pref_logged_in",true).apply();
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("pref_logged_in",false).apply();
                }
                if (email.getText().toString().isEmpty()) {
                    email.setError(getString(R.string.emailerr));
                } else if (password.getText().toString().isEmpty()) {
                    password.setError(getString(R.string.passworderr));
                } else {
                    // login request
                    new LoginTask(getActivity()).execute(email.getText().toString(), password.getText().toString());
                }

            }
        }

        private class LoginTask extends AsyncTask<String, Void, String>{
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

                    String authentication = userName + ":" + password;
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(authentication.getBytes(), Base64.DEFAULT));
                    con.setInstanceFollowRedirects(true);

                    con.connect();
                    response = String.valueOf(con.getResponseCode());
                    //response values are:
                    //401 for an unauthorized or invalid credential and 200 for a valid and authorized user
                    System.out.println("url = "+con.getURL());
                    System.out.println("content-type = "+con.getContentType());
                    System.out.println("content encoding "+con.getContentEncoding());
                    System.out.println("date"+con.getDate());
                    System.out.println("" + response);
                    System.out.println(""+con.getResponseMessage());
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String s = "";
                    while((s = br.readLine())!= null ){
                        sb.append(s);
                    }
                    System.out.println("data: "+sb.toString());

                    con.disconnect();
                    return response;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                pd.dismiss();
                if(response != null) {
                    switch(Integer.parseInt(response)){
                        case 401:
                            Toast.makeText(getActivity(), getActivity().getString(R.string.auth_err), Toast.LENGTH_LONG).show();
                            break;
                        case 200:
                            this.context.startActivity(new Intent(this.context, DashboardMainActivity.class));
                            getActivity().overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
                            getActivity().finish();
                            break;
                    }
                }else{
                    Toast.makeText(getActivity(), getActivity().getString(R.string.servererr), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
