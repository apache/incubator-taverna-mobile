package org.apache.taverna.mobile.ui.workflow;


import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.data.model.Workflows;
import org.apache.taverna.mobile.ui.adapter.EndlessRecyclerOnScrollListener;
import org.apache.taverna.mobile.ui.adapter.WorkflowAdapter;
import org.apache.taverna.mobile.utils.ConnectionInfo;

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

public class WorkflowFragment extends Fragment implements WorkflowMvpView {
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

        showProgressbar(true);
        mWorkflowPresenter.loadAllWorkflow(mPageNumber);

        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                if (mConnectionInfo.isConnectingToInternet()
                        && mWorkflowList.size() % 10 == 0) {
                    mWorkflowList.add(null);
                    mWorkflowAdapter.notifyItemInserted(mWorkflowList.size());
                    ++mPageNumber;
                    mWorkflowPresenter.loadAllWorkflow(mPageNumber);
                    Log.i(LOG_TAG, "Loading more");
                } else if (!mConnectionInfo.isConnectingToInternet()) {
                    Log.i(LOG_TAG, "Internet not available. Not loading more posts.");
                    showErrorSnackBar();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mWorkflowPresenter.detachView();
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
}
