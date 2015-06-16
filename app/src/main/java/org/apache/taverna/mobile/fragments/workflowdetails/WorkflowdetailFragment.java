package org.apache.taverna.mobile.fragments.workflowdetails;
/**
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
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.activities.DashboardMainActivity;
import org.apache.taverna.mobile.tavernamobile.TavernaPlayerAPI;
import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.apache.taverna.mobile.utils.DetailsLoader;
import org.apache.taverna.mobile.utils.WorkflowDownloadManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.prefs.PreferenceChangeEvent;

/**
 * Created by Larry Akah on 6/9/15.
 */
public class WorkflowdetailFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Workflow>{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private DownloadManager downloadManager;
    View rootView;
    private ProgressDialog progressDialog;
    public AlertDialog runDialog;
    public AlertDialog.Builder alertDialogBuilder;
    private String download_url;
    public static long WORKFLO_ID;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WorkflowdetailFragment newInstance(int sectionNumber) {
        WorkflowdetailFragment fragment = new WorkflowdetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public WorkflowdetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        long workflowid = getActivity().getIntent().getLongExtra("workflowid", 0);
        rootView = inflater.inflate(R.layout.fragment_workflow_detail, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getActivity().getResources().getString(R.string.loading));
        progressDialog.setCancelable(true);
        WORKFLO_ID = workflowid;

        Button createRun = (Button) rootView.findViewById(R.id.run_wk);
        createRun.setOnClickListener(this);
        Button download = (Button) rootView.findViewById(R.id.download_wk);
        download.setOnClickListener(this);
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        return rootView;
    }

