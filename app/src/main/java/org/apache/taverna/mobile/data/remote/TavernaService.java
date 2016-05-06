package org.apache.taverna.mobile.data.remote;

import android.database.Observable;

import org.apache.taverna.mobile.data.model.Announcements;

import retrofit2.http.GET;

public interface TavernaService {

    @GET("/")
    Observable<Announcements> getAllAnnouncements();

}