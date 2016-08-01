package org.apache.taverna.mobile.ui.login;


import android.util.Base64;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.ui.base.BasePresenter;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<LoginMvpView> {

    public final String LOG_TAG = getClass().getSimpleName();

    private DataManager mDataManager;

    private Subscription mSubscriptions;

    public LoginPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(LoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }

    public void login(String username, String password) {
        if (mSubscriptions != null) mSubscriptions.unsubscribe();

        mSubscriptions = mDataManager.getLoginUserDetail(getEncodedCredential(username, password))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().moveToWorkflowList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showCredentialError();
                    }

                    @Override
                    public void onNext(User user) {

                    }
                });
    }

    private String getEncodedCredential(String username, String password) {

        return "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64
                .NO_WRAP);
    }

}
