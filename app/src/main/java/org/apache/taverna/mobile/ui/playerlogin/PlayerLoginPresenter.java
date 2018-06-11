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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class PlayerLoginPresenter extends BasePresenter<PlayerLoginMvpView> {

    private static final String TAG = PlayerLoginPresenter.class.getSimpleName();

    private DataManager mDataManager;
    private CompositeDisposable compositeDisposable;

    public PlayerLoginPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(PlayerLoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void playerLogin(final String username, final String password, final boolean loginFlag) {
        compositeDisposable.clear();
        compositeDisposable.add(mDataManager
                .authPlayerUserLoginDetail(getEncodedCredential(username, password), loginFlag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.d(TAG, "onCompleted: " + responseBody.byteStream());
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
                    public void onComplete() {

                    }
                }));
    }

    private String getEncodedCredential(String username, String password) {
        return "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64
                .NO_WRAP);
    }
}
