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

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class BaseApiManager {


    public static final String MY_EXPERIMENT_END_POINT = "http://www.myexperiment.org/";

    public static final String TAVERNA_PLAYER_END_POINT = "http://139.59.28.12:3000/";

    /******** Helper class that sets up a new services *******/

    private <T> T createApi(Class<T> clazz, String ENDPOINT) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(new TavernaOkHttpClient().getTavernaOkHttpClient())
                .build();

        return retrofit.create(clazz);
    }

    public TavernaService getTavernaApi() {
        return createApi(TavernaService.class, MY_EXPERIMENT_END_POINT);
    }

    public  TavernaPlayerService getTavernaPlayerApi(){
        return createApi(TavernaPlayerService.class, TAVERNA_PLAYER_END_POINT);
    }
}