package org.apache.taverna.mobile.data;

import org.apache.taverna.mobile.data.model.DetailAnnouncement;
import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.data.remote.BaseApiManager;

import rx.Observable;

/**
 * Created by Sagar
 */
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
    public Observable<DetailAnnouncement> getAnnouncementDetail(String id){
        return mBaseApiManager.getTavernaApi().getAnnouncement(id);
    }
}