package org.apache.taverna.mobile.ui.workflowrun;


import org.apache.taverna.mobile.ui.base.MvpView;

public interface WorkflowRunMvpView extends MvpView {

    void movetoUploadWorkflow();

    void movetoInputs();

    void setInputsAttribute(int id);
}
