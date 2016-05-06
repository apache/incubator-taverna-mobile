package org.apache.taverna.mobile.data.remote;

import org.apache.taverna.mobile.data.model.Announcements;

import retrofit2.http.GET;
import rx.Observable;

public interface TavernaService {

    @GET("/announcements.xml")
    Observable<Announcements> getAllAnnouncements();

}