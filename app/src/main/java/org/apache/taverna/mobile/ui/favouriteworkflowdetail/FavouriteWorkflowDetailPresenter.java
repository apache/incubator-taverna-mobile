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
package org.apache.taverna.mobile.ui.favouriteworkflowdetail;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.License;
import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.ui.base.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FavouriteWorkflowDetailPresenter extends
        BasePresenter<FavouriteWorkflowDetailMvpView> {

    public final String LOG_TAG = getClass().getSimpleName();
    private DataManager mDataManager;
    private Subscription mSubscriptions;


    public FavouriteWorkflowDetailPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(FavouriteWorkflowDetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }

    public void loadWorkflowDetail(String id) {
        getMvpView().showProgressbar(true);

        mSubscriptions = mDataManager.getFavoriteDetailWorkflow(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Workflow>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showProgressbar(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                    }

                    @Override
                    public void onNext(Workflow workflow) {
                        getMvpView().showWorkflowDetail(workflow);
                        loadUserDetail(workflow.getUploader().getId());
                        getFavourite(workflow.getId());
                    }
                });

    }

    private void loadUserDetail(String id) {

        getMvpView().showProgressbar(true);

        mSubscriptions = mDataManager.getUserDetail(id, getUserQueryOptions())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showProgressbar(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                        getMvpView().showErrorSnackBar("Something went wrong please try after " +
                                "sometime");
                    }

                    @Override
                    public void onNext(User user) {
                        getMvpView().setImage(user);
                    }
                });
    }

    public void loadLicenseDetail(String id) {

        getMvpView().showLicenseProgress(true);

        mSubscriptions = mDataManager.getLicenseDetail(id, getLicenceQueryOptions())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<License>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showLicenseProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showLicenseProgress(false);
                        getMvpView().showErrorSnackBar("Something went wrong please try after " +
                                "sometime");
                    }

                    @Override
                    public void onNext(License license) {
                        getMvpView().showLicense(license);
                    }
                });
    }

    public void setFavourite(String id) {


        mSubscriptions = mDataManager.setFavoriteWorkflow(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorSnackBar("Something went wrong please try after " +
                                "sometime");
                    }

                    @Override
                    public void onNext(Boolean b) {
                        if (b) {
                            getMvpView().setFavouriteIcon();
                        } else {
                            getMvpView().showErrorSnackBar("Something went wrong please try after" +
                                    "sometime");
                        }

                    }
                });
    }

    public void getFavourite(String id) {


        mSubscriptions = mDataManager.getFavoriteWorkflow(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorSnackBar("Something went wrong please try after " +
                                "sometime");
                    }

                    @Override
                    public void onNext(Boolean b) {
                        getMvpView().getFavouriteIcon(b);
                    }
                });
    }


    private Map<String, String> getUserQueryOptions() {

        Map<String, String> option = new HashMap<>();
        option.put("elements", "avatar");
        return option;
    }

    private Map<String, String> getLicenceQueryOptions() {

        Map<String, String> option = new HashMap<>();
        option.put("elements", "title,description,url,created-at");
        return option;
    }


}
