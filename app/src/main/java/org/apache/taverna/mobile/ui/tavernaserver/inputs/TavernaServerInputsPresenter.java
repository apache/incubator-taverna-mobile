package org.apache.taverna.mobile.ui.tavernaserver.inputs;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.Inputs;
import org.apache.taverna.mobile.ui.base.BasePresenter;
import org.apache.taverna.mobile.ui.tavernaserver.createrun.TavernaServerCreateRunMvpView;
import org.apache.taverna.mobile.ui.tavernaserver.createrun.TavernaServerCreateRunPresenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ian on 16/12/16.
 */

public class TavernaServerInputsPresenter extends BasePresenter<TavernaServerInputsMvpView> {

    private static final String TAG = TavernaServerInputsPresenter.class.getSimpleName();

    private DataManager mDataManager;

    private Subscription mSubscriptions;

    public TavernaServerInputsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(TavernaServerInputsMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }

    public void workflowInputs(final String username, final String password, final String runLocation) {

        String basicAuth = getEncodedCredential(username, password);

         mDataManager.getWorkflowInputs(basicAuth.trim(), runLocation)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Inputs>() {

                    @Override
                    public void onCompleted() {
                        System.out.println("Complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e);
                    }

                    @Override
                    public void onNext(Inputs inputs) {
                        getMvpView().setInputs(inputs);
                    }

                });

    }

    private String getEncodedCredential(String username, String password) {

        return "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64
                .NO_WRAP);
    }
}
