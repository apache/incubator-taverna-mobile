package org.apache.taverna.mobile.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.taverna.mobile.tavernamobile.User;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.CookieStore;
import java.net.HttpURLConnection;
/**
 * Apache Taverna Mobile
 * Copyright 2015 The Apache Software Foundation

 * This product includes software developed at
 * The Apache Software Foundation (http://www.apache.org/).

 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Created by Larry Akah on 6/18/15.
 */
public class HttpUtil {

    public <T> Object doPostAuthenticate(){
        User muser = new User();

        return muser;
    }

    public <T> Object doGetRequestResponse(String uri, Class<T> classType, String username, String password){
        Object dataObject = null;

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri);
        HttpResponse httpResponse = null;
        try {
            ((AbstractHttpClient) httpClient).setCookieStore((org.apache.http.client.CookieStore) CookieStore.class.newInstance());
            httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username,password), "UTF-8", false));
            httpResponse = httpClient.execute(httpGet);

            if(isSuccess(httpResponse, HttpURLConnection.HTTP_OK)){
                HttpEntity entity = httpResponse.getEntity();
                if(entity != null){
                    String responseString = EntityUtils.toString(entity);
                    dataObject = this.deSerialize(classType, responseString);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return dataObject;

    }

    //de-serializes xml data to target class
    private <T> Object deSerialize(Class<T> classType, String responseString) {
        //TODO deserialize data and return the appropriate object
       // ObjectInputStream ori = new ObjectInputStream(new DataInputStream(responseString)) ;

        return null;
    }

    private boolean isSuccess(HttpResponse httpResponse, int httpOk) {

        return httpResponse.getStatusLine().getStatusCode() == httpOk;
    }
}
