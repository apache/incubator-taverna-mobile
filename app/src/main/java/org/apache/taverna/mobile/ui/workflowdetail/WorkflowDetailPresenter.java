package org.apache.taverna.mobile.ui.workflowdetail;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.DetailWorkflow;
import org.apache.taverna.mobile.ui.base.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WorkflowDetailPresenter extends BasePresenter<WorkflowDetailMvpView> {

    public final String LOG_TAG = getClass().getSimpleName();
    private DataManager mDataManager;
    private Subscription mSubscriptions;


    public WorkflowDetailPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(WorkflowDetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }

    public void loadWorkflowDetail(String id) {
        getMvpView().showProgressbar(true);
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
        mSubscriptions = mDataManager.getDetailWorkflow(id, getQueryOptions())
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
                    }

                    @Override
                    public void onNext(DetailWorkflow detailWorkflow) {
                        getMvpView().showWorkflowDetail(detailWorkflow);
                    }
                });

    }

    private Map<String, String> getQueryOptions() {

        Map<String, String> option = new HashMap<>();
        option.put("elements", "id,title,type,uploader,preview,created-at,svg,updated-at," +
                "description,license-type,tags");
        return option;
    }
}
