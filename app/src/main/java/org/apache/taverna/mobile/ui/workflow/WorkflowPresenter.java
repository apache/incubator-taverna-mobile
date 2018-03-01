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
package org.apache.taverna.mobile.ui.workflow;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.Search;
import org.apache.taverna.mobile.data.model.Workflows;
import org.apache.taverna.mobile.ui.base.BasePresenter;
import org.apache.taverna.mobile.utils.RxSearch;

import android.support.v7.widget.SearchView;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class WorkflowPresenter extends BasePresenter<WorkflowMvpView> {

    public final String LOG_TAG = WorkflowPresenter.class.getSimpleName();
    private DataManager mDataManager;
    private CompositeDisposable compositeDisposable;


    public WorkflowPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(WorkflowMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void loadAllWorkflow(int pageNumber) {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        compositeDisposable.add(mDataManager.getAllWorkflow(getQueryOptions(pageNumber))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Workflows>() {
                    @Override
                    public void onNext(Workflows workflows) {
                        getMvpView().showProgressbar(false);
                        getMvpView().removeLoadMoreProgressbar();
                        getMvpView().showWorkflows(workflows);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                        getMvpView().showSnackBar(R.string.error_failed_to_fetch_workflow);
                        getMvpView().removeLoadMoreProgressbar();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));

    }

    public void attachSearchHandler(final SearchView searchView) {
        checkViewAttached();
        compositeDisposable.add(RxSearch.fromSearchView(searchView)
                .distinctUntilChanged()
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String searchText) {
                        getMvpView().performSearch(searchText);
                        if (!TextUtils.isEmpty(searchText)) {
                            searchWorkflow(1, searchText);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void detachSearchHandler() {
        compositeDisposable.clear();
    }

    public void searchWorkflow(int pageNumber, String query) {
        checkViewAttached();
        if (!TextUtils.isEmpty(query)) {
            if (pageNumber == 1) {
                getMvpView().showSwipeRefreshLayout(true);
            }
            compositeDisposable.add(mDataManager.getSearchWorkflowResult(getSearchQueryOptions
                    (pageNumber, query))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Search>() {
                        @Override
                        public void onNext(Search search) {
                            getMvpView().removeLoadMoreProgressbar();
                            if (search.getWorkflowList() != null &&
                                    search.getWorkflowList().size() > 0) {
                                getMvpView().showSearchResult(search.getWorkflowList());
                            } else {
                                getMvpView().showSnackBar(R.string.msg_no_workflow_found);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            getMvpView().showSwipeRefreshLayout(false);
                        }

                        @Override
                        public void onComplete() {
                            getMvpView().showSwipeRefreshLayout(false);
                        }
                    }));
        }
    }

    private Map<String, String> getQueryOptions(int pageNumber) {
        Map<String, String> option = new HashMap<>();
        option.put("elements", "title,type,uploader,preview,created-at");
        option.put("page", String.valueOf(pageNumber));
        option.put("num", String.valueOf(10));
        option.put("order", "reverse");
        return option;
    }

    private Map<String, String> getSearchQueryOptions(int pageNumber, String query) {
        Map<String, String> option = getQueryOptions(pageNumber);
        option.put("query", query);
        option.put("type", "workflow");
        return option;
    }
}
