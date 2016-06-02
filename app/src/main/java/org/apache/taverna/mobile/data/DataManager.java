package org.apache.taverna.mobile.data;

import org.apache.taverna.mobile.data.model.Announcement;
import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.data.remote.BaseApiManager;

import rx.Observable;


public class DataManager {

    public BaseApiManager mBaseApiManager = new BaseApiManager();

    public DataManager(){
    }

    /**
     *
     * @return List of all Announcement
     */
    public Observable<Announcements> getAllAnnouncement(int pageNumber){
        return mBaseApiManager.getTavernaApi().getAllAnnouncements(pageNumber);
    }

    /**
     *
     * @return Detail of Announcement
     */
    public Observable<Announcement> getAnnouncementDetail(int id){
        return mBaseApiManager.getTavernaApi().getAnnouncement(id);
    }
}