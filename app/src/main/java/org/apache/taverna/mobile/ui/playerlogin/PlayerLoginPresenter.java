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
package org.apache.taverna.mobile.ui.playerlogin;

import android.util.Base64;
import android.util.Log;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.ui.base.BasePresenter;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PlayerLoginPresenter extends BasePresenter<PlayerLoginMvpView> {

    private static final String TAG = PlayerLoginPresenter.class.getSimpleName();

    private DataManager mDataManager;

    private Subscription mSubscriptions;

    public PlayerLoginPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(PlayerLoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }

    public void playerLogin(final String username, final String password, final boolean loginFlag) {
        if (mSubscriptions != null) mSubscriptions.unsubscribe();

        mSubscriptions = mDataManager.authPlayerUserLoginDetail(getEncodedCredential(username,
                password), loginFlag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                getMvpView().showCredentialError();
                            } else if (((HttpException) e).code() == 406) {
                                getMvpView().validCredential();
                                mDataManager.getPreferencesHelper()
                                        .setUserPlayerLoggedInFlagAndCredential(loginFlag,
                                                getEncodedCredential(username, password));

                            } else {
                                getMvpView().showError(R.string.servererr);
                            }
                        }
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.d(TAG, "onCompleted: " + responseBody.byteStream());
                    }
                });

    }

    private String getEncodedCredential(String username, String password) {

        return "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64
                .NO_WRAP);
    }
}
