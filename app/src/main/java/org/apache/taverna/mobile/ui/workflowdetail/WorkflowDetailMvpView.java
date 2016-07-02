package org.apache.taverna.mobile.ui.workflowdetail;


import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.data.model.License;
import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.ui.base.MvpView;

public interface WorkflowDetailMvpView extends MvpView {

    void showProgressbar(boolean b);

    void showWorkflowDetail(Workflow workflow);

    void setImage(User user);

    void showErrorSnackBar(String error);

    void showLicense(License license);

    void showLicenseProgress(boolean b);
}
