package org.apache.taverna.mobile.activities;

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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TableLayout;
import android.widget.Toast;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.local.PreferencesHelper;
import org.apache.taverna.mobile.ui.anouncements.AnnouncementFragment;
import org.apache.taverna.mobile.ui.favouriteworkflow.FavouriteWorkflowsFragment;
import org.apache.taverna.mobile.ui.licence.LicenceFragment;
import org.apache.taverna.mobile.ui.myworkflows.MyWorkflowFragment;
import org.apache.taverna.mobile.ui.workflow.WorkflowFragment;
import org.apache.taverna.mobile.utils.ActivityUtils;
import org.apache.taverna.mobile.utils.WorkflowOpen;

import java.io.File;

public class DashboardMainActivity extends AppCompatActivity {

    public static final String APP_DIRECTORY_NAME = "TavernaMobile";
    private final int SELECT_WORKFLOW = 10;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle = "Dashboard";
    private Dialog aboutDialog;
    private DrawerLayout mDrawerLayout;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setUpWorkflowDirectory(this);
        aboutDialog = new Dialog(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }


        /**
         * Setting the Fragment in FrameLayout
         */
        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment;

            fragment = new WorkflowFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .commit();
        }
        dataManager = new DataManager(new PreferencesHelper(this));
    }


    /**
     * @param navigationView Design Support NavigationView  OnClick Listener Event
     */
    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {


                        Fragment fragment;

                        switch (menuItem.getItemId()) {
                            case R.id.nav_workflows:

                                fragment = new WorkflowFragment();
                                ActivityUtils
                                        .addFragmentToActivity(
                                                getSupportFragmentManager(),
                                                fragment,
                                                R.id.frame_container);

                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.nav_my_workflows:

                                fragment = new MyWorkflowFragment();
                                ActivityUtils
                                        .addFragmentToActivity(
                                                getSupportFragmentManager(),
                                                fragment,
                                                R.id.frame_container);

                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.nav_favourite_workflow:

                                fragment = new FavouriteWorkflowsFragment();
                                ActivityUtils
                                        .addFragmentToActivity(
                                                getSupportFragmentManager(),
                                                fragment,
                                                R.id.frame_container);
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.nav_announcement:

                                fragment = new AnnouncementFragment();
                                ActivityUtils
                                        .addFragmentToActivity(
                                                getSupportFragmentManager(),
                                                fragment,
                                                R.id.frame_container);

                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.nav_openworkflow:

                                Intent workflowSelectIntent =
                                        new Intent(Intent.ACTION_GET_CONTENT)
                                                .setDataAndTypeAndNormalize(
                                                    Uri.parse(String.format("%s%s%s",
                                                        Environment.getExternalStorageDirectory(),
                                                        File.separator,
                                                        APP_DIRECTORY_NAME)),
                                                    "application/vnd.taverna.t2flow+xml");

                                Intent loadWorkflowIntent = Intent.createChooser
                                        (workflowSelectIntent,
                                                "Choose Workflow (t2flow or xml)");
                                startActivityForResult(loadWorkflowIntent, SELECT_WORKFLOW);
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.nav_usage:

                                aboutDialog.setCanceledOnTouchOutside(true);
                                aboutDialog.setTitle("Usage");
                                aboutDialog.setContentView(R.layout.usage_layout);
                                aboutDialog.show();

                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.nav_about:

                                TableLayout about = (TableLayout) getLayoutInflater().inflate(R
                                        .layout.about, navigationView, false);

                                aboutDialog.setCanceledOnTouchOutside(true);
                                aboutDialog.setTitle("About Taverna Mobile");
                                aboutDialog.setContentView(about);
                                aboutDialog.show();

                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.os_licences:

                                ActivityUtils
                                        .addFragmentToActivity(
                                                getSupportFragmentManager(),
                                                LicenceFragment.newInstance(),
                                                R.id.frame_container);

                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.nav_settings:

                                startActivity(new Intent(getApplicationContext(),
                                        SettingsActivity.class));
                                overridePendingTransition(android.R.anim.slide_in_left, android.R
                                        .anim.slide_out_right);

                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.nav_logout:

                                finish();
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                dataManager.getPreferencesHelper().setLoggedInFlag(false);
                                return true;

                        }
                        return true;
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SELECT_WORKFLOW) {
            String workflowPath = data.getData().getPath();
            //   Toast.makeText(getBaseContext(), "Path: "+workflowPath, Toast.LENGTH_LONG)
            // .show();
            String type = getMimeType(data.getData().getPath());
            if (type.equals("text/xml") || type.equals("application/vnd.taverna.t2flow+xml")) {

                new WorkflowOpen(this).execute(workflowPath);
            } else {
                Toast.makeText(getBaseContext(), "Invalid worklow. Please try again", Toast
                        .LENGTH_LONG).show();

            }
        }
    }

    /**
     * Return the mimetype of the file selected to be run as a workflow
     *
     * @param url the path to the seleted file
     * @return the mimetype of the file selected
     */
    private String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }


    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }

    private void setUpWorkflowDirectory(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File workflowDirectory = new File(Environment.getExternalStorageDirectory() + File
                    .separator + APP_DIRECTORY_NAME);
            if (!workflowDirectory.exists()) {
                boolean state = workflowDirectory.mkdirs();
                if (state) {
                    Toast.makeText(context, "Storage Ready", Toast.LENGTH_SHORT).show();
                    sp.edit().putString(APP_DIRECTORY_NAME, workflowDirectory.getAbsolutePath())
                            .apply();
                    Toast.makeText(context, "Home dir: " + workflowDirectory.getAbsolutePath(),
                            Toast.LENGTH_LONG).show();
                } else { //directory can't be created either because of restricted access or lack
                    // of an external storage media.
                    //we assume the lack of secondary storage so we have to switch to internal
                    // storage
                    //   File dir = new File(Environment.getExternalStoragePublicDirectory
                    // (Environment.))
                    //        Toast.makeText(context, "Storage Error. Directory not created",
                    // Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(
                            mDrawerLayout,
                            "Storage error. No workflows can be saved.",
                            Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });

                    snackbar.show();
                }
//            workflowDirectory.list();
            } else {
                //      Toast.makeText(context, "Directory exists. Home dir: "+workflowDirectory
                // .getAbsolutePath(), Toast.LENGTH_LONG).show();
                sp.edit().putString(APP_DIRECTORY_NAME, workflowDirectory.getAbsolutePath())
                        .apply();
            /*else {
                File mainDir = new File(Environment.getExternalStorageDirectory() + File
                .separator + APP_DIRECTORY_NAME);
                if (mainDir.mkdirs())
                    sp.edit().putString(APP_DIRECTORY_NAME, mainDir.getAbsolutePath()).commit();
                else
                    Toast.makeText(context, "Workflow home not created. Permission issues", Toast
                    .LENGTH_SHORT).show();
            }*/
            }
        } else { //use internal memory to save the data
            File home = context.getDir("Workflows", Context.MODE_PRIVATE);
            sp.edit().putString(APP_DIRECTORY_NAME, home.getAbsolutePath()).apply();
            //     Toast.makeText(context, "Home dir: "+home.getAbsolutePath(), Toast
            // .LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_main, menu);
        restoreActionBar();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
