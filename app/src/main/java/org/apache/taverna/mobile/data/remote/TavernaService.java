/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.taverna.mobile.data.remote;

import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.data.model.DetailAnnouncement;
import org.apache.taverna.mobile.data.model.Workflows;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface TavernaService {

    @GET("/announcements.xml")
    Observable<Announcements> getAllAnnouncements(@Query("page") int pageNumber);

    @GET("/announcement.xml")
    Observable<DetailAnnouncement> getAnnouncement(@Query("id") String id);

    @GET("/workflows.xml")
    Observable<Workflows> getAllWorkflows(@Query("page") int pageNumber);
}