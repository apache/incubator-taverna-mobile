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

import android.util.Log;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.DetailAnnouncement;
import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.ui.base.BasePresenter;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class AnnouncementPresenter extends BasePresenter<AnnouncementMvpView> {

    public final String LOG_TAG = getClass().getSimpleName();
    private DataManager mDataManager;
    private Subscription mSubscriptions;


    public AnnouncementPresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(AnnouncementMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }

    public void loadAllAnnouncement(int pageNumber){

        mSubscriptions = mDataManager.getAllAnnouncement(pageNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Announcements>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showProgressbar(false);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Log.d(LOG_TAG,e.getMessage());
                        getMvpView().showProgressbar(false);
                        getMvpView().showErrorSnackBar();
                    }

                    @Override
                    public void onNext(Announcements announcement) {
                        getMvpView().showAllAnouncement(announcement);
                        Log.d(LOG_TAG,announcement.getAnnouncement().get(1).getResource());
                    }
                });
    }
    public void loadAnnouncementDetails(String id){

        mSubscriptions = mDataManager.getAnnouncementDetail(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<DetailAnnouncement>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showWaitProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG_TAG,e.getMessage());
                        getMvpView().showWaitProgress(false);
                        getMvpView().showErrorSnackBar();
                    }

                    @Override
                    public void onNext(DetailAnnouncement detailAnnouncement) {
                        getMvpView().showAnnouncementDetail(detailAnnouncement);

                    }
                });
    }

}