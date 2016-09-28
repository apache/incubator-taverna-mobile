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
package org.apache.taverna.mobile.ui.favouriteworkflowdetail;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.License;
import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.ui.imagezoom.ImageZoomActivity;
import org.apache.taverna.mobile.ui.imagezoom.ImageZoomFragment;
import org.apache.taverna.mobile.utils.ConnectionInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavouriteWorkflowDetailFragment extends Fragment
        implements FavouriteWorkflowDetailMvpView {

    private static final String ID = "id";

    public final String LOG_TAG = getClass().getSimpleName();

    @BindView(R.id.ivWorkflowImage)
    ImageView workflowImage;

    @BindView(R.id.tvTitle)
    TextView title;

    @BindView(R.id.ivUploader)
    ImageView uploaderImage;

    @BindView(R.id.tvUploaderName)
    TextView uploaderName;

    @BindView(R.id.tvDate)
    TextView date;

    @BindView(R.id.tvType)
    TextView type;

    @BindView(R.id.tvDescription)
    WebView description;

    @BindView(R.id.ivFav)
    ImageView ivFavourite;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.scrollView)
    ScrollView mScrollView;

    @BindView(R.id.rootLayout)
    RelativeLayout rootLayout;

    @BindView(R.id.fabRun)
    FloatingActionButton fabRun;

    private AlertDialog alertDialog;

    private DataManager dataManager;

    private FavouriteWorkflowDetailPresenter mWorkflowDetailPresenter;

    private String id;

    private String licenceId = null;

    private ProgressDialog dialog;

    private ActionBar actionBar;

    private Workflow mWorkflow;

    public static FavouriteWorkflowDetailFragment newInstance(String id) {

        Bundle args = new Bundle();

        args.putString(ID, id);

        FavouriteWorkflowDetailFragment fragment = new FavouriteWorkflowDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getArguments().getString(ID);

        dataManager = new DataManager();
        mWorkflowDetailPresenter = new FavouriteWorkflowDetailPresenter(dataManager);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_workflow, container, false);

        ButterKnife.bind(this, rootView);

        mWorkflowDetailPresenter.attachView(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (ConnectionInfo.isConnectingToInternet(getContext())) {

            mWorkflowDetailPresenter.loadWorkflowDetail(id);
        } else {

            mProgressBar.setVisibility(View.GONE);
            showErrorSnackBar(getString(R.string.no_internet));
        }

        setHasOptionsMenu(true);


    }

    @OnClick(R.id.ivFav)
    void favClick(View v) {
        mWorkflowDetailPresenter.setFavourite(id);
    }

    @OnClick(R.id.ivWorkflowImage)
    void zoomImage(View v) {

        Intent intent = new Intent(getActivity(), ImageZoomActivity.class);

        intent.putExtra(ImageZoomFragment.JPG_URI, mWorkflow.getPreviewUri());
        intent.putExtra(ImageZoomFragment.SVG_URI, mWorkflow.getSvgUri());

        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_workflow_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.licence:

                if (licenceId == null) {

                    showErrorSnackBar("Please wait");
                } else if (licenceId.isEmpty()) {

                    showErrorSnackBar("No Licence Found");
                } else {

                    mWorkflowDetailPresenter.loadLicenseDetail(licenceId);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void showProgressbar(boolean b) {
        if (b) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showWorkflowDetail(Workflow workflow) {

        this.mWorkflow = workflow;

        uploaderName.setText(workflow.getUploader().getContent());
        date.setText(workflow.getUpdatedAt()
                .substring(0, workflow.getUpdatedAt().indexOf(' ')));
        type.setText(workflow.getType().getContent());
        title.setText(workflow.getTitle());
        description.loadData(workflow.getDescription(), "text/html", "utf-8");

        Glide.with(getContext())
                .load(workflow.getPreviewUri())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(workflowImage);

        if (workflow.getLicenseType().getId() == null) {
            licenceId = "";
        } else {
            licenceId = workflow.getLicenseType().getId();
        }

        if (mWorkflow.getType().getContent().equals("Taverna 2")) {
            fabRun.setVisibility(View.VISIBLE);
        }else {
            fabRun.setVisibility(View.GONE);
        }

    }

    @Override
    public void setImage(User user) {

        Glide.with(getContext())
                .load(user.getAvatar().getResource())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(uploaderImage);

    }

    @Override
    public void showErrorSnackBar(String error) {

        final Snackbar snackbar = Snackbar.make(rootLayout, error, Snackbar
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
    public void showLicense(License license) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_licence_detail_workflow, null);

        dialogBuilder.setView(dialogView);

        TextView title = ButterKnife.findById(dialogView, R.id.tvDialogTitle);
        TextView date = ButterKnife.findById(dialogView, R.id.tvDialogDate);
        WebView text = ButterKnife.findById(dialogView, R.id.wvDialogText);
        Button buttonOk = ButterKnife.findById(dialogView, R.id.bDialogOK);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        text.loadDataWithBaseURL("", license.getDescription(), "text/html", "utf-8", "");
        date.setText(license.getCreatedAt().substring(0, license.getCreatedAt().indexOf(' ')));
        title.setText(license.getTitle());

        alertDialog = dialogBuilder.create();

        alertDialog.show();
    }

    @Override
    public void showLicenseProgress(boolean b) {
        if (b) {
            dialog = ProgressDialog.show(getContext(), "Loading", "Please wait...", true, true);
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public void setFavouriteIcon() {
        mWorkflowDetailPresenter.getFavourite(id);
    }

    @Override
    public void getFavouriteIcon(boolean b) {
        if (b) {
            ivFavourite.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            ivFavourite.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mWorkflowDetailPresenter.detachView();
    }
}
