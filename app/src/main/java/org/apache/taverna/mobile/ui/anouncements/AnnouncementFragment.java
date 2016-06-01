package org.apache.taverna.mobile.ui.anouncements;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.ui.adapter.AnnouncementAdapter;
import org.apache.taverna.mobile.ui.adapter.EndlessRecyclerOnScrollListener;
import org.apache.taverna.mobile.ui.adapter.RecyclerItemClickListner;
import org.apache.taverna.mobile.utils.ScrollChildSwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnnouncementFragment extends Fragment implements RecyclerItemClickListner.OnItemClickListener, AnnouncementMvpView {

    public  final String LOG_TAG = getClass().getSimpleName();

    @BindView(R.id.rv_movies) RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    ScrollChildSwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.progress_circular)
    ProgressBar mProgressBar;


    private Announcements mAnnouncements;
    private DataManager dataManager;
    private AnnouncementPresenter mMainPresenter;
    private AnnouncementAdapter mAnnouncementAdapter;
    private String category;
    private int mPageNumber = 1;

    @Override
    public void onItemClick(View childView, int position) {

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAnnouncements = new Announcements();
        dataManager = new DataManager();
        mMainPresenter = new AnnouncementPresenter(dataManager);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_announcement, container, false);
        ButterKnife.bind(this, rootView);
        mMainPresenter.attachView(this);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(ab!=null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListner(getActivity(), this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent, R.color.colorPrimary);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    if (mSwipeRefresh.isRefreshing()) {
                        mPageNumber = 1;
                        mMainPresenter.loadAllAnnouncement(mPageNumber);
                        Log.i(LOG_TAG, "Swipe Refresh");
                    }
                }
                else {
                    Log.i(LOG_TAG, "NO Internet Connection");
                    if (mSwipeRefresh.isRefreshing()) {
                        mSwipeRefresh.setRefreshing(false);
                    }
                }

            }
        });


        mMainPresenter.loadAllAnnouncement(mPageNumber);

        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected())
                {
                    mAnnouncements.getAnnouncement().add(null);
                    mAnnouncementAdapter.notifyItemInserted(mAnnouncements.getAnnouncement().size());
                    mPageNumber = ++mPageNumber;
                    mMainPresenter.loadAllAnnouncement(mPageNumber);
                    Log.i(LOG_TAG, "Loading more");
                }
                else
                {
                    Log.i(LOG_TAG, "Internet not available. Not loading more posts.");
                }
            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMainPresenter.detachView();
    }



    @Override
    public void showAllAnouncement(Announcements announcements) {
        if(mPageNumber == 1){
            mAnnouncements = announcements;
            mAnnouncementAdapter = new AnnouncementAdapter(mAnnouncements.getAnnouncement());
            mRecyclerView.setAdapter(mAnnouncementAdapter);
        }else {
            mAnnouncements.getAnnouncement().remove(mAnnouncements.getAnnouncement().size()-1);
            mAnnouncements.getAnnouncement().addAll(announcements.getAnnouncement());
        }

        mRecyclerView.setVisibility(View.VISIBLE);
        mAnnouncementAdapter.notifyDataSetChanged();
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showProgressbar(boolean status) {
        if (status){
            mProgressBar.setVisibility(View.VISIBLE);
        }else
            mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}