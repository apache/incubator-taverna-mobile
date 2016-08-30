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
package org.apache.taverna.mobile.ui.workflowdetail;

import android.util.Base64;
import android.util.Log;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.License;
import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.ui.base.BasePresenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class WorkflowDetailPresenter extends BasePresenter<WorkflowDetailMvpView> {

    public final String LOG_TAG = getClass().getSimpleName();
    private DataManager mDataManager;
    private CompositeSubscription mCompositeSubscription;


    public WorkflowDetailPresenter(DataManager dataManager) {

        mDataManager = dataManager;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void attachView(WorkflowDetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mCompositeSubscription != null) mCompositeSubscription.unsubscribe();
    }

    public void loadWorkflowDetail(String id) {
        getMvpView().showProgressbar(true);

        mCompositeSubscription.add(mDataManager.getDetailWorkflow(id, getDetailQueryOptions())
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
                }));
    }

    private void loadUserDetail(String id) {

        getMvpView().showProgressbar(true);

        mCompositeSubscription.add(mDataManager.getUserDetail(id, getUserQueryOptions())
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
                }));
    }

    public void loadLicenseDetail(String id) {

        getMvpView().showLicenseProgress(true);

        mCompositeSubscription.add(mDataManager.getLicenseDetail(id, getLicenceQueryOptions())
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
                }));
    }

    public void setFavourite(String id) {


        mCompositeSubscription.add(mDataManager.setFavoriteWorkflow(id)
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
                }));
    }

    public void getFavourite(String id) {


        mCompositeSubscription.add(mDataManager.getFavoriteWorkflow(id)
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
                }));
    }

    public void runWorkflow(String contentURL) {

        mCompositeSubscription.add(mDataManager.downloadWorkflowContent("http://www.myexperiment" +
                ".org/workflows/828/download/Pipelined_list_iteration-v.t2flow")
                .concatMap(new Func1<ResponseBody, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> call(ResponseBody responseBody) {

                        StringBuffer sb = new StringBuffer();
                        String post = "";
                        String user = "email" + ":" + "password";
                        String basicAuth = "Basic " + Base64.encodeToString(user.getBytes(),
                                Base64.DEFAULT);
                        boolean flag = false;
                        try {

                            BufferedReader bufferedReader = new BufferedReader(
                                    new InputStreamReader(responseBody.byteStream()));

                            String str = "";

                            while ((str = bufferedReader.readLine()) != null)
                                sb.append(str);

                            bufferedReader.close();

                            String data = "{\"document\":\"data:application/octet-stream;base64," +
                                    Base64.encodeToString(sb.toString().getBytes("UTF-8"), Base64
                                            .URL_SAFE | Base64.NO_WRAP).replace('-', '+') + "\"}";

                            post = "{\"workflow\":" + data + "}";
                            flag = true;
                        } catch (IOException e) {
                            e.printStackTrace();
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        StringBuffer sb = new StringBuffer();
                        try {

                            BufferedReader bufferedReader = new BufferedReader(
                                    new InputStreamReader(responseBody.byteStream()));

                            String str = "";

                            while ((str = bufferedReader.readLine()) != null)
                                sb.append(str);


                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.e("TAG", "onNext: " + sb.toString());
                    }
                }));
    }

    private Map<String, String> getDetailQueryOptions() {

        Map<String, String> option = new HashMap<>();
        option.put("elements", "id,title,type,uploader,preview,created-at,svg,updated-at," +
                "description,license-type,tags");
        return option;
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
