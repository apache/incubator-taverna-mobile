package org.apache.taverna.mobile.utils;
/**
 * Apache Taverna Mobile
 * Copyright 2015 The Apache Software Foundation
 *
 * This product includes software developed at
 * The Apache Software Foundation (http://www.apache.org/).
 *
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

import com.thebuzzmedia.sjxp.rule.IRule;

import org.apache.taverna.mobile.tavernamobile.Runs;
import org.apache.taverna.mobile.tavernamobile.TavernaPlayerAPI;
import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.apache.taverna.mobile.utils.xmlparsers.MyExperimentXmlParserRules;
import org.apache.taverna.mobile.utils.xmlparsers.WorkflowDetailParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Loads workflow details from the myexperiment API and presents them on the UI .The class is
 * generic and can be used to load the
 * different details sections of the app.
 * Created by Larry Akah on 6/14/15.
 */
public class DetailsLoader extends AsyncTaskLoader<Workflow> {

    private static final String TAG = "DetailsLoader";
    private LoadType lt;
    private String uri;
    private Workflow workflow;
    private Context context;

    public DetailsLoader(Context context, LoadType LoadType, String dataParam) {
        super(context);
        this.context = context;
        this.lt = LoadType;
        uri = dataParam;
        this.workflow = new Workflow();
    }

