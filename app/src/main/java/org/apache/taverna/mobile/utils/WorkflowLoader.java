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

import org.apache.taverna.mobile.adapters.WorkflowAdapter;
import org.apache.taverna.mobile.tavernamobile.TavernaPlayerAPI;
import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
            URL workflowurl = new URL(TavernaPlayerAPI.PLAYER_URL+"workflows");
            HttpURLConnection connection = (HttpURLConnection) workflowurl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestMethod("GET");
            connection.connect(); //send request

//            DataInputStream dis = new DataInputStream(connection.getInputStream());
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
                String author = js.getString("author");
                String title = js.getString("title");
                String description = js.getString("description");
                long id = js.getLong("id");
                userWorkflows.add(new Workflow(ctx,title,author,description,id,""));
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
