package org.apache.taverna.mobile.ui.login;


import org.apache.taverna.mobile.ui.base.MvpView;

public interface LoginMvpView  extends MvpView {

    void moveToWorkflowList();

    void showCredentialError();

    void showError(String string);

    void showProgressDialog(boolean flag);
}
