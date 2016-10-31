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
package org.apache.taverna.mobile.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.activities.SettingsActivity;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.local.PreferencesHelper;
import org.apache.taverna.mobile.ui.anouncements.AnnouncementFragment;
import org.apache.taverna.mobile.ui.favouriteworkflow.FavouriteWorkflowsFragment;
import org.apache.taverna.mobile.ui.licence.LicenceFragment;
import org.apache.taverna.mobile.ui.login.LoginActivity;
import org.apache.taverna.mobile.ui.myworkflows.MyWorkflowFragment;
import org.apache.taverna.mobile.ui.workflow.WorkflowFragment;
import org.apache.taverna.mobile.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardMainActivity extends AppCompatActivity {

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private Dialog aboutDialog;
    private DataManager dataManager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard_main);

        ButterKnife.bind(this);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        aboutDialog = new Dialog(this);

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

            fragment = new WorkflowFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment,
                    R.id.frame_container);

            navigationView.setCheckedItem(R.id.nav_workflows);
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

                        switch (menuItem.getItemId()) {
                            case R.id.nav_workflows:

                                fragment = new WorkflowFragment();
                                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                                        fragment, R.id.frame_container);

                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                toolbar.setTitle(R.string.title_nav_all_workflows);

                                return true;

                            case R.id.nav_my_workflows:

                                fragment = new MyWorkflowFragment();
                                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                                        fragment, R.id.frame_container);

                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                toolbar.setTitle(R.string.title_nav_my_workflows);
                                return true;

                            case R.id.nav_favourite_workflow:

                                fragment = new FavouriteWorkflowsFragment();
                                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                                        fragment, R.id.frame_container);

                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                toolbar.setTitle(R.string.title_nav_favourite_workflows);
                                return true;

                            case R.id.nav_announcement:

                                fragment = new AnnouncementFragment();
                                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                                        fragment, R.id.frame_container);

                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                toolbar.setTitle(R.string.title_nav_announcement);
                                return true;


                            case R.id.nav_usage:

                                aboutDialog.setCanceledOnTouchOutside(true);
                                aboutDialog.setTitle(getString(R.string.title_nav_usage));
                                aboutDialog.setContentView(R.layout.usage_layout);
                                aboutDialog.show();
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.nav_about:

                                TableLayout about = (TableLayout) getLayoutInflater().inflate(R
                                        .layout.about, navigationView, false);

                                aboutDialog.setCanceledOnTouchOutside(true);
                                aboutDialog.setTitle(getString(R.string.title_nav_usage));
                                aboutDialog.setContentView(about);
                                aboutDialog.show();
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.os_licences:

                                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                                        LicenceFragment.newInstance(), R.id.frame_container);

                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                toolbar.setTitle(R.string.title_nav_os_licences);
                                return true;

                            case R.id.nav_settings:

                                startActivity(new Intent(getApplicationContext(),
                                        SettingsActivity.class));
                                overridePendingTransition(android.R.anim.slide_in_left, android.R
                                        .anim.slide_out_right);

                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.nav_logout:

                                finish();

                                mDrawerLayout.closeDrawers();
                                dataManager.getPreferencesHelper().setLoggedInFlag(false);

                                startActivity(new Intent(getApplicationContext(),
                                        LoginActivity.class));

                                return true;

                        }
                        return true;
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_main, menu);

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
