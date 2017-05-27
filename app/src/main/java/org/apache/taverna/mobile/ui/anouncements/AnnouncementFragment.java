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
package org.apache.taverna.mobile.ui.anouncements;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.data.model.DetailAnnouncement;
import org.apache.taverna.mobile.ui.adapter.AnnouncementAdapter;
import org.apache.taverna.mobile.ui.adapter.EndlessRecyclerOnScrollListener;
import org.apache.taverna.mobile.ui.adapter.RecyclerItemClickListner;
import org.apache.taverna.mobile.utils.ConnectionInfo;
import org.apache.taverna.mobile.utils.ScrollChildSwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AnnouncementFragment extends Fragment implements RecyclerItemClickListner
        .OnItemClickListener, AnnouncementMvpView {

    public final String LOG_TAG = getClass().getSimpleName();

    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh)
    ScrollChildSwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.progress_circular)
    ProgressBar mProgressBar;

    private AlertDialog alertDialog;

    private ProgressDialog dialog;

    private Announcements mAnnouncements;

    private DataManager dataManager;

    private AnnouncementPresenter mAnnouncementPresenter;

    private AnnouncementAdapter mAnnouncementAdapter;

    private int mPageNumber = 1;

    private DetailAnnouncement mAnnouncementDetail;


    @Override
    public void onItemClick(View childView, int position) {
        if (mAnnouncements.getAnnouncement().get(position) != null && position != -1) {
            showWaitProgress(true);
            mAnnouncementPresenter.loadAnnouncementDetails(mAnnouncements.getAnnouncement()
                    .get(position).getId());
        }
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAnnouncements = new Announcements();
        dataManager = new DataManager();
        mAnnouncementPresenter = new AnnouncementPresenter(dataManager);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_announcement, container, false);
        ButterKnife.bind(this, rootView);
        mAnnouncementPresenter.attachView(this);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListner(getActivity(), this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent, R.color
                .colorPrimary);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ConnectionInfo.isConnectingToInternet(getContext())) {
                    if (mSwipeRefresh.isRefreshing()) {
                        mPageNumber = 1;
                        mAnnouncementPresenter.loadAllAnnouncement(mPageNumber);
                        Log.i(LOG_TAG, "Swipe Refresh");
                    }
                } else {
                    Log.i(LOG_TAG, "NO Internet Connection");
                    showSnackBar(R.string.no_internet_connection);
                    if (mSwipeRefresh.isRefreshing()) {
                        mSwipeRefresh.setRefreshing(false);
                    }
                }

            }
        });

        showProgressbar(true);
        mAnnouncementPresenter.loadAllAnnouncement(mPageNumber);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                if (ConnectionInfo.isConnectingToInternet(getContext())) {
                    mAnnouncements.getAnnouncement().add(null);
                    mAnnouncementAdapter.notifyItemInserted(mAnnouncements.getAnnouncement().size
                            ());
                    mPageNumber = ++mPageNumber;
                    mAnnouncementPresenter.loadAllAnnouncement(mPageNumber);
                    Log.i(LOG_TAG, "Loading more");
                } else {
                    Log.i(LOG_TAG, "Internet not available. Not loading more posts.");
                    showSnackBar(R.string.no_internet_connection);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAnnouncementPresenter.detachView();
    }


    @Override
    public void showAllAnnouncement(Announcements announcements) {
        if (mPageNumber == 1) {
            mAnnouncements = announcements;
            mAnnouncementAdapter = new AnnouncementAdapter(mAnnouncements.getAnnouncement());
            mRecyclerView.setAdapter(mAnnouncementAdapter);
        } else {
            removeLoadMoreProgressBar();
            mAnnouncements.getAnnouncement().addAll(announcements.getAnnouncement());
        }

        mRecyclerView.setVisibility(View.VISIBLE);
        mAnnouncementAdapter.notifyDataSetChanged();
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void removeLoadMoreProgressBar() {
        mAnnouncements.getAnnouncement().remove(mAnnouncements.getAnnouncement().size() - 1);
        mAnnouncementAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressbar(boolean status) {
        if (status) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showAnnouncementDetail(DetailAnnouncement detailAnnouncement) {
        mAnnouncementDetail = detailAnnouncement;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.detail_annoucement_dialog_layout, null);
        dialogBuilder.setView(dialogView);
        TextView title = ButterKnife.findById(dialogView, R.id.tvDialogTitle);
        TextView date = ButterKnife.findById(dialogView, R.id.tvDialogDate);
        TextView author = ButterKnife.findById(dialogView, R.id.tvDialogAuthor);
        WebView text = ButterKnife.findById(dialogView, R.id.wvDialogText);
        Button buttonOk = ButterKnife.findById(dialogView, R.id.bDialogOK);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        text.loadDataWithBaseURL("", mAnnouncementDetail.getText(), "text/html", "utf-8", "");
        date.setText(mAnnouncementDetail.getDate());
        title.setText(mAnnouncementDetail.getTitle());
        author.setText(mAnnouncementDetail.getAuthor().getContent());
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showSnackBar(int message) {
        final Snackbar snackbar = Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_LONG);
        snackbar.setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }

    @Override
    public void showWaitProgress(boolean b) {
        if (b) {
            dialog = ProgressDialog.show(getContext(), "Loading", "Please wait...", true);
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }
}