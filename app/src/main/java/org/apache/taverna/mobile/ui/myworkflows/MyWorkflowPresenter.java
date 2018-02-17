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
package org.apache.taverna.mobile.ui.myworkflows;

import android.util.Log;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.ui.base.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyWorkflowPresenter extends BasePresenter<MyWorkflowMvpView> {

    public final String LOG_TAG = getClass().getSimpleName();

    private DataManager mDataManager;

    private CompositeDisposable compositeDisposable;

    public MyWorkflowPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(MyWorkflowMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void loadMyWorkflows() {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        compositeDisposable.clear();
        compositeDisposable.add(mDataManager.getMyWorkflows(mDataManager.getPreferencesHelper()
                .getUserID(), getQueryOptions())
                .flatMap(new Function<User, ObservableSource<Workflow>>() {
                    @Override
                    public ObservableSource<Workflow> apply(User user) throws Exception {
                        if (user.getWorkflows().getWorkflowList() != null && user.getWorkflows()
                                .getWorkflowList().size() != 0) {
                            return Observable.fromIterable(user.getWorkflows().getWorkflowList())
                                    .concatMap(new Function<Workflow,
                                            ObservableSource<Workflow>>() {
                                        @Override
                                        public ObservableSource<Workflow> apply(Workflow workflow)
                                                throws Exception {
                                            return mDataManager.getDetailWorkflow(workflow.getId(),
                                                    getWorkflowQueryOptions());
                                        }
                                    });
                        } else {
                            return Observable.empty();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Workflow>() {
                    @Override
                    public void onNext(Workflow workflow) {
                        getMvpView().showWorkflow(workflow);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorSnackBar("Server Error! Try after sometime");
                        Log.e(LOG_TAG, "onError: ", e);
                    }

                    @Override
                    public void onComplete() {
                        getMvpView().showProgressbar(false);
                        getMvpView().checkWorkflowSize();
                    }
                }));
    }

    private Map<String, String> getQueryOptions() {
        Map<String, String> option = new HashMap<>();
        option.put("elements", "workflows");
        return option;
    }

    private Map<String, String> getWorkflowQueryOptions() {
        Map<String, String> option = new HashMap<>();
        option.put("elements", "title,type,uploader,preview,created-at");
        return option;
    }
}
