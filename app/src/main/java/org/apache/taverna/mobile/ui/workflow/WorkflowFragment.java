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

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.data.model.Workflows;
import org.apache.taverna.mobile.ui.adapter.EndlessRecyclerOnScrollListener;
import org.apache.taverna.mobile.ui.adapter.RecyclerItemClickListner;
import org.apache.taverna.mobile.ui.adapter.WorkflowAdapter;
import org.apache.taverna.mobile.ui.base.BaseActivity;
import org.apache.taverna.mobile.ui.workflowdetail.WorkflowDetailActivity;
import org.apache.taverna.mobile.utils.ConnectionInfo;
import org.apache.taverna.mobile.utils.Constants;
import org.apache.taverna.mobile.utils.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkflowFragment extends Fragment implements WorkflowMvpView,
        RecyclerItemClickListner.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    public final String LOG_TAG = getClass().getSimpleName();

    @Inject
    WorkflowPresenter mWorkflowPresenter;
    WorkflowAdapter mWorkflowAdapter;
    WorkflowAdapter mSearchWorkflowAdapter;

    @BindView(R.id.rv_workflows)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_circular)
    ProgressBar mProgressBar;

    @BindView(R.id.swipe_refresh)
    ScrollChildSwipeRefreshLayout mSwipeRefresh;

    private int mPageNumber = 1;

    private int mSearchPageNumber = 1;
    private List<Workflow> mWorkflowList;
    private List<Workflow> mSearchWorkflowList;

    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWorkflowList = new ArrayList<>();
        mSearchWorkflowList = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        mWorkflowPresenter.attachView(this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.hasFixedSize();
        mWorkflowAdapter = new WorkflowAdapter(mWorkflowList, getActivity());
        mRecyclerView.setAdapter(mWorkflowAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListner(getActivity(), this));

        mWorkflowPresenter.loadAllWorkflow(mPageNumber);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (ConnectionInfo.isConnectingToInternet(getContext())
                        && mRecyclerView.getAdapter().getItemCount() % 10 == 0) {
                    if (searchView.isIconified() || TextUtils.isEmpty(searchView.getQuery())) {
                        addLoadMoreProgressbar();
                        ++mPageNumber;
                        mWorkflowPresenter.loadAllWorkflow(mPageNumber);
                    } else {
                        addLoadMoreProgressbar();
                        ++mSearchPageNumber;
                        mWorkflowPresenter.searchWorkflow(mSearchPageNumber, searchView.getQuery()
                                .toString());
                    }
                } else if (!ConnectionInfo.isConnectingToInternet(getContext())) {
                    showSnackBar(R.string.no_internet_connection);
                }
            }
        });

        mSwipeRefresh.setColorSchemeColors(getActivity()
                .getResources().getIntArray(R.array.swipeRefreshColors));
        mSwipeRefresh.setOnRefreshListener(this);

        return rootView;
    }


    @Override
    public void onRefresh() {
        if (searchView.isIconified()) {
            if (ConnectionInfo.isConnectingToInternet(getContext())) {
                mPageNumber = 1;
                mSearchPageNumber = 1;
                mWorkflowPresenter.loadAllWorkflow(mPageNumber);
                mSwipeRefresh.setRefreshing(true);
            } else {
                showSnackBar(R.string.no_internet_connection);
                if (mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        }
    }

    @Override
    public void showProgressbar(boolean show) {
        if (show && mWorkflowAdapter.getItemCount() == 0) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSnackBar(int message) {
        final Snackbar snackbar = Snackbar.make(mRecyclerView, getActivity().getString(message),
                Snackbar.LENGTH_SHORT);
        snackbar.setAction(getActivity().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public void showWorkflows(Workflows workflows) {
        if (mPageNumber == 1) {
            mSwipeRefresh.setRefreshing(false);
            mWorkflowList.clear();
        }
        mWorkflowList.addAll(workflows.getWorkflowList());
        mWorkflowAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeLoadMoreProgressbar() {
        if (mPageNumber != 1 || mSearchPageNumber != 1) {
            ((WorkflowAdapter) mRecyclerView.getAdapter()).removeLastNullWorkflow();
        }
    }

    @Override
    public void addLoadMoreProgressbar() {
        ((WorkflowAdapter) mRecyclerView.getAdapter()).addWorkflow(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWorkflowPresenter.detachView();
    }

    @Override
    public void onItemClick(View childView, int position) {
        if (searchView.isIconified() && TextUtils.isEmpty(searchView.getQuery())) {
            if (mWorkflowAdapter.getItem(position) != null && position != -1) {
                Intent intent = new Intent(getActivity(), WorkflowDetailActivity.class);
                intent.putExtra(Constants.WORKFLOW_ID, mWorkflowAdapter.getItem(position).getId());
                intent.putExtra(Constants.WORKFLOW_TITLE, mWorkflowAdapter.getItem(position)
                        .getTitle());
                startActivity(intent);
            }
        } else {
            if (mSearchWorkflowAdapter.getItem(position) != null && position != -1) {
                Intent intent = new Intent(getActivity(), WorkflowDetailActivity.class);
                intent.putExtra(Constants.WORKFLOW_ID, mSearchWorkflowAdapter.getItem(position)
                        .getId());
                intent.putExtra(Constants.WORKFLOW_TITLE, mSearchWorkflowAdapter.getItem(position)
                        .getTitle());
                startActivity(intent);
            }
        }
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context
                .SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity()
                .getComponentName()));
        searchView.setSubmitButtonEnabled(false);
        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                mWorkflowPresenter.attachSearchHandler(searchView);
                mSearchWorkflowList.clear();
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                mRecyclerView.swapAdapter(mWorkflowAdapter, true);
                mSwipeRefresh.setRefreshing(false);
                mSearchWorkflowList.clear();
                mSearchPageNumber = 1;
            }
        });

    }

    @Override
    public void performSearch(String search) {

        mSearchWorkflowAdapter = new WorkflowAdapter(mSearchWorkflowList, getContext());
        WorkflowAdapter wk = mWorkflowAdapter;
        if (!TextUtils.isEmpty(search)) {
            if (wk != null)
                for (int i = 0; i < wk.getItemCount(); i++) {
                    Workflow workflow = wk.getItem(i);
                    if (workflow.getTitle().toLowerCase().contains(search.toLowerCase())) {
                        mSearchWorkflowAdapter.addWorkflow(workflow);
                    }
                }
            mRecyclerView.swapAdapter(mSearchWorkflowAdapter, true);
        } else {
            mRecyclerView.swapAdapter(mWorkflowAdapter, true);
            mSearchPageNumber = 1;
        }
    }

    @Override
    public void showSearchResult(List<Workflow> workflowList) {
        if (mSearchPageNumber == 1) {
            mSearchWorkflowList.clear();   // to remove local search
            mSearchWorkflowList.addAll(workflowList);
            mSearchWorkflowAdapter.notifyDataSetChanged();
        } else {
            mSearchWorkflowList.addAll(workflowList);
            mSearchWorkflowAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showSwipeRefreshLayout(boolean flag) {
        if (flag) {
            mSwipeRefresh.setRefreshing(flag);
        } else {
            if (mSwipeRefresh.isRefreshing()) {
                mSwipeRefresh.setRefreshing(flag);
            }
        }
    }


}