    /**
     * Called when a fragment is first attached to its activity.
     * {@link #onCreate(android.os.Bundle)} will be called after this.
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.run_wk:
                //TODO implement functionality to issue a run request to the Taverna PLAYER to run the current workflow
                new WorkflowRunTask(getActivity()).execute(""+WORKFLO_ID);
                break;
            case R.id.download_wk:
                // start the android Download manager to start downloading a remote workflow file
                WorkflowDownloadManager dmgr = new WorkflowDownloadManager(getActivity(), downloadManager);
                try {
                    dmgr.downloadWorkflow(new File(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(
                                    DashboardMainActivity.APP_DIRECTORY_NAME, "/")),
                            download_url);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.mark_wk:
                //TODO mark a workflow as important and launch task to store the entry into the local database
                break;
        }
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link android.app.Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        getActivity().getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Workflow> onCreateLoader(int i, Bundle bundle) {
       // progressDialog = ProgressDialog.show(getActivity(),"",getActivity().getResources().getString(R.string.loading));
        progressDialog.show();
        return new DetailsLoader(getActivity(),
                DetailsLoader.LOAD_TYPE.TYPE_WORKFLOW_DETAIL,
                getActivity().getIntent().getLongExtra("workflowid", 0));
    }

    @Override
    public void onLoadFinished(Loader<Workflow> workflowLoader, Workflow workflow) {
        TextView author = (TextView) rootView.findViewById(R.id.wkf_author);
            author.append("->" + workflow.getWorkflow_author());
        TextView title = (TextView) rootView.findViewById(R.id.wtitle);
            title.setText(workflow.getWorkflow_title());
        TextView desc = (TextView) rootView.findViewById(R.id.wdescription);
            desc.setText(workflow.getWorkflow_description());
        TextView createdat = (TextView) rootView.findViewById(R.id.wcreatedat);
            createdat.append(workflow.getWorkflow_datecreated());
        TextView updated = (TextView) rootView.findViewById(R.id.wupdatedat);
            updated.append(workflow.getWorkflow_datemodified());
    //    ImageView preview = (ImageView) rootView.findViewById(R.id.wkf_image);
          //  preview.setImageURI(Uri.parse(workflow.getWorkflow_remote_url()));
        download_url =workflow.getWorkflow_remote_url();
      //  progressDialog.cancel();
        progressDialog.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<Workflow> workflowLoader) {
        workflowLoader.reset();
    }

    //create and return a new TextView
    public TextView createTextView(Context mcontetx, String placeholder){
        TextView tv = new TextView(mcontetx);
        tv.setText(placeholder);
        tv.setMinLines(2);

        return tv;
    }

    //create and return a new EdiText view
    public EditText createEditText(Context ctx, int i){
        EditText edt;
        edt = new EditText(ctx);
        edt.setHint("Enter Value");
        edt.setMinLines(2);
        edt.setId(i);
        return edt;
    }
//fetch and compute the framework on which the run inputs are to be built and entered
    private class WorkflowRunTask extends AsyncTask<String, Void, String>{

        private Context context;

        private WorkflowRunTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(this.context.getResources().getString(R.string.fetchrun));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuffer sb = new StringBuffer();
            try {

                URL workflowurl = new URL(TavernaPlayerAPI.PLAYER_RUN_FRAMEWORK_URL+params[0]);
                HttpURLConnection connection = (HttpURLConnection) workflowurl.openConnection();
                String userpass = "icep603@gmail.com" + ":" + "creationfox";
                String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);

                connection.setRequestProperty("Authorization", basicAuth);
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("GET");
                // connection.setDoInput(true);
                //  connection.setDoOutput(true);
                connection.connect(); //send request
                Log.i("RESPONSE Code", "" + connection.getResponseCode());
                Log.i("RESPONSE Messsage", "" + connection.getResponseMessage());
                Log.i("Authorization ", "" + connection.getRequestProperty("Authorization"));

                InputStream dis = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(dis));

                String jsonData = "";
                while ((jsonData = br.readLine()) != null) {
                    sb.append(jsonData);
                }
                dis.close();
                br.close();
                return sb.toString();

            }catch (IOException ex){
                ex.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            //show the skeleton to the user in a dialog box
            final Context ctx = this.context;
            final LinearLayout ll = new LinearLayout(ctx);
            ll.setOrientation(LinearLayout.VERTICAL);

            try {
                final JSONObject json = new JSONObject(result); //main server response json
                JSONObject mjson = json.getJSONObject("run"); //main framework response json
                String name = mjson.getString("name"); //a name that can be configured or edited for the new run to be created
                ll.addView(createTextView(ctx, name));
                final JSONArray attr_array = mjson.getJSONArray("inputs_attributes");
                for(int i=0; i<attr_array.length(); i++){
                    JSONObject obj = attr_array.getJSONObject(i);
                    ll.addView(createTextView(ctx, obj.getString("name")));
                    ll.addView(createEditText(ctx, i));
                }

                alertDialogBuilder = new AlertDialog.Builder(ctx);
                alertDialogBuilder.setView(ll);
 //               alertDialogBuilder.setMessage(result);
                alertDialogBuilder.setIcon(ctx.getResources().getDrawable(R.mipmap.ic_launcher));
                alertDialogBuilder.setTitle("New Run");
                alertDialogBuilder.setPositiveButton("Execute", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int n = attr_array.length();
                        for(int j=0; j<n; j++){
                            try {
                                EditText inputText = (EditText) ll.findViewById(j);
                                String value = inputText.getText().toString();//get input entry entered by the user
                                JSONObject jojb = attr_array.getJSONObject(j); //get the input attributes provided by the skeleton
                                jojb.put("value", value); //replace value field in object with the entry provided by the user
                                attr_array.put(j, jojb); //replace the input entry with the new name/input json object

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        try {
                            json.put("inputs_attributes", attr_array);
                            Log.i("RUN FRAMEWORK", json.toString(2));
                            new RunTask(ctx).execute(json.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                runDialog = alertDialogBuilder.create();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();

            runDialog.show();
        }
    }
    //Send request for the execution of a run on the server through the player
    private class RunTask extends AsyncTask<String, Void, String>{

        private Context context;

        private RunTask(Context ctx) {
            this.context = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuffer sb = new StringBuffer();
            try {

                URL workflowurl = new URL(TavernaPlayerAPI.PLAYER_RUN_URL);
                HttpURLConnection connection = (HttpURLConnection) workflowurl.openConnection();
                String userpass = "icep603@gmail.com" + ":" + "creationfox";
                String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);

                connection.setRequestProperty("Authorization", basicAuth);
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("POST");
                // connection.setDoInput(true);
                //  connection.setDoOutput(true);
                connection.connect(); //send request

                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                dos.writeBytes(params[0]);//write post data which is a formatted json data representing inputs to a run
                //dos.writeUTF("");
                dos.flush();
                dos.close();

                InputStream dis = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(dis));

                String jsonData = "";
                while ((jsonData = br.readLine()) != null) {
                    sb.append(jsonData);
                }
                dis.close();
                br.close();
                Log.i("RESPONSE Code", "" + connection.getResponseCode());
                Log.i("RESPONSE Messsage", "" + connection.getResponseMessage());
                Log.i("Authorization ", "" + connection.getRequestProperty("Authorization"));

                return sb.toString();

            }catch (IOException ex){
                ex.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("RUN OutPut", s);
        }
    }
}
