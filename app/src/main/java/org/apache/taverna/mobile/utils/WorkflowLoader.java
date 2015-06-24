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
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;

import com.thebuzzmedia.sjxp.XMLParser;
import com.thebuzzmedia.sjxp.rule.DefaultRule;
import com.thebuzzmedia.sjxp.rule.IRule;

import org.apache.taverna.mobile.adapters.WorkflowAdapter;
import org.apache.taverna.mobile.tavernamobile.TavernaPlayerAPI;
import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry Akah on 6/13/15.
 */
public class WorkflowLoader extends AsyncTask<Object, Object, Object>{ //WorkflowLoaderMain {

    private Context ctx;
    private List<Workflow> userWorkflows;
    public static List<Workflow> loadedWorkflows;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

  /*  public WorkflowLoader(Context context) {
        super(context);
        ctx = context;
        loadedWorkflows = new ArrayList<Workflow>();
        userWorkflows = new ArrayList<Workflow>();
    }*/

    public WorkflowLoader(Context context, RecyclerView rc, SwipeRefreshLayout sw) {
        this.ctx = context;
        this.recyclerView = rc;
        this.refreshLayout = sw;
        this.userWorkflows = new ArrayList<Workflow>();
        loadedWorkflows = new ArrayList<Workflow>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        refreshLayout.setRefreshing(true);
    }

    @Override
    public List<Workflow> doInBackground(Object[] objects) {
        //start a network request to fetch user's workflows
        /*try {
            //for password protected urls use the user's credentials
            //Authenticator.setDefault(new TavernaPlayerAPI.Authenticator("taverna","taverna"));

            URL workflowurl = new URL(new TavernaPlayerAPI(ctx).PLAYER_WORKFLOW_URL);
            HttpURLConnection connection = (HttpURLConnection) workflowurl.openConnection();
            String userpass = "icep603@gmail.com" + ":" + "creationfox";
            String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
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
                JSONObject authorJson = js.getJSONObject("user");
                String title = js.getString("title");
                String description = js.getString("description");
                String url = js.getString("url");
                long id = js.getLong("id");
                userWorkflows.add(new Workflow(ctx,title,">"+authorJson.getString("name"),description,id,url));
            }
            return userWorkflows;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }*/
        IRule wkflowRule = new MyExperimentXmlParser.WorkflowRule(IRule.Type.ATTRIBUTE, "/workflows/workflow", "resource", "uri","id", "version");
        IRule workflowNameRule = new MyExperimentXmlParser.WorkflowRule(IRule.Type.CHARACTER, "/workflows/workflow");
        WorkflowParser xmlParser = new WorkflowParser(new IRule[]{wkflowRule, workflowNameRule}, recyclerView, this.ctx);
        try {
            URL workflowurl = new URL("http://www.myexperiment.org/workflows.xml");
            HttpURLConnection connection = (HttpURLConnection) workflowurl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect(); //send request

            Log.i("RESPONSE Code", ""+connection.getResponseCode());
            Log.i("RESPONSE Messsage", ""+connection.getResponseMessage());

            InputStream dis = connection.getInputStream();
            xmlParser.parse(dis, this.userWorkflows);
            Thread.sleep(4000);//4sec delay so that parsing completes

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       // return WorkflowLoader.loadedWorkflows;
        return this.userWorkflows;
    }

/*
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
*/

    @Override
    protected void onPostExecute(Object o) {
        refreshLayout.setRefreshing(false);
        System.out.println("Workflow Count: "+this.userWorkflows.size());
      //  recyclerView.setAdapter(new WorkflowAdapter(this.ctx, (List<Workflow>) o));
    }

    class WorkflowRule extends DefaultRule {
        Workflow workflow;

        /**
         * Create a new rule with the given values.
         *
         * @param type           The type of the rule.
         * @param locationPath   The location path of the element to target in the XML.
         * @param attributeNames An optional list of attribute names to parse values for if the
         *                       type of this rule is {@link com.thebuzzmedia.sjxp.rule.IRule.Type#ATTRIBUTE}.
         * @throws IllegalArgumentException if <code>type</code> is <code>null</code>, if
         *                                            <code>locationPath</code> is <code>null</code> or empty, if
         *                                            <code>type</code> is {@link com.thebuzzmedia.sjxp.rule.IRule.Type#ATTRIBUTE} and
         *                                            <code>attributeNames</code> is <code>null</code> or empty or
         *                                            if <code>type</code> is {@link com.thebuzzmedia.sjxp.rule.IRule.Type#CHARACTER} and
         *                                            <code>attributeNames</code> <strong>is not</strong>
         *                                            <code>null</code> or empty.
         */
        public WorkflowRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
            workflow = new Workflow();
        }
        //instantiated to parse xml data for a given workflow
        public WorkflowRule(Type type, String path, int id, String attributenames){
            super(type,path,attributenames);
        }

        /**
         * Default no-op implementation. Please override with your own logic.
         *
         * @param parser
         * @param index
         * @param value
         * @param userObject
         * @see com.thebuzzmedia.sjxp.rule.IRule#handleParsedAttribute(com.thebuzzmedia.sjxp.XMLParser, int, String, Object)
         */
        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object userObject) {
            switch(index){
                case 0:
                    System.out.println("Workflow Resource: "+value);
                    workflow.setWorkflow_web_url(value);
                    workflow.setWorkflow_description("To view workflow on the web, click "+value);
                    break;
                case 1:
                    System.out.println("Workflow uri: "+value); //uri for detailed workflow
                    workflow.setWorkflow_remote_url(value);
                    break;
                case 2:
                    System.out.println("Workflow id: "+value);
                    workflow.setId(Integer.parseInt(value));
                    break;
                case 3:
                    System.out.println("Workflow version: "+value);
                    workflow.setWorkflow_versions(value);
                    break;
            }
        }

        /**
         * Default no-op implementation. Please override with your own logic.
         *
         * @param parser
         * @param text
         * @param workflowListObject
         * @see com.thebuzzmedia.sjxp.rule.IRule#handleParsedCharacters(com.thebuzzmedia.sjxp.XMLParser, String, Object)
         */
        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object workflowListObject) {
            //add the title to the workflow and add it to the workflow list
            workflow.setWorkflow_title(text);
            workflow.setWorkflow_author("");
            workflow = new Workflow();
            ((List<Workflow>)workflowListObject).add(workflow);
        }
    }
}
