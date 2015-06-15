package org.apache.taverna.mobile.tavernamobile;
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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.taverna.mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 6/8/15.
 */
public class Workflow {
    private Context context;
    private String workflow_author;
    private String workflow_title;
    private String workflow_description, about, policy;
    private String workflow_datecreated, workflow_datemodified;
    private Bitmap workflow_author_bitmap;
    private WorkflowComponent workflowComponent;
    private String workflow_remote_url;
    private List<Runs> workflow_runs;
    private int workflow_input;
    private long id;

    public static enum workflow_input_type{ TYPE_INT, TYPE_STRING, TYPE_OBJECT};

    public Workflow(Context context) {
        this.context = context;
        this.workflow_runs = new ArrayList<Runs>();
    }

    public Workflow(Context ctx, String title, String author, String description, long id, String url){
        this.context = ctx;
        this.workflow_author = author;
        this.workflow_author_bitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ic_userprofile);
        this.workflow_description =description;
        this.workflow_title =title;
        this.workflow_input=1;
        this.id = id;
        this.workflow_remote_url = url;
        this.workflow_runs = new ArrayList<Runs>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWorkflow_datecreated() {
        return this.workflow_datecreated;
    }

    public String getWorkflow_remote_url() {
        return this.workflow_remote_url;
    }

    public void setWorkflow_remote_url(String workflow_remote_url) {
        this.workflow_remote_url = workflow_remote_url;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setWorkflow_datecreated(String workflow_datecreated) {
        this.workflow_datecreated = workflow_datecreated;
    }

    public String getWorkflow_datemodified() {
        return this.workflow_datemodified;
    }

    public void setWorkflow_datemodified(String workflow_datemodified) {
        this.workflow_datemodified = workflow_datemodified;
    }

    public List<Runs> getWorkflow_runs() {
        return this.workflow_runs;
    }

    public void setWorkflow_runs(List<Runs> workflow_runs) {
        this.workflow_runs = workflow_runs;
    }

    public void addWorkflowRun(Runs runs){ //adds a run to this workflow
        this.workflow_runs.add(runs);
    }

    public int getWorkflow_input() {
        return this.workflow_input;
    }

    public void setWorkflow_input(int workflow_input) {
        this.workflow_input = workflow_input;
    }

    public workflow_input_type getInputType(){
        return workflow_input_type.TYPE_INT;
    }

    public String getWorkflow_author() {
        return this.workflow_author;
    }

    public String getWorkflow_description() {
        return this.workflow_description;
    }

    public String getWorkflow_title() {
        return this.workflow_title;
    }

    public Bitmap getWorkflow_author_bitmap() {
        return this.workflow_author_bitmap;
    }

    public WorkflowComponent getWorkflowComponent() {
        return this.workflowComponent;
    }

    public void setWorkflow_author(String workflow_author) {
        this.workflow_author = workflow_author;
    }

    public void setWorkflow_title(String workflow_title) {
        this.workflow_title = workflow_title;
    }

    public void setWorkflow_description(String workflow_description) {
        this.workflow_description = workflow_description;
    }

    public void setWorkflow_author_bitmap(Bitmap workflow_author_bitmap) {
        this.workflow_author_bitmap = workflow_author_bitmap;
    }

    public void setWorkflowComponent(WorkflowComponent workflowComponent) {
        this.workflowComponent = workflowComponent;
    }
}
/* Use the sample code if it becomes necessary to pass this objects amongst activities
/ simple class that just has one member property as an example
public class Workflow implements Parcelable {
    private int mData;

    */
/* everything below here is for implementing Parcelable *//*


    // 99.9% of the time you can just ignore this
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
public static final Parcelable.Creator<Workflow> CREATOR = new Parcelable.Creator<Workflow>() {
    public Workflow createFromParcel(Parcel in) {
        return new Workflow(in);
    }

    public Workflow[] newArray(int size) {
        return new Workflow[size];
    }
};

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Workflow(Parcel in) {
        mData = in.readInt();
    }
}

//application in intents. Sending object to another activity
    Intent it = new Intent();
    it.putExtra("parsedWorkflow", myWorkflow);
//retrieve object
    Workflow mWorkflow = (Workflow) getIntent().getParcelableExtra("parsedWorkflow");

 */
