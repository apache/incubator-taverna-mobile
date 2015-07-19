package org.apache.taverna.mobile.fragments.workflowdetails;
/**
 * Apache Taverna Mobile
 * Copyright 2015 The Apache Software Foundation

 * This product includes software developed at
 * The Apache Software Foundation (http://www.apache.org/).

 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.adapters.RunAdapter;
import org.apache.taverna.mobile.tavernamobile.Runs;
import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.apache.taverna.mobile.utils.DetailsLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkflowRunHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkflowRunHistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Workflow>{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM2 = "param2";
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private RunAdapter runAdapter;

    private static String workflowID; //represents a run name that matches the given workflow

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkflowRunHistoryFragment.
     */
    public static WorkflowRunHistoryFragment newInstance(String param2) {
        WorkflowRunHistoryFragment fragment = new WorkflowRunHistoryFragment();
        Bundle args = new Bundle();
        workflowID = param2;
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WorkflowRunHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Runs> runsList = new ArrayList<Runs>();
/*        runsList.add(new Runs("Test Run1 ",
                SimpleDateFormat.getDateTimeInstance().format(new Date()).toString()
                ,SimpleDateFormat.getDateTimeInstance().format(new Date()).toString(),"failed"));
        runsList.add(new Runs("Test Run2 ",
                SimpleDateFormat.getDateTimeInstance().format(new Date()).toString()
                ,SimpleDateFormat.getDateTimeInstance().format(new Date()).toString(),"finished"));
       */
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getActivity().getResources().getString(R.string.loading));
        progressDialog.setCancelable(true);

        runAdapter = new RunAdapter(getActivity(),runsList );
       // System.out.println("WorkflowTitle->Run->"+workflowID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_workflow_run_history, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(android.R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getActivity().getLoaderManager().initLoader(0,savedInstanceState,this).forceLoad();
        return rootView;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link android.app.Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.setAdapter(runAdapter);
        mRecyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING);
        //getActivity().getLoaderManager().initLoader(1,null,this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Workflow> onCreateLoader(int i, Bundle bundle) {
        //progressDialog.show();
        return new DetailsLoader(getActivity(),
                DetailsLoader.LOAD_TYPE.TYPE_RUN_HISTORY,
                workflowID);
    }

    @Override
    public void onLoadFinished(Loader<Workflow> workflowLoader, Workflow workflow) {
        runAdapter.setRunList(workflow.getWorkflow_runs());
        mRecyclerView.setAdapter(runAdapter);
       // progressDialog.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<Workflow> workflowLoader) {
        workflowLoader.reset();
    }
}
