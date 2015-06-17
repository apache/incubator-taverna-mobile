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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.fragments.FavoriteFragment;
import org.apache.taverna.mobile.fragments.NavigationDrawerFragment;
import org.apache.taverna.mobile.fragments.WorkflowItemFragment;

import java.io.File;

public class DashboardMainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, FavoriteFragment.FavoriteItemSelected {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    static final int NUM_ITEMS = 2;
    private final int SELECT_WORKFLOW = 10;
    public static final String APP_DIRECTORY_NAME = "TavernaMobile";

    MyAdapter mAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main);
        setUpWorkflowDirectory(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        //manage tabs and swipe
        mAdapter = new MyAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

      /*  //Handle search actions from a system sent intent
        Intent searchIntent = getIntent();
        if(searchIntent != null && Intent.ACTION_SEARCH.equals(searchIntent.getAction())){
            //retrieve and process query then display results
            String query = searchIntent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this,"Query = "+query, Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position+1){
            case 1://return home
                fragmentManager.beginTransaction()
                        .replace(R.id.container, WorkflowItemFragment.newInstance("param1", "param2"))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
                try {
                    mPager.setCurrentItem(0);
                }catch (NullPointerException np){
                    np.printStackTrace();
                }
                break;
            case 2: //open workflow
                Intent workflowSelectIntent = new Intent(Intent.ACTION_GET_CONTENT)
                        .setDataAndTypeAndNormalize(Uri.parse(String.format("%s%s%s",
                                        Environment.getExternalStorageDirectory(),
                                        File.separator, APP_DIRECTORY_NAME)),
                                "application/vnd.taverna.t2flow+xml");

                Intent loadWorkflowIntent = Intent.createChooser(workflowSelectIntent,
                        "Choose Workflow (.t2flow)");
                startActivityForResult(loadWorkflowIntent, SELECT_WORKFLOW);

                break;
            case 3: //show usage
                fragmentManager.beginTransaction()
                        .replace(R.id.container, FavoriteFragment.newInstance(position + 1))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;
            case 4: //show about
                fragmentManager.beginTransaction()
                        .replace(R.id.container, FavoriteFragment.newInstance(position + 1))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;
            case 5://open settings/preference activity
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                break;
            case 6: //logout user
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode , int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_WORKFLOW){
                String workflowPath = data.getData().getPath();
                Toast.makeText(getBaseContext(), "Path: "+workflowPath, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_activity_dashboard_main);
                break;
            case 2:
                mTitle = getString(R.string.title_openworkflow);
                break;
            case 3:
                mTitle = getString(R.string.title_usage);
                break;
            case 4:
                mTitle = getString(R.string.title_about);
                break;
            case 5:
                mTitle = getString(R.string.title_activity_settings);
                break;
            case 6:
                mTitle = getString(R.string.title_exit);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    private void setUpWorkflowDirectory(Context context){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File workflowDirectory = new File(Environment.getExternalStorageDirectory()+File.separator+APP_DIRECTORY_NAME);
            if (!workflowDirectory.exists()) {
                boolean state = workflowDirectory.mkdirs();
                if (state) {
                    Toast.makeText(context, "Storage Ready", Toast.LENGTH_SHORT).show();
                    sp.edit().putString(APP_DIRECTORY_NAME, workflowDirectory.getAbsolutePath()).commit();
                    Toast.makeText(context, "Home dir: "+workflowDirectory.getAbsolutePath(), Toast.LENGTH_LONG).show();
                } else { //directory can't be created either because of restricted access or lack of an external storage media.
                    //we assume the lack of secondary storage so we have to switch to internal storage
                    //   File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.))
            //        Toast.makeText(context, "Storage Error. Directory not created", Toast.LENGTH_SHORT).show();
                }
//            workflowDirectory.list();
            }else {
          //      Toast.makeText(context, "Directory exists. Home dir: "+workflowDirectory.getAbsolutePath(), Toast.LENGTH_LONG).show();
                sp.edit().putString(APP_DIRECTORY_NAME, workflowDirectory.getAbsolutePath()).commit();
            /*else {
                File mainDir = new File(Environment.getExternalStorageDirectory() + File.separator + APP_DIRECTORY_NAME);
                if (mainDir.mkdirs())
                    sp.edit().putString(APP_DIRECTORY_NAME, mainDir.getAbsolutePath()).commit();
                else
                    Toast.makeText(context, "Workflow home not created. Permission issues", Toast.LENGTH_SHORT).show();
            }*/
            }
        }else{//use internal memory to save the data
            File home = context.getDir("Workflows", Context.MODE_PRIVATE);
            sp.edit().putString(APP_DIRECTORY_NAME, home.getAbsolutePath()).commit();
       //     Toast.makeText(context, "Home dir: "+home.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            MenuInflater mi = getMenuInflater();
                mi.inflate(R.menu.dashboard_main, menu);
          /*  //get the searchview and set the searchable configuration
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            //assuming this activity is the searchable activity
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setSubmitButtonEnabled(true);
//            searchView.setIconifiedByDefault(false);*/

            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFavoriteItemSelected(int position) {
        //trigger when a favorite item is selected.
        startActivity(new Intent(this, WorkflowDetailActivity.class));
    }

    public class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
        @Override
        public String getPageTitle(int position){
            switch(position+1){
                case 2:
                    return (DashboardMainActivity.this).getResources().getString(R.string.title_favorite);

                case 1:
                    return (DashboardMainActivity.this).getResources().getString(R.string.title_explore);
            }
            return "";
        }

        @Override
        public Fragment getItem(int position) {
            switch(position+1){
                case 1:
                    return WorkflowItemFragment.newInstance("Workflows","Running ...");
                case 2:
                    return FavoriteFragment.newInstance(position);
            }
            return WorkflowItemFragment.newInstance("","");
        }
    }
}
