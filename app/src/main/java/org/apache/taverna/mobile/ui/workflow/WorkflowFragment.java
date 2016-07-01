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
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.data.model.Workflows;
import org.apache.taverna.mobile.ui.adapter.EndlessRecyclerOnScrollListener;
import org.apache.taverna.mobile.ui.adapter.RecyclerItemClickListner;
import org.apache.taverna.mobile.ui.adapter.WorkflowAdapter;
import org.apache.taverna.mobile.ui.workflowdetail.WorkflowDetailActivity;
import org.apache.taverna.mobile.utils.ConnectionInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkflowFragment extends Fragment implements WorkflowMvpView, RecyclerItemClickListner.OnItemClickListener {
    public final String LOG_TAG = getClass().getSimpleName();

    @BindView(R.id.rvDashboard)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_circular)
    ProgressBar mProgressBar;

    private DataManager dataManager;

    private WorkflowPresenter mWorkflowPresenter;

    private WorkflowAdapter mWorkflowAdapter;

    private ConnectionInfo mConnectionInfo;
    private int mPageNumber = 1;
    private List<Workflow> mWorkflowList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWorkflowList = new ArrayList<>();
        dataManager = new DataManager();
        mWorkflowPresenter = new WorkflowPresenter(dataManager);
        mConnectionInfo = new ConnectionInfo(getContext());
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

        mWorkflowPresenter.loadAllWorkflow(mPageNumber);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                if (mConnectionInfo.isConnectingToInternet()
                        && mWorkflowList.size() % 10 == 0) {
                    mWorkflowList.add(null);
                    mWorkflowAdapter.notifyItemInserted(mWorkflowList.size());
                    ++mPageNumber;
                    mWorkflowPresenter.loadAllWorkflow(mPageNumber);
                    Log.d(LOG_TAG, "Loading more");
                } else if (!mConnectionInfo.isConnectingToInternet()) {
                    Log.d(LOG_TAG, "Internet not available. Not loading more posts.");
                    showErrorSnackBar();
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
    public void showErrorSnackBar() {

    }

    @Override
    public void showWorkflows(Workflows workflows) {
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
        Intent intent=new Intent(getActivity() , WorkflowDetailActivity.class);
        intent.putExtra("id",mWorkflowList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}
