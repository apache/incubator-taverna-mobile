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

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.activities.DashboardMainActivity;
import org.apache.taverna.mobile.utils.WorkflowDownloadManager;

import java.io.File;
import java.util.prefs.PreferenceChangeEvent;

/**
 * Created by Larry Akah on 6/9/15.
 */
public class WorkflowdetailFragment extends Fragment implements View.OnClickListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private DownloadManager downloadManager;
    private boolean isDownloading = false;

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
        View rootView = inflater.inflate(R.layout.fragment_workflow_detail, container, false);
        Button download = (Button) rootView.findViewById(R.id.download_wk);
        download.setOnClickListener(this);
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        return rootView;
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
                            "http://www.iceteck.com/volley.jar");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.mark_wk:
                //TODO mark a workflow as important and launch task to store the entry into the local database
                break;
        }
    }
}
