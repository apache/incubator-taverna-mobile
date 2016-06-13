package org.apache.taverna.mobile.data.remote;

import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.data.model.DetailAnnouncement;

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
    Observable<DetailAnnouncement> getAnnouncement(@Query("id") String id);
}