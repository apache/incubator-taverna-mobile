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
package org.apache.taverna.mobile.data;

import org.apache.taverna.mobile.data.local.DBHelper;
import org.apache.taverna.mobile.data.local.PreferencesHelper;
import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.data.model.DetailAnnouncement;
import org.apache.taverna.mobile.data.model.License;
import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.data.model.Workflows;
import org.apache.taverna.mobile.data.remote.BaseApiManager;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;


public class DataManager {

    public BaseApiManager mBaseApiManager = new BaseApiManager();

    public DBHelper mDBHelper = new DBHelper();

    private PreferencesHelper mPreferencesHelper;

    public DataManager() {
    }

    public DataManager(PreferencesHelper mPreferencesHelper) {
        this.mPreferencesHelper = mPreferencesHelper;
    }

    /**
     * @return PreferenceHelper
     */
    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    /**
     * @return List of all Announcement
     */
    public Observable<Announcements> getAllAnnouncement(int pageNumber) {
        return mBaseApiManager.getTavernaApi().getAllAnnouncements(pageNumber);
    }

    /**
     * @return Detail of Announcement
     */
    public Observable<DetailAnnouncement> getAnnouncementDetail(String id) {
        return mBaseApiManager.getTavernaApi().getAnnouncement(id);
    }

    /**
     * @return List of all Workflow
     */
    public Observable<Workflows> getAllWorkflow(Map<String, String> options) {
        return mBaseApiManager.getTavernaApi().getAllWorkflows(options)
                .concatMap(new Func1<Workflows, Observable<? extends Workflows>>() {
                    @Override
                    public Observable<? extends Workflows> call(Workflows workflows) {
                        return mDBHelper.syncWorkflows(workflows);
                    }
                });
    }

    /**
     * @return Detail of  Workflow
     */

    public Observable<Workflow> getDetailWorkflow(String id, Map<String, String> options) {
        return mBaseApiManager.getTavernaApi().getDetailWorkflow(id, options)
                .concatMap(new Func1<Workflow, Observable<? extends Workflow>>() {
                    @Override
                    public Observable<? extends Workflow> call(Workflow workflow) {
                        return mDBHelper.syncWorkflow(workflow);
                    }
                });
    }

    /**
     * @return Detail of  User
     */

    public Observable<User> getUserDetail(String id, Map<String, String> options) {
        return mBaseApiManager.getTavernaApi().getUserDetail(id, options);
    }

    /**
     * @return Detail of  Licence
     */

    public Observable<License> getLicenseDetail(String id, Map<String, String> options) {
        return mBaseApiManager.getTavernaApi().getLicenseDetail(id, options);
    }

    /**
     * @return Is Workflow toggle Favourite or not
     */

    public Observable<Boolean> setFavoriteWorkflow(String id) {
        return mDBHelper.setFavouriteWorkflow(id);
    }

    /**
     * @return Is Workflow  Favourite or not
     */

    public Observable<Boolean> getFavoriteWorkflow(String id) {
        return mDBHelper.getFavouriteWorkflow(id);
    }

    /**
     * @return Favourite Workflow list
     */

    public Observable<List<Workflow>> getFavoriteWorkflowList() {
        return mDBHelper.getFavouriteWorkflow();
    }

    /**
     * @param id is the id of workflow
     * @return Favourite Workflow Detail from DBhelper
     */

    public Observable<Workflow> getFavoriteDetailWorkflow(String id) {
        return mDBHelper.getFavouriteWorkflowDetail(id);
    }

    /**
     * @param credentials is base64 encoded credential
     * @param flagLogin  is used to maintain the Remain login or not
     * @return User Detail if valid credentials
     */

    public Observable<User> getLoginUserDetail(String credentials, final boolean flagLogin) {
        return mBaseApiManager.getTavernaApi().getLoginUserDetail(credentials)
                .concatMap(new Func1<User, Observable<? extends User>>() {
                    @Override
                    public Observable<? extends User> call(User user) {
                        mPreferencesHelper.setLoggedInFlag(flagLogin);

                        return mPreferencesHelper.saveUserDetail(user);
                    }
                });
    }


    public Observable<ResponseBody> downloadWorkflowContent(String url) {
        return mBaseApiManager.getTavernaApi().downloadWorkflowContent(url);
    }

}