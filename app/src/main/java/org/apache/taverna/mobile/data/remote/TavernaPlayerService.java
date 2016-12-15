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


import org.apache.taverna.mobile.data.model.PlayerWorkflow;
import org.apache.taverna.mobile.data.model.PlayerWorkflowDetail;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscription;

public interface TavernaPlayerService {

    @POST("rest/runs")
    @Headers({
            "Content-type: application/vnd.taverna.t2flow+xml"
            })
    Observable<Response<ResponseBody>> uploadWorkflow(@Body RequestBody body, @Header("Authorization")
            String authorization);

    @PUT("rest/runs/{uuid}")
    Observable<Response<ResponseBody>> startWorkflowRun(@Body RequestBody body, @Header("Authorization")
            String authorization);

    @GET("rest/runs/{uuid}/input/baclava")
    Observable<Response<ResponseBody>> getInputs(@Header("Authorization")
            String authorization, @Path("uuid") String runLocationID);

    @POST("/users/sign_in")
    @Headers({APIEndPoint.XML_ACCEPT_HEADER})
    Observable<ResponseBody> playerlogin(@Header("Authorization") String
                                                 authorization);


    @GET("/runs/new")
    @Headers({
            APIEndPoint.JSON_CONTENT_HEADER,
            APIEndPoint.JSON_ACCEPT_HEADER})
    Observable<PlayerWorkflowDetail> getWorkflowDetail(@Query("workflow_id") int id);

}
