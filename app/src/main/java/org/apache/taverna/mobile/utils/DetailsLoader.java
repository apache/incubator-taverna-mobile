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

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.apache.taverna.mobile.tavernamobile.Runs;
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

import static org.apache.taverna.mobile.utils.DetailsLoader.LOAD_TYPE.*;

/**
 * Created by root on 6/14/15.
 */
public class DetailsLoader extends AsyncTaskLoader<Workflow> {

    public static enum LOAD_TYPE {TYPE_WORKFLOW_DETAIL, TYPE_RUN_HISTORY,  TYPE_POLICY, TYPE_ABOUT_WORKFLOW};
    private LOAD_TYPE lt;
    private long wid;
    private Workflow workflow;
    private Context context;

    public DetailsLoader(Context context, LOAD_TYPE load_type, long id) {
        super(context);
        this.context = context;
        this.lt = load_type;
        this.wid = id;
    }

    @Override
    public Workflow loadInBackground() {
        //start a network request to fetch user's workflow details
        try {
            //for password protected urls use the user's credentials
            Authenticator.setDefault(new TavernaPlayerAPI.Authenticator("taverna", "taverna"));
            URL workflowurl;

            switch (this.lt){
                case TYPE_WORKFLOW_DETAIL:
                    workflowurl = new URL(new TavernaPlayerAPI(this.context).PLAYER_WORKFLOW_URL+this.wid);
                    break;
                case TYPE_RUN_HISTORY:
                    workflowurl = new URL(new TavernaPlayerAPI(this.context).PLAYER_RUN_URL);
                    break;
                case TYPE_POLICY:
                    workflowurl = new URL(new TavernaPlayerAPI(this.context).SERVER_BASE_URL);
                    break;
                case TYPE_ABOUT_WORKFLOW:
                    workflowurl = new URL(new TavernaPlayerAPI(this.context).PLAYER_WORKFLOW_URL);
                    break;
                default:
                    workflowurl = new URL(new TavernaPlayerAPI(this.context).PLAYER_WORKFLOW_URL);
                    break;
            }
            HttpURLConnection connection = (HttpURLConnection) workflowurl.openConnection();
            String userpass = "icep603@gmail.com" + ":" + "creationfox";
            String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);

            connection.setRequestProperty ("Authorization", basicAuth);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("GET");
            // connection.setDoInput(true);
            //  connection.setDoOutput(true);
            connection.connect(); //send request
            Log.i("RESPONSE Code", "" + connection.getResponseCode());
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
            switch(this.lt) {
                case TYPE_WORKFLOW_DETAIL: {
                    JSONObject js = new JSONObject(sb.toString());
                        Log.i("JSON ", js.toString(2));
                        String created_at = js.getString("created_at");
                        String updated_at = js.getString("updated_at");
                        JSONObject user = js.getJSONObject("user");
                    workflow = new Workflow(this.context, js.getString("title"),
                            user.getString("name"),
                            js.getString("description"),
                            js.getInt("id"),
                            js.getString("url"));
                    workflow.setWorkflow_datecreated(created_at);
                    workflow.setWorkflow_datemodified(updated_at);
                }
                    return workflow;
                case TYPE_RUN_HISTORY:{
                    workflow = new Workflow(this.context);
                    JSONArray jsonArray = new JSONArray(sb.toString());
                    Log.i("JSON ", jsonArray.toString(2));
                    for(int j=0; j< jsonArray.length();j++){
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        long id = jsonObject.getLong("id");
                        long workflow_id = jsonObject.getLong("workflow_id");
                        String name = jsonObject.getString("name");
                        String started = jsonObject.getString("start_time");
                        String ended = jsonObject.getString("finish_time");
                        String state = jsonObject.getString("state");

                        if(workflow_id == this.wid) {
                            Runs mrun = new Runs(name,started,ended,state);
                            mrun.setRun_id(id);
                            mrun.setRun_workflow_id(workflow_id);

                            workflow.addWorkflowRun(mrun);
                        }
                    }

                }
                    return workflow;
                case TYPE_POLICY:{

                }
                    return workflow;
                case TYPE_ABOUT_WORKFLOW:{

                }
                    return workflow;
                default:
                    return workflow;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return workflow;
    }

    @Override
    protected void onStartLoading() {
        if(workflow != null){
            deliverResult(workflow);
        }else{
            forceLoad();
        }
    }

    @Override
    public void deliverResult(Workflow data) {
        if(isStarted()){
            super.deliverResult(data);
        }
    }
}
