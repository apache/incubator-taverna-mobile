package org.apache.taverna.mobile.ui.playerlogin;

import android.util.Base64;
import android.util.Log;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.ui.base.BasePresenter;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PlayerLoginPresenter extends BasePresenter<PlayerLoginMvpView> {

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
                password),loginFlag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: ", e);
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                getMvpView().showCredentialError();
                            } else if (((HttpException) e).code() == 406) {
                                getMvpView().validCredential();
                                mDataManager.getPreferencesHelper().setUserPlayerLoggedInFlag
                                        (loginFlag,getEncodedCredential(username, password));

                            }
                        }
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.e("hello", "onCompleted: " + responseBody.byteStream());
                    }
                });

    }

    private String getEncodedCredential(String username, String password) {

        return "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64
                .NO_WRAP);
    }
}
