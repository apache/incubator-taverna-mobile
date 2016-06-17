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
import org.apache.taverna.mobile.data.model.Workflows;
import org.apache.taverna.mobile.data.remote.BaseApiManager;

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
    public Observable<Workflows> getAllWorkflow(int pageNumber) {
        return mBaseApiManager.getTavernaApi().getAllWorkflows(pageNumber);
    }
}