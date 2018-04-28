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
import org.apache.taverna.mobile.data.model.License;
import org.apache.taverna.mobile.data.model.Search;
import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.data.model.Workflows;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface TavernaService {

    @GET(APIEndPoint.ALL_ANNOUNCEMENT)
    Observable<Announcements> getAllAnnouncements(@Query("page") int pageNumber);

    @GET(APIEndPoint.ANNOUNCEMENT)
    Observable<DetailAnnouncement> getAnnouncement(@Query("id") String id);

    @GET(APIEndPoint.ALL_WORKFLOW)
    Observable<Workflows> getAllWorkflows(@QueryMap Map<String, String> options);

    @GET(APIEndPoint.WORKFLOW)
    Observable<Workflow> getDetailWorkflow(@Query("id") String id
            , @QueryMap Map<String, String> options);

    @GET(APIEndPoint.USER)
    Observable<User> getUserDetail(@Query("id") String id
            , @QueryMap Map<String, String> options);

    @GET(APIEndPoint.LICENSE)
    Observable<License> getLicenseDetail(@Query("id") String id
            , @QueryMap Map<String, String> options);

    @GET(APIEndPoint.WHOAMI)
    Observable<User> getLoginUserDetail(@Header("Authorization") String credentials);

    @GET
    @Headers(APIEndPoint.XML_ACCEPT_HEADER)
    Observable<ResponseBody> downloadWorkflowContent(@Url String workflowContentUrl);

    @GET(APIEndPoint.MY_WORKFLOWS)
    Observable<Workflows> getMyWorkflows(@Query("id") String id,
                                         @QueryMap Map<String, String> options);

    @GET(APIEndPoint.SEARCH)
    Observable<Search> getSearchWorkflowResult(@QueryMap Map<String, String> options);
}