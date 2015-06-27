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

import com.thebuzzmedia.sjxp.rule.IRule;

import org.apache.taverna.mobile.tavernamobile.TavernaPlayerAPI;
import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.apache.taverna.mobile.utils.xmlparsers.MyExperimentXmlParserRules;
import org.apache.taverna.mobile.utils.xmlparsers.WorkflowDetailParser;
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

/**
 * Loads workflow details from the myexperiment API and presents them on the UI .The class is generic and can be used to load the
 * different details sections of the app.
 * Created by Larry Akah on 6/14/15.
 */
public class DetailsLoader extends AsyncTaskLoader<Workflow> {

    public static enum LOAD_TYPE {TYPE_WORKFLOW_DETAIL, TYPE_RUN_HISTORY,  TYPE_POLICY, TYPE_ABOUT_WORKFLOW};
    private LOAD_TYPE lt;
    private String uri;
    private Workflow workflow;
    private Context context;

    public DetailsLoader(Context context, LOAD_TYPE load_type, String detailsRUI) {
        super(context);
        this.context = context;
        this.lt = load_type;
        this.uri = detailsRUI;
        this.workflow = new Workflow();
    }

    @Override
    public Workflow loadInBackground() {
        //start a network request to fetch user's workflow details
        try {
            Log.i("LOADER STARTED", "loading data");
            //for password protected urls use the user's credentials
            Authenticator.setDefault(new TavernaPlayerAPI.Authenticator("taverna", "taverna"));
            URL workflowurl;

            switch (this.lt){
                case TYPE_WORKFLOW_DETAIL:
                    workflowurl = new URL(this.uri);
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

         //   connection.setRequestProperty ("Authorization", basicAuth);
           // connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("GET");
            // connection.setDoInput(true);
            //  connection.setDoOutput(true);
            connection.connect(); //send request
            Log.i("RESPONSE Code", "" + connection.getResponseCode());
            Log.i("RESPONSE Messsage", ""+connection.getResponseMessage());

            InputStream dis = connection.getInputStream();

            switch(this.lt) {
                case TYPE_WORKFLOW_DETAIL: {
                    //make rules and apply the parser
                    IRule workfl = new MyExperimentXmlParserRules.WorkflowDetailRule(IRule.Type.ATTRIBUTE,
                            "/workflow", "uri","resource", "id","version");
                    IRule title = new MyExperimentXmlParserRules.TitleRule(IRule.Type.CHARACTER,"/workflow/title");
                    IRule description = new MyExperimentXmlParserRules.DescriptionRule(IRule.Type.CHARACTER, "/workflow/description");
                    IRule type = new MyExperimentXmlParserRules.TypeRule(IRule.Type.CHARACTER, "/workflow/type");
                    IRule attrType = new MyExperimentXmlParserRules.TypeRule(IRule.Type.ATTRIBUTE, "/workflow/type", "resource", "uri","id");
                    IRule uploader = new MyExperimentXmlParserRules.UploaderRule(IRule.Type.CHARACTER, "/workflow/uploader");
                    IRule attrUploader = new MyExperimentXmlParserRules.UploaderRule(IRule.Type.ATTRIBUTE, "/workflow/uploader",  "resource", "uri","id");
                    IRule date = new MyExperimentXmlParserRules.DateRule(IRule.Type.CHARACTER, "/workflow/created-at");
                    IRule preview = new MyExperimentXmlParserRules.PreviewRule(IRule.Type.CHARACTER, "/workflow/preview");
                    IRule licetype = new MyExperimentXmlParserRules.LicenceTypeRule(IRule.Type.CHARACTER, "/workflow/licence-type");
                    IRule attrlicetype = new MyExperimentXmlParserRules.LicenceTypeRule(IRule.Type.ATTRIBUTE,"/workflow/licence-type", "resource", "uri","id");
                    IRule contenturi = new MyExperimentXmlParserRules.ContentUriRule(IRule.Type.CHARACTER, "/workflow/content-uri");
                    IRule contentType = new MyExperimentXmlParserRules.ContentTypeRule(IRule.Type.CHARACTER, "/workflow/content-type");
                    IRule tags = new MyExperimentXmlParserRules.TagsRule(IRule.Type.CHARACTER, "/workflow/tags/tag");
                    IRule attrTags = new MyExperimentXmlParserRules.TagsRule(IRule.Type.ATTRIBUTE, "/workflow/tags/tag", "resource", "uri","id");

                    WorkflowDetailParser parser = new WorkflowDetailParser(new IRule[]{workfl,title,description,type,
                            attrlicetype,attrType, uploader,attrUploader,date,preview,licetype,contenturi,contentType,tags,attrTags});
                 //   System.out.println(sb.toString());
                    parser.parse(dis, this.workflow);
                }
                dis.close();
                //br.close();
                    return workflow;
                case TYPE_RUN_HISTORY:{
                    BufferedReader br = new BufferedReader(new InputStreamReader(dis));
                    StringBuffer sb = new StringBuffer();
                    String jsonData = "";
                    while((jsonData = br.readLine()) != null){
                        sb.append(jsonData);
                    }
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
/*
                        if(workflow_id == this.wid) {
                            Runs mrun = new Runs(name,started,ended,state);
                            mrun.setRun_id(id);
                            mrun.setRun_workflow_id(workflow_id);

                            workflow.addWorkflowRun(mrun);
                        }
                        */
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
                    BufferedReader br = new BufferedReader(new InputStreamReader(dis));
                    StringBuffer sb = new StringBuffer();
                    String jsonData = "";
                    while((jsonData = br.readLine()) != null){
                        sb.append(jsonData);
                    }
                    dis.close();
                    br.close();
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
    public boolean isStarted() {
        return super.isStarted();
    }

    @Override
    protected void onStartLoading() {
      /*  if(workflow != null){
            deliverResult(workflow);
        }else{
            forceLoad();
        }*/
        forceLoad();
        Log.i("Loading State","loading started");
    }

    @Override
    protected void onStopLoading() {
        Log.i("Loading State","loading stopped");
    }

    @Override
    public void deliverResult(Workflow data) {
        if(isStarted()){
            super.deliverResult(data);
        }
    }
}
/**
 * JSONObject js = new JSONObject(sb.toString());
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
 **/