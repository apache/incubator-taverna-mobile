package org.apache.taverna.mobile.ui.playerlogin;

import org.apache.taverna.mobile.ui.base.MvpView;


public interface PlayerLoginMvpView extends MvpView{

    void showError(String string);

    void showCredentialError();

    void validCredential();
}
