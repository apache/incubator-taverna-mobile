package org.apache.taverna.mobile.data.remote;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * @author Sagar
 */
public class BaseApiManager {


    final String ENDPOINT = "http://www.myexperiment.org/";
    public TavernaService mTavernaService;

    public BaseApiManager() {

        mTavernaService = createApi(TavernaService.class, ENDPOINT);
    }

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
        return mTavernaService;
    }
}