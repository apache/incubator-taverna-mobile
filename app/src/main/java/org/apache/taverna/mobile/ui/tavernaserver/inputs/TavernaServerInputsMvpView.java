package org.apache.taverna.mobile.ui.tavernaserver.inputs;

import org.apache.taverna.mobile.data.model.Inputs;
import org.apache.taverna.mobile.ui.base.MvpView;

import java.util.List;

/**
 * Created by ian on 16/12/16.
 */

public interface TavernaServerInputsMvpView extends MvpView {


    void showError(int stringID);

    void showCredentialError();

    void setInputs(Inputs inputs);
}
