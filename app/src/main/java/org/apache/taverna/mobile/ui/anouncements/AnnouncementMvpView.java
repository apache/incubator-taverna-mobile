package org.apache.taverna.mobile.ui.anouncements;

import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.ui.base.MvpView;

public interface AnnouncementMvpView extends MvpView {

    void showAllAnouncement(Announcements announcements);
    void showProgressbar(boolean b);
}