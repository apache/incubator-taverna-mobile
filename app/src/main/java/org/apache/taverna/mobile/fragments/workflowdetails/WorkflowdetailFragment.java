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
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.activities.DashboardMainActivity;
import org.apache.taverna.mobile.tavernamobile.TavernaPlayerAPI;
import org.apache.taverna.mobile.tavernamobile.User;
import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.apache.taverna.mobile.utils.DetailsLoader;
import org.apache.taverna.mobile.utils.WorkflowDownloadManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
    static View rootView;
    private static ProgressDialog progressDialog;
    public AlertDialog runDialog;
    public AlertDialog.Builder alertDialogBuilder;
    private static String download_url;
    public static long WORKFLO_ID;
    public static Context cont;
    private static boolean LOAD_STATE = false;
    private ZoomControls zoomControls;
    static Animation zoomin;
    static Animation zoomout;

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
        progressDialog.setCancelable(false);
        WORKFLO_ID = workflowid;
        zoomin = AnimationUtils.loadAnimation(getActivity(), R.anim.zoomin);
        zoomout = AnimationUtils.loadAnimation(getActivity(), R.anim.zoomout);
        zoomControls = (ZoomControls) rootView.findViewById(R.id.zoomControls);
        zoomControls.setOnZoomInClickListener(this);
        zoomControls.setOnZoomOutClickListener(this);

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
        cont = getActivity();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.run_wk:
                //TODO implement functionality to issue a run request to the Taverna PLAYER to run the current workflow
                //new WorkflowRunTask(getActivity()).execute(""+WORKFLO_ID);
                new WorkflowProcessTask(getActivity()).execute(download_url);
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
            case R.id.zoomControls:
                zoomin.startNow();
                Toast.makeText(getActivity(), "Zooming", Toast.LENGTH_SHORT).show();
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
        if(!LOAD_STATE)
            getActivity().getLoaderManager().initLoader(1, null, this).forceLoad();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LOAD_STATE = true;
    }

    @Override
    public Loader<Workflow> onCreateLoader(int i, Bundle bundle) {
        progressDialog.show();
        return new DetailsLoader(getActivity(),
                DetailsLoader.LOAD_TYPE.TYPE_WORKFLOW_DETAIL,
                getActivity().getIntent().getStringExtra("uri"));
    }

    @Override
    public void onLoadFinished(Loader<Workflow> workflowLoader, Workflow workflow) {
       /* TextView author = (TextView) rootView.findViewById(R.id.wkf_author);
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
     */ //progressDialog.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<Workflow> workflowLoader) {
        workflowLoader.reset();
    }

    public static void setWorkflowDetails(final Workflow wk){

        ((Activity)cont).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //load necessary widgets
                TextView author = (TextView) rootView.findViewById(R.id.wkf_author);
                TextView updated = (TextView) rootView.findViewById(R.id.wupdatedat);
                TextView type = (TextView) rootView.findViewById(R.id.wtype);
                TextView title = (TextView) rootView.findViewById(R.id.wtitle);
                TextView desc = (TextView) rootView.findViewById(R.id.wdescription);
                TextView createdat = (TextView) rootView.findViewById(R.id.wcreatedat);
                final ImageView preview = (ImageView) rootView.findViewById(R.id.wkf_image);

                //set widget data
                User uploader = wk.getUploader();
                author.setText("Uploader ->" + uploader != null?uploader.getName():"Unknown");
                title.setText(wk.getWorkflow_title());
                desc.setText(wk.getWorkflow_description());
                createdat.setText("Created : " + wk.getWorkflow_datecreated());
                updated.setText("Workflow Description");
                type.setText("Type-> "+wk.getWorkflow_Type());

                  //preview.setImageURI(Uri.parse(wk.getWorkflow_preview()));
                new LoadImageThread(preview, wk.getWorkflow_preview()).execute();
                download_url =wk.getWorkflow_remote_url();
                zoomin.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
                        preview.startAnimation(zoomout);
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        preview.startAnimation(zoomout);

                    }
                });
                zoomout.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
                        preview.startAnimation(zoomin);
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        preview.startAnimation(zoomin);

                    }
                });
                progressDialog.dismiss();
            }
        });
    }

    private static class LoadImageThread extends AsyncTask<String, Void, Bitmap>{
          ImageView imageView;
          String src ;
        public LoadImageThread(ImageView image, String source) {
            imageView = image;
            src = source;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap myBitmap = null;
            try {
                URL url = new URL(src);
                HttpURLConnection connection = null;
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                 myBitmap = BitmapFactory.decodeStream(input);
//                imageView.setImageBitmap(myBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return myBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

    //create and return a new TextView
    public TextView createTextView(Context mcontext, String placeholder){
        TextView tv = new TextView(mcontext);
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

                URL workflowurl = new URL(new TavernaPlayerAPI(this.context).PLAYER_RUN_FRAMEWORK_URL+params[0]);
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

    /**
     *    Send request for the execution of a run on a Taverna server through the Taverna player using the Player API
     *    This process passes through several steps,\n
     *    1- Downloading and caching a local version of the workflow whose run we need \n
     *    2- uploading the workflow through the portal to register it so a run can be generated from it. The request requires some authentication
     *    3- Retrieving the results and extracting data required to create a run (the workflow_id) as provided by the results
     *    4- Posting a run request to the player so that a new run can be created and started
     *    5- retrieving a run framework so that users can know what types of input is expected for a successful run
     *    6- retrieving and displaying run results
     */
    private class WorkflowProcessTask extends AsyncTask<String, Void, String>{

        private Context context;

        private WorkflowProcessTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Uploading Workflow ... ");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuffer sb = new StringBuffer();
            try {
                //prepare connection requests
                URL workflowurl = new URL(params[0]); //the resource xml file representing the workflow to be uploaded to the player
                String playerurl = new TavernaPlayerAPI(this.context).PLAYER_BASE_URL+"workflows.json";
                URL posturl = new URL(playerurl);
                HttpURLConnection connection = (HttpURLConnection) posturl.openConnection();
                HttpURLConnection wconn = (HttpURLConnection) workflowurl.openConnection();
                    wconn.setRequestMethod("GET");
                    wconn.setDoOutput(true);
                    wconn.setRequestProperty("Accept", "application/xml");
                    wconn.connect();

                String user = "icep603@gmail.com" + ":" + "creationfox";
                String basicAuth = "Basic " + Base64.encodeToString(user.getBytes(), Base64.DEFAULT);
                //read the file from remote resource and encode the stream with a base64 algorithm
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(wconn.getInputStream()));
                String str = "";
                while ((str = bufferedReader.readLine()) != null)
                    sb.append(str); //in this string builder we have read the workflow( as .t2flow or .xml) workflow from remote resource. Now we need to post that to the player.
                bufferedReader.close();
                wconn.disconnect();

                       // Base64.encodeToString(sb.toString().getBytes("UTF-8"), Base64.DEFAULT)
                String data = "{\"document\":\"data:application/octet-stream;base64," +
                        "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHM6c2N1ZmwgeG1sbnM6cz0iaHR0cDovL29yZy5lbWJsLmViaS5lc2NpZW5jZS94c2N1ZmwvMC4xYWxwaGEiIHZlcnNpb249IjAuMiIgbG9nPSIwIj4KICA8czp3b3JrZmxvd2Rlc2NyaXB0aW9uIGxzaWQ9InVybjpsc2lkOnd3dy5teWdyaWQub3JnLnVrOm9wZXJhdGlvbjpLNlpDRzZJV05TMCIgYXV0aG9yPSIiIHRpdGxlPSIiIC8+CiAgPHM6cHJvY2Vzc29yIG5hbWU9IlN0cmluZ19Db25zdGFudCIgYm9yaW5nPSJ0cnVlIj4KICAgIDxzOnN0cmluZ2NvbnN0YW50Pmh0dHA6Ly93d3cuY3MubWFuLmFjLnVrL35nb2RlcmlzYS9QaG90by5qcGc8L3M6c3RyaW5nY29uc3RhbnQ+CiAgPC9zOnByb2Nlc3Nvcj4KICA8czpwcm9jZXNzb3IgbmFtZT0iR2V0X2ltYWdlX2Zyb21fVVJMIj4KICAgIDxzOmxvY2FsPm9yZy5lbWJsLmViaS5lc2NpZW5jZS5zY3VmbHdvcmtlcnMuamF2YS5XZWJJbWFnZUZldGNoZXI8L3M6bG9jYWw+CiAgPC9zOnByb2Nlc3Nvcj4KICA8czpsaW5rIHNvdXJjZT0iU3RyaW5nX0NvbnN0YW50OnZhbHVlIiBzaW5rPSJHZXRfaW1hZ2VfZnJvbV9VUkw6dXJsIiAvPgogIDxzOmxpbmsgc291cmNlPSJHZXRfaW1hZ2VfZnJvbV9VUkw6aW1hZ2UiIHNpbms9InZpeiIgLz4KICA8czpzaW5rIG5hbWU9InZpeiIgLz4KPC9zOnNjdWZsPgoKCg=="+"\"}";
                String post = "{\"workflow\":"+data+"}";
                //clear sb so that we can use it again to fetch results from this post request
                sb.delete(0,sb.length()-1);
                System.out.println("BODY=>"+post);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", basicAuth);
                connection.setRequestProperty("Accept", "*/*");
               // connection.setRequestProperty("Content-Length", "10165");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                connection.setUseCaches (false);
                connection.setDoOutput(true);
             //   connection.setDoInput(true);
                connection.connect(); //send request

                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                dos.writeBytes(post);//write post data which is a formatted json data representing body of workflow

                dos.flush();
                dos.close();
/*
                InputStream dis = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(dis));
                while ((str = br.readLine())!= null)
                    sb.append(str);*/
                System.out.println("Post Response Code: "+connection.getResponseCode());
                System.out.println("Post response message: "+connection.getResponseMessage());
                connection.disconnect();
            }catch (IOException e){
                e.printStackTrace();
                sb.append("Error reading remote workflow. Please try again later");
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
            progressDialog.dismiss();
        }
    }
    /**
     * creates a new workflow run from the
     */
    private class RunTask extends AsyncTask<String, Void, String>{

        private Context context;

        private RunTask(Context ctx) {
            this.context = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Creating new run for the workflow");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuffer sb = new StringBuffer();
            try {

                URL workflowurl = new URL(new TavernaPlayerAPI(this.context).PLAYER_RUN_URL);
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
            progressDialog.dismiss();
            //TODO startup the runActivity to display the run results
        }
    }
}
