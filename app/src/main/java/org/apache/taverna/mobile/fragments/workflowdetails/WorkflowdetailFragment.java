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

import android.app.Activity;
import android.app.DownloadManager;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.activities.DashboardMainActivity;
import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.apache.taverna.mobile.utils.DetailsLoader;
import org.apache.taverna.mobile.utils.WorkflowDownloadManager;
import org.w3c.dom.Text;

import java.io.File;
import java.util.prefs.PreferenceChangeEvent;

/**
 * Created by Larry Akah on 6/9/15.
 */
public class WorkflowdetailFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Workflow>{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private DownloadManager downloadManager;
    View rootView;
    private ProgressDialog progressDialog;
    public static long WORKFLO_ID;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WorkflowdetailFragment newInstance(int sectionNumber) {
        WorkflowdetailFragment fragment = new WorkflowdetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public WorkflowdetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_workflow_detail, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getActivity().getResources().getString(R.string.loading));
        progressDialog.setCancelable(true);
        WORKFLO_ID = getActivity().getIntent().getLongExtra("workflowid", 0);

        Button download = (Button) rootView.findViewById(R.id.download_wk);
        download.setOnClickListener(this);
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        return rootView;
    }

    /**
     * Called when a fragment is first attached to its activity.
     * {@link #onCreate(android.os.Bundle)} will be called after this.
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.run_wk:
                //TODO implement functionality to issue a run request to the Taverna PLAYER to run the current workflow
                break;
            case R.id.download_wk:
                // start the android Download manager to start downloading a remote workflow file
                WorkflowDownloadManager dmgr = new WorkflowDownloadManager(getActivity(), downloadManager);
                try {
                    dmgr.downloadWorkflow(new File(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(
                                    DashboardMainActivity.APP_DIRECTORY_NAME, "/")),
                            "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.mark_wk:
                //TODO mark a workflow as important and launch task to store the entry into the local database
                break;
        }
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
        getActivity().getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Workflow> onCreateLoader(int i, Bundle bundle) {
       // progressDialog = ProgressDialog.show(getActivity(),"",getActivity().getResources().getString(R.string.loading));
        progressDialog.show();
        return new DetailsLoader(getActivity(),
                DetailsLoader.LOAD_TYPE.TYPE_WORKFLOW_DETAIL,
                getActivity().getIntent().getLongExtra("workflowid", 0));
    }

    @Override
    public void onLoadFinished(Loader<Workflow> workflowLoader, Workflow workflow) {
        TextView author = (TextView) rootView.findViewById(R.id.wkf_author);
            author.setText(workflow.getWorkflow_author());
        TextView title = (TextView) rootView.findViewById(R.id.wtitle);
            title.setText(workflow.getWorkflow_title());
        TextView desc = (TextView) rootView.findViewById(R.id.wdescription);
            desc.setText(workflow.getWorkflow_description());
        TextView createdat = (TextView) rootView.findViewById(R.id.wcreatedat);
            createdat.append(workflow.getWorkflow_datecreated());
        TextView updated = (TextView) rootView.findViewById(R.id.wupdatedat);
            updated.append(workflow.getWorkflow_datemodified());
    //    ImageView preview = (ImageView) rootView.findViewById(R.id.wkf_image);
          //  preview.setImageURI(Uri.parse(workflow.getWorkflow_remote_url()));
      //  progressDialog.cancel();
        progressDialog.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<Workflow> workflowLoader) {
        workflowLoader.reset();
    }
}
