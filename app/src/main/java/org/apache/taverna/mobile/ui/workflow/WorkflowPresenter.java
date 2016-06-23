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
package org.apache.taverna.mobile.ui.workflow;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.DetailWorkflow;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.data.model.Workflows;
import org.apache.taverna.mobile.ui.base.BasePresenter;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class WorkflowPresenter extends BasePresenter<WorkflowMvpView> {

    public final String LOG_TAG = getClass().getSimpleName();
    private DataManager mDataManager;
    private Subscription mSubscriptions;


    public WorkflowPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(WorkflowMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }

    public void loadAllWorkflow(int pageNumber) {
        if (pageNumber == 1) {
            getMvpView().showProgressbar(true);
        }
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
        mSubscriptions = mDataManager.getAllWorkflow(pageNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Workflows>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Workflows workflows) {
                        getMvpView().removeLoadMoreProgressbar();
                        loadDetailWorkFlow(workflows.getWorkflowList());
                    }
                });

    }

    public void loadDetailWorkFlow(List<Workflow> workflowList) {
        for (int i = 0; i < workflowList.size(); i++) {
            mDataManager.getDetailWorkflow(workflowList.get(i).getId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<DetailWorkflow>() {
                        @Override
                        public void onCompleted() {
                            getMvpView().showProgressbar(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            getMvpView().showProgressbar(false);
                            getMvpView().showErrorSnackBar();
                        }

                        @Override
                        public void onNext(DetailWorkflow detailWorkflow) {
                            getMvpView().showWorkflowDetail(detailWorkflow);
                        }
                    });
        }
    }
}