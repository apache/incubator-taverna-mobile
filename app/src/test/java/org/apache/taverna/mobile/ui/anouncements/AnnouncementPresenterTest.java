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
package org.apache.taverna.mobile.ui.anouncements;

import org.apache.taverna.mobile.FakeRemoteDataSource;
import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.data.model.DetailAnnouncement;
import org.apache.taverna.mobile.utils.RxSchedulersOverrideRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnnouncementPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule rxSchedulersOverrideRule = new
            RxSchedulersOverrideRule();
    @Mock
    DataManager dataManager;

    @Mock
    AnnouncementMvpView announcementMvpView;

    Announcements announcements;
    DetailAnnouncement announcement;
    private AnnouncementPresenter announcementPresenter;
    private Map<String, String> option;

    @Before
    public void setUp() {

        announcementPresenter = new AnnouncementPresenter(dataManager);
        announcementPresenter.attachView(announcementMvpView);
        announcements = FakeRemoteDataSource.getAnnouncements();
        announcement = FakeRemoteDataSource.getAnnouncement();

        option = new HashMap<>();
        option.put("order", "reverse");
        option.put("page", String.valueOf(1));
    }

    @After
    public void tearDown() {
        announcementPresenter.detachView();
    }

    @Test
    public void loadAllAnnouncement_validAnnouncementsData_ReturnsResults() {

        when(dataManager.getAllAnnouncement(option)).thenReturn(
                Observable.just(announcements));

        announcementPresenter.loadAllAnnouncement(1);

        verify(announcementMvpView, never()).showSnackBar(R.string.no_more_announcement_available);
        verify(announcementMvpView, never()).removeLoadMoreProgressBar();
        verify(announcementMvpView).showAllAnnouncement(announcements);
        verify(announcementMvpView, never()).showSnackBar(R.string.failed_to_fetch_announcement);
    }

    @Test
    public void loadAllAnnouncement_NULLAnnouncementsData_RemoveLoadMore() {

        Announcements announcements = new Announcements();
        when(dataManager.getAllAnnouncement(option)).thenReturn(
                Observable.just(announcements));

        announcementPresenter.loadAllAnnouncement(1);

        verify(announcementMvpView).showSnackBar(R.string.no_more_announcement_available);
        verify(announcementMvpView).removeLoadMoreProgressBar();
        verify(announcementMvpView, never()).showAllAnnouncement(announcements);
        verify(announcementMvpView, never()).showSnackBar(R.string.failed_to_fetch_announcement);
    }

    @Test
    public void loadAllAnnouncement_RuntimeError_ShowError() {


        when(dataManager.getAllAnnouncement(option)).thenReturn(
                Observable.<Announcements>error(new RuntimeException()));

        announcementPresenter.loadAllAnnouncement(1);

        verify(announcementMvpView, never()).showSnackBar(R.string.no_more_announcement_available);
        verify(announcementMvpView, never()).removeLoadMoreProgressBar();
        verify(announcementMvpView, never()).showAllAnnouncement(announcements);
        verify(announcementMvpView).showSnackBar(R.string.failed_to_fetch_announcement);
    }

    @Test
    public void loadAnnouncementDetails_validAnnouncementData_ReturnsResults() {

        when(dataManager.getAnnouncementDetail("1")).thenReturn(Observable.just(announcement));
        announcementPresenter.loadAnnouncementDetails("1");

        verify(announcementMvpView).showAnnouncementDetail(announcement);
        verify(announcementMvpView, never()).showSnackBar(R.string.failed_to_fetch_announcement);

    }

    @Test
    public void loadAnnouncementDetails_RuntimeError_ShowError() {

        DetailAnnouncement detailAnnouncement = new DetailAnnouncement();

        when(dataManager.getAnnouncementDetail("1")).thenReturn(Observable
                .<DetailAnnouncement>error(new RuntimeException()));

        announcementPresenter.loadAnnouncementDetails("1");

        verify(announcementMvpView, never()).showAnnouncementDetail(detailAnnouncement);
        verify(announcementMvpView).showSnackBar(R.string.failed_to_fetch_announcement);

    }
}