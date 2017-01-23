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


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anton46.stepsview.StepsView;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.local.PreferencesHelper;
import org.apache.taverna.mobile.data.model.Inputs;
import org.apache.taverna.mobile.ui.DownloadingFragment;
import org.apache.taverna.mobile.ui.tavernaserver.createrun.TavernaServerCreateRunFragment;
import org.apache.taverna.mobile.ui.tavernaserver.inputs.TavernaServerInputsFragment;
import org.apache.taverna.mobile.utils.Constants;
import org.apache.taverna.mobile.utils.NonSwipeableViewPager;
import org.apache.taverna.mobile.utils.WebViewGenerator;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.fragment;
import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class WorkflowRunActivity extends FragmentActivity implements WorkflowRunMvpView,
        TavernaServerCreateRunFragment.OnSuccessful, TavernaServerInputsFragment.OnFragmentInteractionListener {



    @BindView(R.id.stepsView)
    StepsView mStepsView;

    @BindView(R.id.viewpager)
    NonSwipeableViewPager mPager;

    int position = 0;

    private String[] labels;
    private String workflowRunURL;

    private DataManager dataManager;

    private WorkflowRunPresenter mWorkflowRunPresenter;

    private PagerAdapter mPagerAdapter;

    private TavernaServerInputsFragment tavernaServerInputsFragment;

    private WeakReference<Fragment> createRunsFragment;
    private WeakReference<Fragment> inputsFragment;
    private WeakReference<Fragment> downloadingFragment;
    private WeakReference<Fragment> webviewFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workflow_run);

        ButterKnife.bind(this);

        dataManager = new DataManager(new PreferencesHelper(getContext()));

        mWorkflowRunPresenter = new WorkflowRunPresenter(dataManager);

        mWorkflowRunPresenter.attachView(this);

        labels = getResources().getStringArray(R.array.player_run_slider_view_labels);

        mStepsView.setCompletedPosition(position % labels.length)
                .setLabels(labels)
                .setBarColorIndicator(
                        getContext().getResources().getColor(R.color.material_blue_grey_800))
                .setProgressColorIndicator(getContext().getResources().getColor(R.color
                        .colorPrimary))
                .setLabelColorIndicator(getContext().getResources().getColor(R.color.colorPrimary))
                .drawView();


        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

//        if (dataManager.getPreferencesHelper().isUserPlayerLoggedInFlag()) {
//            mPager.setCurrentItem(++position);
//            mStepsView.setCompletedPosition(position % labels.length).drawView();
//
//            mWorkflowRunPresenter.runWorkflow(getIntent().getStringExtra(Constants.WORKFLOW_URL));
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWorkflowRunPresenter.detachView();
    }

    /**
     * Get the URL location of the run and start monitoring it
     * @param runLocation The URL on the server of the run
     */
    @Override
    public void onRunStart(String runLocation) {
        position = 1;
        mPager.setCurrentItem(position);
        mStepsView.setCompletedPosition(position % labels.length).drawView();
        getIntent().putExtra(runLocation, Constants.RUN_LOCATION);
        String password = getIntent().getStringExtra(Constants.SERVER_PASS);
        String username = getIntent().getStringExtra(Constants.SERVER_USER);
        ((TavernaServerInputsFragment)inputsFragment.get()).fetchInputs(username, password, runLocation);

        //mWorkflowRunPresenter.runWorkflow(getIntent().getStringExtra(Constants.WORKFLOW_URL));
        //mWorkflowRunPresenter.runWorkflow(runID);
    }

    @Override
    public void movetoUploadWorkflow() {
        position = 2;
        mPager.setCurrentItem(position);
        mStepsView.setCompletedPosition(position % labels.length).drawView();
    }

    @Override
    public void moveToInputs() {
        position = 3;
        mStepsView.setCompletedPosition(position % labels.length).drawView();
        mPager.setCurrentItem(position);
    }

    @Override
    public void setInputs(Inputs inputs) {
        //Update the view or something?
    }

    @Override
    public void setInputsAttribute(int id) {
        String playerURL = dataManager.getPreferencesHelper().getPlayerURL();

        if (playerURL.trim().endsWith("/")) {
            playerURL = playerURL.substring(0, playerURL.length() - 1);
        }
        workflowRunURL = playerURL + "/workflows/" + id +
                "/runs/new";
        mPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showError() {
        Toast.makeText(this, getString(R.string.servererr), Toast
                .LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onFragmentInteraction(Inputs inputs) {
        //TODO something when the inputs are set
        System.out.println("Inputs returned to run activity");
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TavernaServerCreateRunFragment.newInstance();
                case 1:
                    return TavernaServerInputsFragment.newInstance();
                case 2:
                    return DownloadingFragment.newInstance(getString(R.string
                            .uploading_workflow_lable));
                default:
                    return WebViewGenerator.newInstance(workflowRunURL);

            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // It's fairly easy to communicate from fragment to activity but
            // the other way is tricky. This is the 'best' way I found to do
            // it. See http://stackoverflow.com/questions/14035090/how-to-get-existing-fragments-when-using-fragmentpageradapter
            // Really Android, you should be able to do this in a simpler way
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    createRunsFragment = new WeakReference<Fragment>(createdFragment);
                    break;
                case 1:
                    inputsFragment = new WeakReference<Fragment>(createdFragment);
                    break;
                case 2:
                    downloadingFragment = new WeakReference<Fragment>(createdFragment);
                    break;
                case 3:
                    webviewFragment = new WeakReference<Fragment>(createdFragment);
            }
            return createdFragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public interface RunIDListener {
        void onRunLocation();
    }


}
