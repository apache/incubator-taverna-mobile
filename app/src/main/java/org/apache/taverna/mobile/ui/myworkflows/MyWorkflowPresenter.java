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

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MyWorkflowPresenter extends BasePresenter<MyWorkflowMvpView> {

    public final String LOG_TAG = getClass().getSimpleName();

    private DataManager mDataManager;

    private Subscription mSubscriptions;

    public MyWorkflowPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MyWorkflowMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }

    public void loadMyWorkflows() {
        getMvpView().showProgressbar(true);
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
        mSubscriptions = mDataManager.getMyWorkflows("474", getQueryOptions())
                .flatMap(new Func1<User, Observable<Workflow>>() {
                    @Override
                    public Observable<Workflow> call(User user) {
                        return Observable.from(user.getWorkflows().getWorkflowList())
                                .concatMap(new Func1<Workflow, Observable<? extends Workflow>>() {
                                    @Override
                                    public Observable<? extends Workflow> call(Workflow workflow) {
                                        return mDataManager.getDetailWorkflow(workflow.getId(),
                                                getWorkflowQueryOptions());
                                    }
                                });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Workflow>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showProgressbar(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorSnackBar("Server Error! Try after sometime");
                        Log.e(LOG_TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(Workflow workflow) {
                        getMvpView().showWorkflow(workflow);
                    }
                });

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
