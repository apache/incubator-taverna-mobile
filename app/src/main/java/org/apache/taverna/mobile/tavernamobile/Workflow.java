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
    private User uploader;
    private String workflow_author;
    private String workflow_title;
    private String workflow_description, about, policy;
    private String workflow_datecreated, workflow_datemodified;
    private Bitmap workflow_author_bitmap;
    private WorkflowComponent workflowComponent;
    private String workflow_remote_url; //provides a link to download the workflow. Equivalent to content-uri in the xml form
    private String workflow_web_url; //a string containing the workflow resource that can be loaded in  browser
    private String workflow_details_url;//used to refer to the details of the workflow
    private List<Runs> workflow_runs;
    private int workflow_input;
    private long id;
    private String workflow_uploader;//indicate the user who uploaded the workflow
    private String workflow_Type; //describes whether it is a type 1 or 2 workflow
    private String workflow_preview; //a url to a preview image of the workflow
    private String workflow_thumb_big; //a url to a full scale image of the workflow. I Will usually an SVG because the it is available for most of the workflows
    private String workflow_licence_type; //describes a type of licensing for the workflow
    private String workflow_content_type;//specifies a content type for the workflow;
    private List<String> workflow_tags;//provides a list of string tags that could be used to index the workflow for searches
    private String workflow_versions;//a list of version for the workflow uploaded over time
    private List<String> workflow_credits;//key contributors to the workflow

    public static enum workflow_input_type{ TYPE_INT, TYPE_STRING, TYPE_OBJECT};

    public Workflow() {
    }

    public Workflow(Context context) {
        this.context = context;
        this.workflow_runs = new ArrayList<Runs>();
    }
    public Workflow(String author, String description, long id, String url){
        this.workflow_author = author;
        this.workflow_author_bitmap = null;//BitmapFactory.decodeResource(getResources(), R.drawable.ic_userprofile);
        this.workflow_description =description;
        this.workflow_input=1;
        this.id = id;
        this.workflow_remote_url = url;
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
        return this.id;
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

    public String getWorkflow_details_url() {
        return this.workflow_details_url;
    }

    public User getUploader() {
        return this.uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    public void setWorkflow_details_url(String workflow_details_url) {
        this.workflow_details_url = workflow_details_url;
    }

    public String getPolicy() {
        return this.policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getAbout() {
        return this.about;
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

    public String getWorkflow_web_url() {
        return workflow_web_url;
    }

    public void setWorkflow_web_url(String workflow_web_url) {
        this.workflow_web_url = workflow_web_url;
    }

    public String getWorkflow_uploader() {
        return workflow_uploader;
    }

    public void setWorkflow_uploader(String workflow_uploader) {
        this.workflow_uploader = workflow_uploader;
    }

    public String getWorkflow_Type() {
        return workflow_Type;
    }

    public void setWorkflow_Type(String workflow_Type) {
        this.workflow_Type = workflow_Type;
    }

    public String getWorkflow_preview() {
        return workflow_preview;
    }

    public void setWorkflow_preview(String workflow_preview) {
        this.workflow_preview = workflow_preview;
    }

    public String getWorkflow_thumb_big() {
        return workflow_thumb_big;
    }

    public void setWorkflow_thumb_big(String workflow_thumb_big) {
        this.workflow_thumb_big = workflow_thumb_big;
    }

    public String getWorkflow_licence_type() {
        return workflow_licence_type;
    }

    public void setWorkflow_licence_type(String workflow_licence_type) {
        this.workflow_licence_type = workflow_licence_type;
    }

    public String getWorkflow_content_type() {
        return workflow_content_type;
    }

    public void setWorkflow_content_type(String workflow_content_type) {
        this.workflow_content_type = workflow_content_type;
    }

    public List<String> getWorkflow_tags() {
        return workflow_tags;
    }

    public void setWorkflow_tags(List<String> workflow_tags) {
        this.workflow_tags = workflow_tags;
    }

    public String getWorkflow_versions() {
        return workflow_versions;
    }

    public void setWorkflow_versions(String workflow_versions) {
        this.workflow_versions = workflow_versions;
    }

    public List<String> getWorkflow_credits() {
        return workflow_credits;
    }

    public void setWorkflow_credits(List<String> workflow_credits) {
        this.workflow_credits = workflow_credits;
    }

    @Override
    public String toString() {
        return this.workflow_title;
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
