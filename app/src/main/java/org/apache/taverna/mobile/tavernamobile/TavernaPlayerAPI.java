package org.apache.taverna.mobile.tavernamobile;
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
import android.content.Context;
import android.preference.PreferenceManager;

import java.net.PasswordAuthentication;

/**
 * Created by Larry Akah on 6/13/15.
 */
public class TavernaPlayerAPI {

    public static String PLAYER_BASE_URL = "http://heater.cs.man.ac.uk:3000/";
    public static String SERVER_BASE_URL = "http://heater.cs.man.ac.uk:8090/taverna-2.5.4/";
    public static String PLAYER_WORKFLOW_URL = PLAYER_BASE_URL+"workflows/";
    public static String PLAYER_RUN_URL = PLAYER_BASE_URL+"runs/";
    public static String PLAYER_RUN_FRAMEWORK_URL = PLAYER_RUN_URL+"new?workflow_id="; //returns a json 'framework' used for creating runs for the given workflow

    public TavernaPlayerAPI(Context context) {
        String server = PreferenceManager.getDefaultSharedPreferences(context).getString("pref_server_url","/");
        String player = PreferenceManager.getDefaultSharedPreferences(context).getString("pref_player_url","/");
        String user = PreferenceManager.getDefaultSharedPreferences(context).getString("pref_player_url","/");
        String password = PreferenceManager.getDefaultSharedPreferences(context).getString("pref_player_url","/");
        PLAYER_BASE_URL = player;
        SERVER_BASE_URL = server;
        PLAYER_WORKFLOW_URL = PLAYER_BASE_URL+"workflows/";
        PLAYER_RUN_URL = PLAYER_BASE_URL+"runs/";
        PLAYER_RUN_FRAMEWORK_URL = PLAYER_RUN_URL+"new?workflow_id=";
    }

    public TavernaPlayerAPI(){

    }

    public static String getPLAYER_BASE_URL() {
        return PLAYER_BASE_URL;
    }

    public static String getSERVER_BASE_URL() {
        return SERVER_BASE_URL;
    }

    public static String getPLAYER_WORKFLOW_URL() {
        return PLAYER_WORKFLOW_URL;
    }

    public static String getPLAYER_RUN_URL(Context ctx) {
        return PLAYER_RUN_URL;
    }

    public static String getPLAYER_RUN_FRAMEWORK_URL(Context ctx) {
        return PLAYER_RUN_FRAMEWORK_URL;
    }

    public String getPlayerUserName(Context c){
        return PreferenceManager.getDefaultSharedPreferences(c).getString("pref_user","default");
    }

    public String getPlayerUserPassword(Context c){
        return PreferenceManager.getDefaultSharedPreferences(c).getString("pref_password","default");
    }

    public static class Authenticator extends java.net.Authenticator{
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
