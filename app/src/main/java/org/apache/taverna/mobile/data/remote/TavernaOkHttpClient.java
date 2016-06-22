package org.apache.taverna.mobile.data.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class TavernaOkHttpClient {


    public OkHttpClient getTavernaOkHttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //Enable Full Body Logging
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        //Interceptor :> Full Body Logger
        builder.addInterceptor(logger);
        return builder.build();

    }
}