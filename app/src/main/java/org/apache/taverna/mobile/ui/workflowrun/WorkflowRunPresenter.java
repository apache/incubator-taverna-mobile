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
package org.apache.taverna.mobile.ui.workflowrun;


import android.util.Base64;
import android.util.Log;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.PlayerWorkflow;
import org.apache.taverna.mobile.data.model.PlayerWorkflowDetail;
import org.apache.taverna.mobile.ui.base.BasePresenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class WorkflowRunPresenter extends BasePresenter<WorkflowRunMvpView> {

    private static final String TAG = WorkflowRunPresenter.class.getSimpleName();

    private final DataManager mDataManager;
    private CompositeDisposable compositeDisposable;

    public WorkflowRunPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(WorkflowRunMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void runWorkflow(String contentURL) {
        compositeDisposable.clear();
        compositeDisposable.add(mDataManager.downloadWorkflowContent(contentURL)
                .concatMap(new Function<ResponseBody, ObservableSource<PlayerWorkflow>>() {

                    @Override
                    public ObservableSource<PlayerWorkflow> apply(ResponseBody responseBody)
                            throws Exception {
                        StringBuffer sb = new StringBuffer();
                        String post = "";

                        String basicAuth = mDataManager.getPreferencesHelper()
                                .getUserPlayerCredential();
                        boolean flag = false;
                        try {

                            BufferedReader bufferedReader = new BufferedReader(
                                    new InputStreamReader(responseBody.byteStream()));

                            String str = "";

                            while ((str = bufferedReader.readLine()) != null) {
                                sb.append(str);
                            }

                            bufferedReader.close();

                            String data = "{\"document\":\"data:application/octet-stream;base64," +
                                    Base64.encodeToString(sb.toString().getBytes("UTF-8"), Base64
                                            .URL_SAFE | Base64.NO_WRAP).replace('-', '+') + "\"}";

                            post = "{\"workflow\":" + data + "}";
                            flag = true;
                        } catch (IOException e) {
                            Log.e(TAG, "call: ", e);
                        }
                        if (flag) {
                            RequestBody body =
                                    RequestBody.create(MediaType.parse("application/json"), post);

                            return mDataManager.uploadWorkflowContent(body, basicAuth.trim());
                        } else {
                            return Observable.empty();
                        }
                    }
                })
                .concatMap(new Function<PlayerWorkflow, ObservableSource<PlayerWorkflowDetail>>() {

                    @Override
                    public ObservableSource<PlayerWorkflowDetail> apply(
                            PlayerWorkflow playerWorkflow) throws Exception {
                        return mDataManager.getWorkflowDetail(playerWorkflow.getId());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<PlayerWorkflowDetail>() {
                    @Override
                    public void onNext(PlayerWorkflowDetail playerWorkflowDetail) {
                        getMvpView().setInputsAttribute(playerWorkflowDetail.getRun()
                                .getWorkflowId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }
}
