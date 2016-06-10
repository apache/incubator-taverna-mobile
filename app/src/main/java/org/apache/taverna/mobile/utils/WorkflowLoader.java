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

import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.apache.taverna.mobile.utils.xmlparsers.MyExperimentXmlParserRules;
import org.apache.taverna.mobile.utils.xmlparsers.WorkflowParser;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry Akah on 6/13/15.
 */
public class WorkflowLoader extends AsyncTask<String, Object, Object> { //WorkflowLoaderMain {

    private Context ctx;
    private List<Workflow> userWorkflows;
    private SwipeRefreshLayout refreshLayout;

    public WorkflowLoader(Context context, SwipeRefreshLayout sw) {
        this.ctx = context;
        this.refreshLayout = sw;
        this.userWorkflows = new ArrayList<Workflow>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        refreshLayout.setRefreshing(true);
    }

    @Override
    public List<Workflow> doInBackground(String[] pages) {
        //start a network request to fetch user's workflows

        IRule wkflowRule = new MyExperimentXmlParserRules.WorkflowRule(IRule.Type.ATTRIBUTE,
                "/workflows/workflow", "resource", "uri", "id", "version");
        IRule workflowNameRule = new MyExperimentXmlParserRules.WorkflowRule(IRule.Type
                .CHARACTER, "/workflows/workflow");
        WorkflowParser xmlParser = new WorkflowParser(new IRule[]{wkflowRule, workflowNameRule});
        try {
            URL workflowurl = new URL("http://www.myexperiment.org/workflows.xml?page=" + Integer
                    .parseInt((pages[0])));
            HttpURLConnection connection = (HttpURLConnection) workflowurl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect(); //send request
            Log.i("WorkflowLoader", "Loading workflows page = " + pages[0]);

            InputStream dis = connection.getInputStream();
            xmlParser.parse(dis, this.userWorkflows);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this.userWorkflows;
    }

    @Override
    protected void onPostExecute(Object o) {
        refreshLayout.setRefreshing(false);
        System.out.println("Workflow Count: " + this.userWorkflows.size());
    }
}
