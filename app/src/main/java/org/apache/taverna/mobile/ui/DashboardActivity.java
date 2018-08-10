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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.ui.anouncements.AnnouncementFragment;
import org.apache.taverna.mobile.ui.base.BaseActivity;
import org.apache.taverna.mobile.ui.favouriteworkflow.FavouriteWorkflowsFragment;
import org.apache.taverna.mobile.ui.login.LoginActivity;
import org.apache.taverna.mobile.ui.myworkflows.MyWorkflowFragment;
import org.apache.taverna.mobile.ui.usage.UsageActivity;
import org.apache.taverna.mobile.ui.userprofile.UserProfileActivity;
import org.apache.taverna.mobile.ui.workflow.WorkflowFragment;
import org.apache.taverna.mobile.utils.ActivityUtils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TableLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class DashboardActivity extends BaseActivity {

    @Inject DataManager dataManager;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Dialog dialog;
    private Fragment fragment;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        setupDrawerContent(navigationView);
        dialog = new Dialog(this);

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

        setNavHeader();
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

                                Intent intent = new Intent(DashboardActivity.this,
                                        UsageActivity.class);
                                startActivity(intent);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.nav_about:

                                TableLayout about = (TableLayout) getLayoutInflater().inflate(R
                                        .layout.about, navigationView, false);

                                dialog.setCanceledOnTouchOutside(true);
                                dialog.setTitle(getString(R.string.title_about));
                                dialog.setContentView(about);
                                dialog.show();
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.os_licences:

                                WebView webView = (WebView) getLayoutInflater().inflate(R.layout
                                        .fragment_licence, navigationView, false);

                                webView.getSettings().setUseWideViewPort(true);
                                webView.loadUrl("file:///android_asset/licences.html");

                                AlertDialog alertDialog = new AlertDialog.Builder(DashboardActivity
                                        .this, R.style.Theme_Taverna_Dialog)
                                        .setTitle(getString(R.string.title_nav_os_licences))
                                        .setView(webView)
                                        .setPositiveButton(android.R.string.ok, null)
                                        .create();

                                alertDialog.show();
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.apache_licences:

                                WebView lWebView = (WebView) getLayoutInflater().inflate(R.layout
                                        .fragment_licence, navigationView, false);

                                lWebView.getSettings().setUseWideViewPort(true);
                                lWebView.
                                        loadUrl("file:///android_asset/apache_licence_notice.html");

                                AlertDialog lAlertDialog = new AlertDialog.Builder(DashboardActivity
                                        .this, R.style.Theme_Taverna_Dialog)
                                        .setTitle(getString(R.string.title_nav_apache_licences))
                                        .setView(lWebView)
                                        .setPositiveButton(android.R.string.ok, null)
                                        .create();

                                lAlertDialog.show();
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.nav_settings:

                                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                                        new SettingFragment(), R.id.frame_container);

                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                toolbar.setTitle(R.string.title_nav_settings);
                                return true;

                            case R.id.nav_logout:

                                signOutConfirmation();
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

    private void signOutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.sign_out)
                .setMessage(R.string.sign_out_message)
                .setPositiveButton(R.string.sign_out, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        signOut();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void signOut() {
        mDrawerLayout.closeDrawers();
        dataManager.getPreferencesHelper().clear();
        dataManager.getDBHelper().clearFavouriteWorkflow();

        startActivity(new Intent(getApplicationContext(),
                LoginActivity.class));
        finish();
    }

    private void setNavHeader() {

        View headerView =  navigationView.getHeaderView(0);
        final CircleImageView navUserAvatar = headerView.findViewById(R.id.nav_user_avatar);

        if (dataManager.getPreferencesHelper().getUserAvatarUrl() != null) {

            String avatarUrl = dataManager.getPreferencesHelper().getUserAvatarUrl();

            Glide.with(getContext())
                    .load(avatarUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                    .error(R.drawable.ic_account_circle_black_24dp)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<?
                                super GlideDrawable> glideAnimation) {
                            navUserAvatar.setImageDrawable(resource);
                        }
                    });

            navUserAvatar.setOnClickListener(new View.OnClickListener() {
                @OnClick
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                }
            });
        } else {

            String avatarUrl = "http://www.myexperiment.org/images/avatar.png";

            Glide.with(getContext())
                    .load(avatarUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                    .error(R.drawable.ic_account_circle_black_24dp)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<?
                                super GlideDrawable> glideAnimation) {
                            navUserAvatar.setImageDrawable(resource);
                        }
                    });

            navUserAvatar.setOnClickListener(new View.OnClickListener() {
                @OnClick
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                }
            });
        }

        String userName = dataManager.getPreferencesHelper().getUserName();
        TextView navUserName = headerView.findViewById(R.id.nav_user_name);
        navUserName.setText(userName);

        String userEmail = dataManager.getPreferencesHelper().getUserEmail();
        TextView navUserEmail = headerView.findViewById(R.id.nav_user_email);
        navUserEmail.setText(userEmail);

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

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(navigationView)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }
}
