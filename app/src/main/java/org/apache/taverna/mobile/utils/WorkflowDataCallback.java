package org.apache.taverna.mobile.utils;

import org.apache.taverna.mobile.tavernamobile.Workflow;

import java.util.List;

/**
 * Callback for when data is ready to be put into the workflow adapter
 * Created by root on 6/24/15.
 */
public interface WorkflowDataCallback {

    public void onWorkflowDataReady(List<Workflow> data);
}
