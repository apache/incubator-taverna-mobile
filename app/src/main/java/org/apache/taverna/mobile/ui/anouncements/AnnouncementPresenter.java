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
import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.data.model.DetailAnnouncement;
import org.apache.taverna.mobile.ui.base.BasePresenter;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.disposables.CompositeDisposable;

public class AnnouncementPresenter extends BasePresenter<AnnouncementMvpView> {

    public final String LOG_TAG = getClass().getSimpleName();

    private CompositeDisposable compositeDisposable;

    @Inject
    DataManager mDataManager;

    @Inject
    public AnnouncementPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void loadAllAnnouncement(int pageNumber) {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        compositeDisposable.add(mDataManager.getAllAnnouncement(
                getAnnouncementQueryOptions(pageNumber))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Announcements>() {

                    @Override
                    public void onNext(Announcements announcements) {
                        if (announcements.getAnnouncement() != null) {
                            getMvpView().showAllAnnouncement(announcements);
                        } else {
                            getMvpView().showSnackBar(R.string.no_more_announcement_available);
                            getMvpView().removeLoadMoreProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                        getMvpView().showSnackBar(R.string.failed_to_fetch_announcement);
                    }

                    @Override
                    public void onComplete() {
                        getMvpView().showProgressbar(false);
                    }
                }));
    }

    public void loadAnnouncementDetails(String id) {
        checkViewAttached();
        compositeDisposable.add(mDataManager.getAnnouncementDetail(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<DetailAnnouncement>() {

                    @Override
                    public void onNext(DetailAnnouncement detailAnnouncement) {
                        getMvpView().showAnnouncementDetail(detailAnnouncement);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG_TAG, e.getMessage());
                        getMvpView().showWaitProgress(false);
                        getMvpView().showSnackBar(R.string.failed_to_fetch_announcement);
                    }

                    @Override
                    public void onComplete() {
                        getMvpView().showWaitProgress(false);
                    }
                }));
    }


    private Map<String, String> getAnnouncementQueryOptions(int PageNumber) {
        Map<String, String> option = new HashMap<>();
        option.put("order", "reverse");
        option.put("page", String.valueOf(PageNumber));
        return option;
    }

}