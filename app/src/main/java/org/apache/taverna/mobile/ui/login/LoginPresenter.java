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
package org.apache.taverna.mobile.ui.login;


import android.util.Base64;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<LoginMvpView> {

    public final String LOG_TAG = getClass().getSimpleName();

    private DataManager mDataManager;
    private CompositeDisposable compositeDisposable;

    @Inject
    public LoginPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(LoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void login(String username, String password, boolean flagLogin) {
        checkViewAttached();
        getMvpView().showProgressDialog(true);
        compositeDisposable.add(mDataManager
                .getLoginUserDetail(getEncodedCredential(username, password), flagLogin)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User value) {
                        getMvpView().showDashboardActivity();
                        getMvpView().showProgressDialog(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showCredentialError();
                        getMvpView().showProgressDialog(false);
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
