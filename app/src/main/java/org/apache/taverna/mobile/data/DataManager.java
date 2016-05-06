package org.apache.taverna.mobile.data;

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
    public Observable<Announcements> getAllAnnouncement(){
        return mBaseApiManager.getTavernaApi().getAllAnnouncements();
    }
}