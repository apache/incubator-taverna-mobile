package org.apache.taverna.mobile.tavernamobile;

import android.content.Context;
import android.preference.PreferenceManager;

import java.net.PasswordAuthentication;

/**
 * Created by root on 6/13/15.
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
        PLAYER_BASE_URL = player;
        SERVER_BASE_URL = server;
        PLAYER_WORKFLOW_URL = PLAYER_BASE_URL+"workflows/";
        PLAYER_RUN_URL = PLAYER_BASE_URL+"runs/";
        PLAYER_RUN_FRAMEWORK_URL = PLAYER_RUN_URL+"new?workflow_id=";
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

    public static String getPLAYER_RUN_URL() {
        return PLAYER_RUN_URL;
    }

    public static String getPLAYER_RUN_FRAMEWORK_URL() {
        return PLAYER_RUN_FRAMEWORK_URL;
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
