package org.apache.taverna.mobile.activities;

/*
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

import org.apache.taverna.mobile.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class FlashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);
        //getSupportActionBar().hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        final Context context = this;
        //setup initial app settings
        if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_set", false)) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString
                    ("pref_server_url", "http://heater.cs.man.ac.uk:8090/taverna-2.5.4/").apply();
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString
                    ("pref_player_url", "http://heater.cs.man.ac.uk:3000/").apply();
        } else {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("pref_set",
                    true).apply();
        }
        Handler mhandler = new Handler();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean
                        ("pref_logged_in", false)) {
                    startActivity(new Intent(FlashScreenActivity.this, LoginActivity.class));
                    (FlashScreenActivity.this).finish();
                } else {
                    startActivity(new Intent(FlashScreenActivity.this, DashboardMainActivity
                            .class));
                    (FlashScreenActivity.this).finish();
                }
            }
        }, 2500);

    }
}
