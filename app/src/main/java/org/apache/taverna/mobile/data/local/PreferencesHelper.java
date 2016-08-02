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
package org.apache.taverna.mobile.data.local;


import android.content.Context;
import android.content.SharedPreferences;

import org.apache.taverna.mobile.data.model.User;

import rx.Observable;
import rx.functions.Func0;

public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "taverna_app_pref_file";

    private static final String PREF_KEY_LOGGED_IN = "pref_logged_in";

    private static final String PREF_KEY_USER_ID = "pref_user_id";

    private static final String PREF_KEY_USER_NAME = "pref_user_name";

    private static final String PREF_KEY_USER_DESCRIPTION = "pref_user_description";

    private static final String PREF_KEY_USER_EMAIL = "pref_user_email";

    private static final String PREF_KEY_USER_AVATAR = "pref_user_avatar";

    private static final String PREF_KEY_USER_CITY = "pref_user_city";

    private static final String PREF_KEY_USER_COUNTRY = "pref_user_country";

    private static final String PREF_KEY_USER_WEBSITE = "pref_user_website";

    private final SharedPreferences mPref;


    public PreferencesHelper(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }


    public void clear() {
        mPref.edit().clear().apply();
    }

    public boolean getLoggedInFlag() {
        return mPref.getBoolean(PREF_KEY_LOGGED_IN, false);
    }

    public void setLoggedInFlag(Boolean flag) {
        mPref.edit().putBoolean(PREF_KEY_LOGGED_IN, flag).apply();
    }

    public String getUserWebsite() {
        return mPref.getString(PREF_KEY_USER_WEBSITE, null);
    }

    private void setUserWebsite(String website) {
        mPref.edit().putString(PREF_KEY_USER_WEBSITE, website).apply();
    }

    public String getUserCountry() {
        return mPref.getString(PREF_KEY_USER_COUNTRY, null);
    }

    private void setUserCountry(String country) {
        mPref.edit().putString(PREF_KEY_USER_COUNTRY, country).apply();
    }

    public String getUserCity() {
        return mPref.getString(PREF_KEY_USER_CITY, null);
    }

    private void setUserCity(String city) {
        mPref.edit().putString(PREF_KEY_USER_CITY, city).apply();
    }

    public String getUserID() {
        return mPref.getString(PREF_KEY_USER_ID, null);
    }

    private void setUserID(String userID) {
        mPref.edit().putString(PREF_KEY_USER_ID, userID).apply();
    }

    public String getUserName() {
        return mPref.getString(PREF_KEY_USER_NAME, null);
    }

    private void setUserName(String userName) {
        mPref.edit().putString(PREF_KEY_USER_NAME, userName).apply();

    }

    public String getUserDescription() {
        return mPref.getString(PREF_KEY_USER_DESCRIPTION, null);
    }

    private void setUserDescription(String userDescription) {
        mPref.edit().putString(PREF_KEY_USER_DESCRIPTION, userDescription).apply();
    }

    public String getUserEmail() {
        return mPref.getString(PREF_KEY_USER_EMAIL, null);
    }

    private void setUserEmail(String userEmail) {
        mPref.edit().putString(PREF_KEY_USER_EMAIL, userEmail).apply();
    }

    public String getUserAvatar() {
        return mPref.getString(PREF_KEY_USER_AVATAR, null);
    }

    private void setUserAvatar(String userAvatar) {
        mPref.edit().putString(PREF_KEY_USER_AVATAR, userAvatar).apply();
    }

    public Observable<User> saveUserDetail(final User user) {
        return Observable.defer(new Func0<Observable<User>>() {
            @Override
            public Observable<User> call() {
                if (user.getElementId() != null) {
                    setUserID(user.getElementId());
                }
                if (user.getName() != null) {
                    setUserName(user.getName());
                }
                if (user.getDescription() != null) {
                    setUserDescription(user.getDescription());
                }
                if (user.getEmail() != null) {
                    setUserEmail(user.getEmail());
                }
                if (user.getAvatar().getResource() != null) {
                    setUserAvatar(user.getAvatar().getResource());
                }
                if (user.getCity() != null) {
                    setUserCity(user.getCity());
                }
                if (user.getCountry() != null) {
                    setUserCountry(user.getCountry());
                }
                if (user.getWebsite() != null) {
                    setUserWebsite(user.getWebsite());
                }

                return Observable.just(user);
            }
        });

    }

}
