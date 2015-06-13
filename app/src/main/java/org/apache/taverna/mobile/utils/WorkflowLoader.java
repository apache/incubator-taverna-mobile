package org.apache.taverna.mobile.utils;
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
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.apache.taverna.mobile.tavernamobile.TavernaPlayerAPI;
import org.apache.taverna.mobile.tavernamobile.Workflow;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry Akah on 6/13/15.
 */
public class WorkflowLoader extends AsyncTaskLoader<List<Workflow>> {

    private Context ctx;
    private List<Workflow> userWorkflows;

    public WorkflowLoader(Context context) {
        super(context);
        ctx = context;
    }

    @Override
    public List<Workflow> loadInBackground() {
         userWorkflows = new ArrayList<Workflow>();
        //start a network request to fetch user's workflows
        try {
            //for password protected urls use the user's credentials
            Authenticator.setDefault(new TavernaPlayerAPI.Authenticator("taverna","taverna"));

            URL workflowurl = new URL(TavernaPlayerAPI.PLAYER_URL+"workflows");
            HttpURLConnection connection = (HttpURLConnection) workflowurl.openConnection();
            String userpass = "icep603@gmail.com" + ":" + "creationfox";
            String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(),Base64.DEFAULT);
            //new String(Base64.encode(userpass.getBytes(),Base64.DEFAULT));

            connection.setRequestProperty ("Authorization", basicAuth);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("GET");
           // connection.setDoInput(true);
          //  connection.setDoOutput(true);
            connection.connect(); //send request
            Log.i("RESPONSE Code", ""+connection.getResponseCode());
            Log.i("RESPONSE Messsage", ""+connection.getResponseMessage());
            Log.i("Authorization ", ""+connection.getRequestProperty("Authorization"));

            InputStream dis = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            StringBuffer sb = new StringBuffer();
            String jsonData = "";
            while((jsonData = br.readLine()) != null){
                sb.append(jsonData);
            }
            dis.close();
            br.close();
            JSONArray jsonWorkflow = new JSONArray(sb.toString());
            for(int i=0; i<jsonWorkflow.length();i++){
                JSONObject js = jsonWorkflow.getJSONObject(i);
                Log.i("JSON ", js.toString(2));
                //String author = js.getString("author");
                String title = js.getString("title");
                String description = js.getString("description");
                String url = js.getString("url");
                long id = js.getLong("id");
                userWorkflows.add(new Workflow(ctx,title,"Larry",description,id,url));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userWorkflows;
    }

    @Override
    public void onCanceled(List<Workflow> data) {
        super.onCanceled(data);
    }

    @Override
    public boolean isStarted() {
        return super.isStarted();
    }

    @Override
    protected void onStartLoading() {
        //if there is data available, deliver it at once
        ((Activity)ctx).setProgressBarIndeterminateVisibility(true);
        if(userWorkflows != null)
            deliverResult(userWorkflows);
        else{
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<Workflow> data) {
        if(isStarted()){
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}
