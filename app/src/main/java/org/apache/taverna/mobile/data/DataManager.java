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

import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.data.model.DetailAnnouncement;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.data.model.License;
import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.data.model.Workflows;
import org.apache.taverna.mobile.data.remote.BaseApiManager;

import java.util.Map;

import rx.Observable;


public class DataManager {

    public BaseApiManager mBaseApiManager = new BaseApiManager();

    public DataManager() {
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
        return mBaseApiManager.getTavernaApi().getAllWorkflows(options);
    }

    /**
     * @return Detail of  Workflow
     */

    public Observable<Workflow> getDetailWorkflow(String id, Map<String, String> options) {
        return mBaseApiManager.getTavernaApi().getDetailWorkflow(id, options);
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

}