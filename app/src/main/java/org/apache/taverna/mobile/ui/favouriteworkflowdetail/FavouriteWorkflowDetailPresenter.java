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

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.disposables.CompositeDisposable;

public class FavouriteWorkflowDetailPresenter extends
        BasePresenter<FavouriteWorkflowDetailMvpView> {

    public final String LOG_TAG = getClass().getSimpleName();

    private DataManager mDataManager;
    private CompositeDisposable compositeDisposable;

    @Inject
    public FavouriteWorkflowDetailPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(FavouriteWorkflowDetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void loadWorkflowDetail(String id) {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        compositeDisposable.add(mDataManager.getFavoriteDetailWorkflow(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Workflow>() {
                    @Override
                    public void onNext(Workflow workflow) {
                        getMvpView().showWorkflowDetail(workflow);
                        loadUserDetail(workflow.getUploader().getId());
                        getFavourite(workflow.getId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                    }

                    @Override
                    public void onComplete() {
                        getMvpView().showProgressbar(false);
                    }
                }));
    }

    private void loadUserDetail(String id) {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        compositeDisposable.add(mDataManager.getUserDetail(id, getUserQueryOptions())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        getMvpView().setImage(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                        getMvpView().showErrorSnackBar("Something went wrong please try after " +
                                "sometime");
                    }

                    @Override
                    public void onComplete() {
                        getMvpView().showProgressbar(false);
                    }
                }));
    }

    public void loadLicenseDetail(String id) {
        checkViewAttached();
        getMvpView().showLicenseProgress(true);
        compositeDisposable.add(mDataManager.getLicenseDetail(id, getLicenceQueryOptions())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<License>() {
                    @Override
                    public void onNext(License license) {
                        getMvpView().showLicense(license);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showLicenseProgress(false);
                        getMvpView().showErrorSnackBar("Something went wrong please try after " +
                                "sometime");
                    }

                    @Override
                    public void onComplete() {
                        getMvpView().showLicenseProgress(false);
                    }
                }));
    }

    public void setFavourite(String id) {
        checkViewAttached();
        compositeDisposable.add(mDataManager.setFavoriteWorkflow(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean favoriteStatus) {
                        if (favoriteStatus) {
                            getMvpView().setFavouriteIcon();
                        } else {
                            getMvpView().showErrorSnackBar("Something went wrong please try after" +
                                    "sometime");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorSnackBar("Something went wrong please try after " +
                                "sometime");
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void getFavourite(String id) {
        checkViewAttached();
        compositeDisposable.add(mDataManager.getFavoriteWorkflow(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean b) {
                        getMvpView().getFavouriteIcon(b);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorSnackBar("Something went wrong please try after " +
                                "sometime");
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
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
