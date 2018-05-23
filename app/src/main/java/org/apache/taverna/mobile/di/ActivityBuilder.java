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
package org.apache.taverna.mobile.di;

import org.apache.taverna.mobile.ui.DashboardActivity;
import org.apache.taverna.mobile.ui.DashboardActivityModule;
import org.apache.taverna.mobile.ui.FlashScreenActivity;
import org.apache.taverna.mobile.ui.anouncements.AnnouncementFragment;
import org.apache.taverna.mobile.ui.favouriteworkflow.FavouriteWorkflowsFragment;
import org.apache.taverna.mobile.ui.favouriteworkflowdetail.FavouriteWorkflowDetailActivity;
import org.apache.taverna.mobile.ui.favouriteworkflowdetail.FavouriteWorkflowDetailFragment;
import org.apache.taverna.mobile.ui.imagezoom.ImageZoomActivity;
import org.apache.taverna.mobile.ui.imagezoom.ImageZoomFragment;
import org.apache.taverna.mobile.ui.login.LoginActivity;
import org.apache.taverna.mobile.ui.login.LoginFragment;
import org.apache.taverna.mobile.ui.myworkflows.MyWorkflowFragment;
import org.apache.taverna.mobile.ui.playerlogin.PlayerLoginFragment;
import org.apache.taverna.mobile.ui.tutorial.TutorialActivity;
import org.apache.taverna.mobile.ui.usage.UsageActivity;
import org.apache.taverna.mobile.ui.workflow.WorkflowFragment;
import org.apache.taverna.mobile.ui.workflowdetail.WorkflowDetailActivity;
import org.apache.taverna.mobile.ui.workflowdetail.WorkflowDetailFragment;
import org.apache.taverna.mobile.ui.workflowrun.WorkflowRunActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract DashboardActivity contributeDashboardActivity();

    @ContributesAndroidInjector
    abstract AnnouncementFragment contributeAnnouncementFragment();

    @ContributesAndroidInjector
    abstract FavouriteWorkflowsFragment contributeFavouriteWorkflowsFragment();

    @ContributesAndroidInjector
    abstract FavouriteWorkflowDetailActivity contributeFavouriteWorkflowDetailActivity();

    @ContributesAndroidInjector
    abstract FavouriteWorkflowDetailFragment contributeFavouriteWorkflowDetailFragment();

    @ContributesAndroidInjector
    abstract ImageZoomActivity contributeImageZoomActivity();

    @ContributesAndroidInjector
    abstract ImageZoomFragment contributeImageZoomFragment();

    @ContributesAndroidInjector
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector
    abstract LoginFragment contributeLoginFragment();

    @ContributesAndroidInjector
    abstract MyWorkflowFragment contributeMyWorkflowFragment();

    @ContributesAndroidInjector
    abstract PlayerLoginFragment contributePlayerLoginFragment();

    @ContributesAndroidInjector
    abstract TutorialActivity contributeTutorialActivity();

    @ContributesAndroidInjector
    abstract UsageActivity contributeUsageActivity();

    @ContributesAndroidInjector
    abstract WorkflowFragment contributeWorkflowFragment();

    @ContributesAndroidInjector
    abstract WorkflowDetailActivity contributeWorkflowDetailActivity();

    @ContributesAndroidInjector
    abstract WorkflowDetailFragment contributeWorkflowDetailFragment();

    @ContributesAndroidInjector
    abstract WorkflowRunActivity contributeWorkflowRunActivity();

    @ContributesAndroidInjector
    abstract FlashScreenActivity contributeFlashScreenActivity();

}
