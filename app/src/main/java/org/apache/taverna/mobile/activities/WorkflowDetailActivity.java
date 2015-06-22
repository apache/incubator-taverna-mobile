package org.apache.taverna.mobile.activities;

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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.fragments.workflowdetails.WorkflowAboutFragment;
import org.apache.taverna.mobile.fragments.workflowdetails.WorkflowLicenceFragment;
import org.apache.taverna.mobile.fragments.workflowdetails.WorkflowRunHistoryFragment;
import org.apache.taverna.mobile.fragments.workflowdetails.WorkflowdetailFragment;

import java.util.Locale;

public class WorkflowDetailActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow_detail);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        if(item.getItemId() == android.R.id.home){
            finish();
            this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right );
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        finish();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right );
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position +1){
                case 1:
                return WorkflowdetailFragment.newInstance(position + 1);
                case 2:
                   return WorkflowRunHistoryFragment.newInstance("workflow",WorkflowdetailFragment.WORKFLO_ID);
                case 3:
                    return WorkflowLicenceFragment.newInstance("","");
                case 4:
                    return WorkflowAboutFragment.newInstance("","");
            }
            return WorkflowdetailFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.detail_title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.detail_title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.detail_title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.detail_title_section4).toUpperCase(l);
            }
            return "";
        }


    }

}
