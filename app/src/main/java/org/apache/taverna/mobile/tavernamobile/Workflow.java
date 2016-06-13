package org.apache.taverna.mobile.tavernamobile;
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

import org.apache.taverna.mobile.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
    private String workflow_remote_url; //provides a link to download the workflow. Equivalent to
    // content-uri in the xml form
    private String workflow_web_url; //a string containing the workflow resource that can be
    // loaded in  browser
    private String workflow_details_url; //used to refer to the details of the workflow
    private List<Runs> workflow_runs;
    private int workflow_input;
    private long id;
    private String workflow_uploader; //indicate the user who uploaded the workflow
    private String workflow_Type; //describes whether it is a type 1 or 2 workflow
    private String workflow_preview; //a url to a preview image of the workflow
    private String workflow_thumb_big; //a url to a full scale image of the workflow. I Will
    // usually an SVG because the it is available for most of the workflows
    private String workflow_licence_type; //describes a type of licensing for the workflow
    private String workflow_content_type; //specifies a content type for the workflow;
    private List<String> workflow_tags; //provides a list of string tags that could be used to
    // index the workflow for searches
    private String workflow_versions; //a list of version for the workflow uploaded over time
    private List<String> workflow_credits; //key contributors to the workflow

    public Workflow() {
    }

    ;

    public Workflow(Context context) {
        this.context = context;
        this.workflow_runs = new ArrayList<Runs>();
    }

    public Workflow(String author, String description, long id, String url) {
        this.workflow_author = author;
        this.workflow_author_bitmap = null; //BitmapFactory.decodeResource(getResources(), R
        // .drawable.ic_userprofile);
        this.workflow_description = description;
        this.workflow_input = 1;
        this.id = id;
        this.workflow_remote_url = url;
        this.workflow_runs = new ArrayList<Runs>();
    }

    public Workflow(Context ctx, String title, String author, String description, long id, String
            url) {
        this.context = ctx;
        this.workflow_author = author;
        this.workflow_author_bitmap = BitmapFactory.decodeResource(this.context.getResources(), R
                .drawable.ic_userprofile);
        this.workflow_description = description;
        this.workflow_title = title;
        this.workflow_input = 1;
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

    public String getWorkflowDatecreated() {
        return this.workflow_datecreated;
    }

    public void setWorkflowDatecreated(String workflow_datecreated) {
        this.workflow_datecreated = workflow_datecreated;
    }

    public String getWorkflowRemoteUrl() {
        return this.workflow_remote_url;
    }

    public void setWorkflowRemoteUrl(String workflow_remote_url) {
        this.workflow_remote_url = workflow_remote_url;
    }

    public String getWorkflowDetailsUrl() {
        return this.workflow_details_url;
    }

    public void setWorkflowDetailsUrl(String workflow_details_url) {
        this.workflow_details_url = workflow_details_url;
    }

    public User getUploader() {
        return this.uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
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

    public String getWorkflowDatemodified() {
        return this.workflow_datemodified;
    }

    public void setWorkflowDatemodified(String workflow_datemodified) {
        this.workflow_datemodified = workflow_datemodified;
    }

    public List<Runs> getWorkflowRuns() {
        return this.workflow_runs;
    }

    public void setWorkflowRuns(List<Runs> workflow_runs) {
        this.workflow_runs = workflow_runs;
    }

    public void addWorkflowRun(Runs runs) { //adds a run to this workflow
        this.workflow_runs.add(runs);
    }

    public int getWorkflowInput() {
        return this.workflow_input;
    }

    public void setWorkflowInput(int workflow_input) {
        this.workflow_input = workflow_input;
    }

    public WorkflowInputType getInputType() {
        return WorkflowInputType.TYPE_INT;
    }

    public String getWorkflowAuthor() {
        return this.workflow_author;
    }

    public void setWorkflowAuthor(String workflow_author) {
        this.workflow_author = workflow_author;
    }

    public String getWorkflowDescription() {
        return this.workflow_description;
    }

    public void setWorkflowDescription(String workflow_description) {
        this.workflow_description = workflow_description;
    }

    public String getWorkflowTitle() {
        return this.workflow_title;
    }

    public void setWorkflowTitle(String workflow_title) {
        this.workflow_title = workflow_title;
    }

    public Bitmap getWorkflowAuthorBitmap() {
        return this.workflow_author_bitmap;
    }

    public void setWorkflowAuthorBitmap(Bitmap workflow_author_bitmap) {
        this.workflow_author_bitmap = workflow_author_bitmap;
    }

    public WorkflowComponent getWorkflowComponent() {
        return this.workflowComponent;
    }

    public void setWorkflowComponent(WorkflowComponent workflowComponent) {
        this.workflowComponent = workflowComponent;
    }

    public String getWorkflowWebUrl() {
        return workflow_web_url;
    }

    public void setWorkflowWebUrl(String workflow_web_url) {
        this.workflow_web_url = workflow_web_url;
    }

    public String getWorkflowUploader() {
        return workflow_uploader;
    }

    public void setWorkflowUploader(String workflow_uploader) {
        this.workflow_uploader = workflow_uploader;
    }

    public String getWorkflowType() {
        return workflow_Type;
    }

    public void setWorkflowType(String workflow_Type) {
        this.workflow_Type = workflow_Type;
    }

    public String getWorkflowPreview() {
        return workflow_preview;
    }

    public void setWorkflowPreview(String workflow_preview) {
        this.workflow_preview = workflow_preview;
    }

    public String getWorkflowThumbBig() {
        return workflow_thumb_big;
    }

    public void setWorkflowThumbBig(String workflow_thumb_big) {
        this.workflow_thumb_big = workflow_thumb_big;
    }

    public String getWorkflowLicenceType() {
        return workflow_licence_type;
    }

    public void setWorkflowLicenceType(String workflow_licence_type) {
        this.workflow_licence_type = workflow_licence_type;
    }

    public String getWorkflowContentType() {
        return workflow_content_type;
    }

    public void setWorkflowContentType(String workflow_content_type) {
        this.workflow_content_type = workflow_content_type;
    }

    public List<String> getWorkflowTags() {
        return workflow_tags;
    }

    public void setWorkflowTags(List<String> workflow_tags) {
        this.workflow_tags = workflow_tags;
    }

    public String getWorkflowVersions() {
        return workflow_versions;
    }

    public void setWorkflowVersions(String workflow_versions) {
        this.workflow_versions = workflow_versions;
    }

    public List<String> getWorkflowCredits() {
        return workflow_credits;
    }

    public void setWorkflowCredits(List<String> workflow_credits) {
        this.workflow_credits = workflow_credits;
    }

    @Override
    public String toString() {
        return this.workflow_title;
    }

    public static enum WorkflowInputType { TYPE_INT, TYPE_STRING, TYPE_OBJECT }
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

// this is used to regenerate your object. All Parcelables must have a CREATOR that implements
these two methods
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
