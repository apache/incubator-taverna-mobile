package org.apache.taverna.mobile.ui.imagezoom;


import org.apache.taverna.mobile.ui.base.MvpView;

import android.content.Context;

public interface ImageZoomMvpView extends MvpView{

    void showErrorSnackBar(String error);

    Context getAppContext();

    void setJPGuri(String uri);

    void setJPGImage();

    void addImageAttacher();
}
