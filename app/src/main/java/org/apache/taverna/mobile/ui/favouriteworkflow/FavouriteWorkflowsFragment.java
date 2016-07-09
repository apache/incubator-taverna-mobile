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
package org.apache.taverna.mobile.ui.favouriteworkflow;


import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.ui.adapter.FavouriteWorkflowsAdapter;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteWorkflowsFragment extends Fragment implements FavouriteWorkflowsMvpView {

    public final String LOG_TAG = getClass().getSimpleName();

    @BindView(R.id.rv_fav_workflows)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_circular)
    ProgressBar mProgressBar;

    @BindView(R.id.error_no_workflow)
    TextView tvNoWorkflowError;
    private DataManager dataManager;

    private FavouriteWorkflowsPresenter mFavouriteWorkflowsPresenter;

    private FavouriteWorkflowsAdapter mFavouriteWorkflowsAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataManager = new DataManager();

        mFavouriteWorkflowsPresenter = new FavouriteWorkflowsPresenter(dataManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favourite_workflow_list, container, false);

        ButterKnife.bind(this, rootView);

        mFavouriteWorkflowsPresenter.attachView(this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.hasFixedSize();

        showProgressbar(true);
        mFavouriteWorkflowsPresenter.loadAllWorkflow();

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
        final Snackbar snackbar = Snackbar.make(mRecyclerView, "Error occurred.Please try after some time", Snackbar
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
    public void showWorkflows(List<Workflow> workflowList) {

        mFavouriteWorkflowsAdapter = new FavouriteWorkflowsAdapter(workflowList, getContext());
        mRecyclerView.setAdapter(mFavouriteWorkflowsAdapter);
    }

    @Override
    public void showNoWorkflowError() {
        tvNoWorkflowError.setVisibility(View.VISIBLE);
    }
}