    @Override
    public Workflow loadInBackground() {
        HttpURLConnection connection = null;
        InputStream dis = null;
        //start a network request to fetch user's workflow details
        try {
            Log.i("LOADER STARTED", "loading data");
            //for password protected urls use the user's credentials
            Authenticator.setDefault(new TavernaPlayerAPI.Authenticator("taverna", "taverna"));
            TavernaPlayerAPI tavernaPlayerAPI = new TavernaPlayerAPI();
            URL workflowurl;

            switch (this.lt) {
                case TYPE_WORKFLOW_DETAIL:
                    workflowurl = new URL(uri);
                    connection = (HttpURLConnection) workflowurl.openConnection();
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("GET");
                    connection.connect(); //send request

                    Log.i("Workflow Response Code", "" + connection.getResponseCode());
                    Log.i("Workflow Response msg", "" + connection.getResponseMessage());
                    dis = connection.getInputStream();
                    break;
                case TYPE_RUN_HISTORY:
                    workflowurl = new URL(new TavernaPlayerAPI(this.context).mPlayerRunUrl);
                    connection = (HttpURLConnection) workflowurl.openConnection();
                    String userpass = tavernaPlayerAPI.getPlayerUserName(this.context) + ":" +
                            tavernaPlayerAPI.getPlayerUserPassword(this.context);
                    String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(Charset
                            .forName("UTF-8")),
                            Base64.DEFAULT);

                    connection.setRequestProperty("Authorization", basicAuth);
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("GET");
                    connection.connect(); //send request
                    Log.i("RESPONSE CODE", "" + connection.getResponseCode());
                    Log.i("RESPONSE Messsage. Run", "" + connection.getResponseMessage());
                    dis = connection.getInputStream();
                    break;
                case TYPE_POLICY:
                    workflowurl = new URL(new TavernaPlayerAPI(this.context).mServerBaseUrl);
                    break;
                default:
                    workflowurl = new URL(new TavernaPlayerAPI(this.context).mPlayerWorkFlowUrl);
                    break;
            }

            switch (this.lt) {
                case TYPE_WORKFLOW_DETAIL: {
                    //make rules and apply the parser
                    IRule workfl = new MyExperimentXmlParserRules.WorkflowDetailRule(IRule.Type
                            .ATTRIBUTE,
                            "/workflow", "uri", "resource", "id", "version");
                    IRule title = new MyExperimentXmlParserRules.TitleRule(IRule.Type.CHARACTER,
                            "/workflow/title");
                    IRule description = new MyExperimentXmlParserRules.DescriptionRule(IRule.Type
                            .CHARACTER, "/workflow/description");
                    IRule type = new MyExperimentXmlParserRules.TypeRule(IRule.Type.CHARACTER,
                            "/workflow/type");
                    IRule attrType = new MyExperimentXmlParserRules.TypeRule(IRule.Type
                            .ATTRIBUTE, "/workflow/type", "resource", "uri", "id");
                    IRule uploader = new MyExperimentXmlParserRules.UploaderRule(IRule.Type
                            .CHARACTER, "/workflow/uploader");
                    IRule attrUploader = new MyExperimentXmlParserRules.UploaderRule(IRule.Type
                            .ATTRIBUTE, "/workflow/uploader", "resource", "uri", "id");
                    IRule date = new MyExperimentXmlParserRules.DateRule(IRule.Type.CHARACTER,
                            "/workflow/created-at");
                    IRule preview = new MyExperimentXmlParserRules.PreviewRule(IRule.Type
                            .CHARACTER, "/workflow/preview");
                    IRule licetype = new MyExperimentXmlParserRules.LicenceTypeRule(IRule.Type
                            .CHARACTER, "/workflow/licence-type");
                    IRule attrlicetype = new MyExperimentXmlParserRules.LicenceTypeRule(IRule
                            .Type.ATTRIBUTE, "/workflow/licence-type", "resource", "uri", "id");
                    IRule contenturi = new MyExperimentXmlParserRules.ContentUriRule(IRule.Type
                            .CHARACTER, "/workflow/content-uri");
                    IRule contentType = new MyExperimentXmlParserRules.ContentTypeRule(IRule.Type
                            .CHARACTER, "/workflow/content-type");
                    IRule tags = new MyExperimentXmlParserRules.TagsRule(IRule.Type.CHARACTER,
                            "/workflow/tags/tag");
                    IRule attrTags = new MyExperimentXmlParserRules.TagsRule(IRule.Type
                            .ATTRIBUTE, "/workflow/tags/tag", "resource", "uri", "id");

                    WorkflowDetailParser parser = new WorkflowDetailParser(new IRule[]{workfl,
                            title, description, type,
                            attrlicetype, attrType, uploader, attrUploader, date, preview,
                            licetype, contenturi, contentType, tags, attrTags});
                    //   Log.e(TAG, sb.toString());
                    parser.parse(dis, this.workflow);
                }
                    dis.close();
                //br.close();
                return workflow;
                case TYPE_RUN_HISTORY: {
                    Log.e(TAG, "Downloading run history");
                    BufferedReader br = new BufferedReader(new InputStreamReader(dis, "UTF-8"));
                    StringBuffer sb = new StringBuffer();
                    String jsonData = "";
                    while ((jsonData = br.readLine()) != null) {
                        sb.append(jsonData);
                    }
                    workflow = new Workflow(this.context);
                    JSONArray jsonArray = new JSONArray(sb.toString());
                    Log.i("RUN JSON ", jsonArray.toString(2));
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        long id = jsonObject.getLong("id");
                        long workflow_id = jsonObject.getLong("workflow_id");
                        String name = jsonObject.getString("name");
                        String started = jsonObject.getString("start_time");
                        String ended = jsonObject.getString("finish_time");
                        String state = jsonObject.getString("state");
                        JSONObject userobj = jsonObject.getJSONObject("user");
                        String username = userobj.getString("name");
                        StringBuffer nm = new StringBuffer(), ur = new StringBuffer();
                        for (String n : name.toLowerCase().split(" ")) {
                            nm.append(n);
                        }
                        for (String p : uri.toLowerCase().split(" ")) {
                            ur.append(p);
                        }
                        if (nm.toString().equals(ur.toString())) {
                            Runs mrun = new Runs(name, started, ended, state);
                            mrun.setrunId(id);
                            mrun.setRunWorkflowId(workflow_id);
                            mrun.setRunAuthor(username);

                            workflow.addWorkflowRun(mrun);
                        }
                    }
                }
                return workflow;
                case TYPE_POLICY: {

                }
                return workflow;
                default:
                    BufferedReader br = new BufferedReader(new InputStreamReader(dis, "UTF-8"));
                    StringBuffer sb = new StringBuffer();
                    String jsonData = "";
                    while ((jsonData = br.readLine()) != null) {
                        sb.append(jsonData);
                    }
                    dis.close();
                    br.close();
                    return workflow;
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "loadInBackground: ", e);
        } catch (IOException e) {
            Log.e(TAG, "loadInBackground: ", e);
        } catch (JSONException e) {
            Log.e(TAG, "loadInBackground: ", e);
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
        Log.i("Loading State", "loading started");
    }

    @Override
    protected void onStopLoading() {
        Log.i("Loading detailComponent", "loading stopped");
    }

    @Override
    public void deliverResult(Workflow data) {
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    public static enum LoadType {
        TYPE_WORKFLOW_DETAIL, TYPE_RUN_HISTORY, TYPE_POLICY,
        TYPE_ABOUT_WORKFLOW
    }
}
/**
 * JSONObject js = new JSONObject(sb.toString());
 * Log.i("JSON ", js.toString(2));
 * String created_at = js.getString("created_at");
 * String updated_at = js.getString("updated_at");
 * JSONObject user = js.getJSONObject("user");
 * workflow = new Workflow(this.context, js.getString("title"),
 * user.getString("name"),
 * js.getString("description"),
 * js.getInt("id"),
 * js.getString("url"));
 * workflow.setWorkflowDatecreated(created_at);
 * workflow.setWorkflowDatemodified(updated_at);
 **/