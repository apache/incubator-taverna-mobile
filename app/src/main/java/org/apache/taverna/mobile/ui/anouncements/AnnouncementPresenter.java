package org.apache.taverna.mobile.ui.anouncements;

import android.util.Log;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.DetailAnnouncement;
import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.ui.base.BasePresenter;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sagar
 */
public class AnnouncementPresenter extends BasePresenter<AnnouncementMvpView> {

    public final String LOG_TAG = getClass().getSimpleName();
    private DataManager mDataManager;
    private Subscription mSubscriptions;


    public AnnouncementPresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(AnnouncementMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }

    public void loadAllAnnouncement(int pageNumber){

        mSubscriptions = mDataManager.getAllAnnouncement(pageNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Announcements>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showProgressbar(false);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Log.d(LOG_TAG,e.getMessage());
                        getMvpView().showProgressbar(false);
                        getMvpView().showErrorSnackBar();
                    }

                    @Override
                    public void onNext(Announcements announcement) {
                        getMvpView().showAllAnouncement(announcement);
                        Log.d(LOG_TAG,announcement.getAnnouncement().get(1).getResource());
                    }
                });
    }
    public void loadAnnouncementDetails(String id){

        mSubscriptions = mDataManager.getAnnouncementDetail(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<DetailAnnouncement>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showWaitProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG_TAG,e.getMessage());
                        getMvpView().showWaitProgress(false);
                        getMvpView().showErrorSnackBar();
                    }

                    @Override
                    public void onNext(DetailAnnouncement detailAnnouncement) {
                        getMvpView().showAnnouncementDetail(detailAnnouncement);

                    }
                });
    }

}