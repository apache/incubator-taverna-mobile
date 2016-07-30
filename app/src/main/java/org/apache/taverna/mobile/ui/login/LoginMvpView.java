package org.apache.taverna.mobile.ui.login;


import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.ui.base.MvpView;

public interface LoginMvpView  extends MvpView {

    void moveToWorkflowList();

    void showCredentialError();

    void saveUser(User user);

    void showError(String string);
}
