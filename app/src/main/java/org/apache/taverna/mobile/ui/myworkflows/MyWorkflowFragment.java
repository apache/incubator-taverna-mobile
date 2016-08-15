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
package org.apache.taverna.mobile.ui.myworkflows;

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
import org.apache.taverna.mobile.data.local.PreferencesHelper;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.ui.adapter.RecyclerItemClickListner;
import org.apache.taverna.mobile.ui.adapter.WorkflowAdapter;
import org.apache.taverna.mobile.ui.workflowdetail.WorkflowDetailActivity;
import org.apache.taverna.mobile.utils.ConnectionInfo;
import org.apache.taverna.mobile.utils.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyWorkflowFragment extends Fragment implements MyWorkflowMvpView,
        RecyclerItemClickListner.OnItemClickListener {

    public final String LOG_TAG = getClass().getSimpleName();

    @BindView(R.id.rv_workflows)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_circular)
    ProgressBar mProgressBar;

    @BindView(R.id.swipe_refresh)
    ScrollChildSwipeRefreshLayout mSwipeRefresh;

    private DataManager dataManager;

    private MyWorkflowPresenter mWorkflowPresenter;

    private WorkflowAdapter mWorkflowAdapter;


    private List<Workflow> mWorkflowList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWorkflowList = new ArrayList<>();

        dataManager = new DataManager(new PreferencesHelper(getContext()));

        mWorkflowPresenter = new MyWorkflowPresenter(dataManager);

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

        mWorkflowAdapter = new WorkflowAdapter(mWorkflowList, getContext());

        mRecyclerView.setAdapter(mWorkflowAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListner(getActivity(), this));

        mWorkflowPresenter.loadMyWorkflows();

        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent, R.color
                .colorPrimary);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ConnectionInfo.isConnectingToInternet(getContext())) {

                    mWorkflowPresenter.loadMyWorkflows();

                    mSwipeRefresh.setRefreshing(true);
                } else {

                    showErrorSnackBar("NO Internet Connection");
                    if (mSwipeRefresh.isRefreshing()) {
                        mSwipeRefresh.setRefreshing(false);
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void showProgressbar(boolean b) {

        if (b) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showErrorSnackBar(String error) {

        final Snackbar snackbar = Snackbar.make(mRecyclerView, error, Snackbar
                .LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }

    @Override
    public void showWorkflow(Workflow workflow) {

        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
            mWorkflowList.clear();
        }

        mWorkflowList.add(workflow);
        mWorkflowAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWorkflowPresenter.detachView();
    }

    @Override
    public void onItemClick(View childView, int position) {
        Intent intent = new Intent(getActivity(), WorkflowDetailActivity.class);
        intent.putExtra("id", mWorkflowList.get(position).getId());
        intent.putExtra("title", mWorkflowList.get(position).getTitle());
        startActivity(intent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}
