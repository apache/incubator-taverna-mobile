package org.apache.taverna.mobile.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import org.apache.taverna.mobile.R;

/**
 * Created by root on 6/8/15.
 */
public class Workflow {
    private Context context;
    private String workflow_author;
    private String workflow_title;
    private String workflow_description;
    private Bitmap workflow_author_bitmap;
    private WorkflowComponent workflowComponent;
    private int workflow_input;
    private long id;

    public long getid() {
        return id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static enum workflow_input_type{ TYPE_INT, TYPE_STRING, TYPE_OBJECT};

    public Workflow(Context ctx, WorkflowComponent component){
        this.context = ctx;
        this.workflowComponent = component;
        this.workflow_author = "Larry Akah";
        this.workflow_author_bitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ic_userprofile);
        this.workflow_description =" Prokaryotic behaviour in carnivorous plants";
        this.workflow_title ="Carnivorous synthesis";
        this.workflow_input=1;

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
