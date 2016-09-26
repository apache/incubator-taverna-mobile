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
package org.apache.taverna.mobile.ui.workflowrun;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.widget.Toast;

import com.anton46.stepsview.StepsView;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.local.PreferencesHelper;
import org.apache.taverna.mobile.ui.DownloadingFragment;
import org.apache.taverna.mobile.ui.playerlogin.PlayerLoginFragment;
import org.apache.taverna.mobile.utils.NonSwipeableViewPager;
import org.apache.taverna.mobile.utils.WebViewGen;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class WorkflowRunActivity extends FragmentActivity implements WorkflowRunMvpView,
        PlayerLoginFragment.OnSuccessful {

    public static final String WORKFLOW_URL = "Workflow_url";


    private DataManager dataManager;

    private WorkflowRunPresenter mWorkflowRunPresenter;

    @BindView(R.id.stepsView)
    StepsView mStepsView;

    @BindView(R.id.viewpager)
    NonSwipeableViewPager mPager;

    private PagerAdapter mPagerAdapter;

    private final String[] labels = {"Player\nLogin","Download", "Upload", "Inputs &\n Run"};
    int position =0;

    String workflowRunURL;

    String workflowURL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workflow_run);

        ButterKnife.bind(this);

        dataManager = new DataManager(new PreferencesHelper(getContext()));

        mWorkflowRunPresenter = new WorkflowRunPresenter(dataManager);

        mWorkflowRunPresenter.attachView(this);


        mStepsView.setCompletedPosition(position % labels.length)
                .setLabels(labels)
                .setBarColorIndicator(
                        getContext().getResources().getColor(R.color.material_blue_grey_800))
                .setProgressColorIndicator(getContext().getResources().getColor(R.color.colorPrimary))
                .setLabelColorIndicator(getContext().getResources().getColor(R.color.colorPrimary))
                .drawView();


        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        if(dataManager.getPreferencesHelper().isUserPlayerLoggedInFlag()){
            mPager.setCurrentItem(++position);
            mStepsView.setCompletedPosition(position % labels.length).drawView();

            mWorkflowRunPresenter.runWorkflow(getIntent().getStringExtra(WORKFLOW_URL));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWorkflowRunPresenter.detachView();
    }


    @Override
    public void onSuccessfulLogin() {
        position =1;
        mPager.setCurrentItem(position);
        mStepsView.setCompletedPosition(position % labels.length).drawView();
        mWorkflowRunPresenter.runWorkflow("hh");
    }

    @Override
    public void movetoUploadWorkflow() {
        position =2;
        mPager.setCurrentItem(position);
        mStepsView.setCompletedPosition(position % labels.length).drawView();
    }

    @Override
    public void movetoInputs() {
        position =3;
        mStepsView.setCompletedPosition(position % labels.length).drawView();
        mPager.setCurrentItem(position);

    }

    @Override
    public void setInputsAttribute(int id) {

        workflowRunURL = "http://139.59.28.12:3000/workflows/"+id+"/runs/new";
        mPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showError() {
        Toast.makeText(this, "Server Error. Please try after sometime", Toast
                .LENGTH_LONG).show();
        finish();
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return PlayerLoginFragment.newInstance();
                case 1:
                    return DownloadingFragment.newInstance("Downloading Workflow");
                case 2:
                    return DownloadingFragment.newInstance("Uploading Workflow");
                case 3:
                    return WebViewGen.newInstance(workflowRunURL);
                default:
                    return DownloadingFragment.newInstance("Uploading1+ Workflow");

            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


}
