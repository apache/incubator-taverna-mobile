package org.apache.taverna.mobile.data.remote;

import org.apache.taverna.mobile.data.model.Announcement;
import org.apache.taverna.mobile.data.model.Announcements;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Sagar
 */

public interface TavernaService {

    @GET("/announcements.xml")
    Observable<Announcements> getAllAnnouncements(@Query("page") int pageNumber);

    @GET("/announcement.xml")
    Observable<Announcement> getAnnouncement(@Query("id") int id);
}