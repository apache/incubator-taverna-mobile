package org.apache.taverna.mobile.tavernamobile;
/**
 * Apache Taverna Mobile
 * Copyright 2015 The Apache Software Foundation
 *
 * This product includes software developed at
 * The Apache Software Foundation (http://www.apache.org/).
 *
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

import android.content.Context;
import android.preference.PreferenceManager;

import java.net.PasswordAuthentication;

/**
 * Created by Larry Akah on 6/13/15.
 */
public class TavernaPlayerAPI {

    public static String mPlayerBaseUrl = "http://heater.cs.man.ac.uk:3000/";
    public static String mServerBaseUrl = "http://heater.cs.man.ac.uk:8090/taverna-2.5.4/";
    public static String mPlayerWorkFlowUrl = mPlayerBaseUrl + "workflows/";
    public static String mPlayerRunUrl = mPlayerBaseUrl + "runs/";
    public static String mPlayerRunFrameworkUrl = mPlayerRunUrl + "new?workflow_id=";
    //returns a json 'framework' used for creating runs for the given workflow

    public TavernaPlayerAPI(Context context) {
        String server = PreferenceManager.getDefaultSharedPreferences(context).getString
                ("pref_server_url", "/");
        String player = PreferenceManager.getDefaultSharedPreferences(context).getString
                ("pref_player_url", "/");
        String user = PreferenceManager.getDefaultSharedPreferences(context).getString
                ("pref_player_url", "/");
        String password = PreferenceManager.getDefaultSharedPreferences(context).getString
                ("pref_player_url", "/");
        mPlayerBaseUrl = player;
        mServerBaseUrl = server;
        mPlayerWorkFlowUrl = mPlayerBaseUrl + "workflows/";
        mPlayerRunUrl = mPlayerBaseUrl + "runs/";
        mPlayerRunFrameworkUrl = mPlayerRunUrl + "new?workflow_id=";
    }

    public TavernaPlayerAPI() {

    }

    public static String getplayerBaseUrl() {
        return mPlayerBaseUrl;
    }

    public static String getserverBaseUrl() {
        return mServerBaseUrl;
    }

    public static String getplayerWorkflowUrl() {
        return mPlayerWorkFlowUrl;
    }

    public static String getplayerRunUrl(Context ctx) {
        return mPlayerRunUrl;
    }

    public static String getplayerRunFrameworkUrl(Context ctx) {
        return mPlayerRunFrameworkUrl;
    }

    public String getPlayerUserName(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString("pref_user", "default");
    }

    public String getPlayerUserPassword(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString("pref_password",
                "default");
    }

    public static class Authenticator extends java.net.Authenticator {
        private String username, password;

        public Authenticator(String username, String password) {
            super();
            this.username = username;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(this.username, this.password.toCharArray());
        }
    }
}
