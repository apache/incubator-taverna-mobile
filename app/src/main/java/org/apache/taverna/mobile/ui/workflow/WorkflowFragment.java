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


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.data.model.Workflows;
import org.apache.taverna.mobile.ui.adapter.EndlessRecyclerOnScrollListener;
import org.apache.taverna.mobile.ui.adapter.RecyclerItemClickListner;
import org.apache.taverna.mobile.ui.adapter.WorkflowAdapter;
import org.apache.taverna.mobile.ui.workflowdetail.WorkflowDetailActivity;
import org.apache.taverna.mobile.utils.ConnectionInfo;
import org.apache.taverna.mobile.utils.Constants;
import org.apache.taverna.mobile.utils.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkflowFragment extends Fragment implements WorkflowMvpView,
        RecyclerItemClickListner.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    public final String LOG_TAG = getClass().getSimpleName();

    @BindView(R.id.rv_workflows)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_circular)
    ProgressBar mProgressBar;

    @BindView(R.id.swipe_refresh)
    ScrollChildSwipeRefreshLayout mSwipeRefresh;

    private WorkflowPresenter mWorkflowPresenter;
    private WorkflowAdapter mWorkflowAdapter;

    private int mPageNumber = 1;
    private List<Workflow> mWorkflowList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWorkflowList = new ArrayList<>();
        DataManager dataManager = new DataManager();
        mWorkflowPresenter = new WorkflowPresenter(dataManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
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
                        && mWorkflowList.size() % 10 == 0) {
                    mWorkflowList.add(null);
                    mWorkflowAdapter.notifyItemInserted(mWorkflowList.size());
                    ++mPageNumber;
                    mWorkflowPresenter.loadAllWorkflow(mPageNumber);
                } else if (!ConnectionInfo.isConnectingToInternet(getContext())) {
                    showSnackBar(getActivity().getString(R.string.no_internet_connection));
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
        if (ConnectionInfo.isConnectingToInternet(getContext())) {
            mPageNumber = 1;
            mWorkflowPresenter.loadAllWorkflow(mPageNumber);
            mSwipeRefresh.setRefreshing(true);
        } else {
            showSnackBar(getActivity().getString(R.string.no_internet_connection));
            if (mSwipeRefresh.isRefreshing()) {
                mSwipeRefresh.setRefreshing(false);
            }
        }
    }

    @Override
    public void showProgressbar(boolean show) {
        mSwipeRefresh.setRefreshing(show);
        if (show && mWorkflowAdapter.getItemCount() == 0) {
            mProgressBar.setVisibility(View.VISIBLE);
            mSwipeRefresh.setRefreshing(false);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void showSnackBar(String message) {
        final Snackbar snackbar = Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_INDEFINITE);
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
            mWorkflowList.clear();
        }
        mWorkflowList.addAll(workflows.getWorkflowList());
        mWorkflowAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeLoadMoreProgressbar() {
        if (mPageNumber != 1) {
            mWorkflowList.remove(mWorkflowList.size() - 1);
            mWorkflowAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWorkflowPresenter.detachView();
    }

    @Override
    public void onItemClick(View childView, int position) {
        if (mWorkflowList.get(position) != null && position != -1) {
            Intent intent = new Intent(getActivity(), WorkflowDetailActivity.class);
            intent.putExtra(Constants.WORKFLOW_ID, mWorkflowList.get(position).getId());
            intent.putExtra(Constants.WORKFLOW_TITLE, mWorkflowList.get(position).getTitle());
            startActivity(intent);
        }
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}
