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
package org.apache.taverna.mobile.ui.favouriteworkflow;

import android.support.v7.widget.SearchView;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.ui.base.BasePresenter;
import org.apache.taverna.mobile.utils.RxSearch;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FavouriteWorkflowsPresenter extends BasePresenter<FavouriteWorkflowsMvpView> {

    public final String LOG_TAG = getClass().getSimpleName();

    private DataManager mDataManager;
    private CompositeDisposable compositeDisposable;

    public FavouriteWorkflowsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(FavouriteWorkflowsMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void loadAllWorkflow() {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        compositeDisposable.add(mDataManager.getFavoriteWorkflowList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<Workflow>>() {

                    @Override
                    public void onComplete() {
                        getMvpView().showProgressbar(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                        getMvpView().showErrorSnackBar();
                    }

                    @Override
                    public void onNext(List<Workflow> workflowList) {
                        if (workflowList.size() != 0) {
                            getMvpView().showWorkflows(workflowList);
                        } else {
                            getMvpView().showEmptyWorkflow();
                        }
                    }
                }));
    }

    public void attachSearchHandler(SearchView searchView) {
        checkViewAttached();
        RxSearch.fromSearchView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String searchQuery) {
                        getMvpView().performSearch(searchQuery);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
