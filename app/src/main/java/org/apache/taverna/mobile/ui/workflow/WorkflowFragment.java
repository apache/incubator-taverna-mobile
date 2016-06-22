package org.apache.taverna.mobile.ui.workflow;


import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.DetailWorkflow;
import org.apache.taverna.mobile.ui.adapter.WorkflowAdapter;
import org.apache.taverna.mobile.utils.ConnectionInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_circular)
    ProgressBar mProgressBar;

    private DataManager dataManager;

    private WorkflowPresenter mWorkflowPresenter;

    private WorkflowAdapter mWorkflowAdapter;

    private ConnectionInfo mConnectionInfo;
    private int mPageNumber = 0;
    private List<DetailWorkflow> detailWorkflowList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailWorkflowList = new ArrayList<>();
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
        showProgressbar(true);
        mRecyclerView.hasFixedSize();
        mWorkflowAdapter = new WorkflowAdapter(detailWorkflowList, getContext());
        mRecyclerView.setAdapter(mWorkflowAdapter);
        mWorkflowPresenter.loadAllWorkflow(mPageNumber);
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
    public void showWorkflowDetail(DetailWorkflow detailWorkflow) {
        detailWorkflowList.add(detailWorkflow);
        mWorkflowAdapter.notifyDataSetChanged();
    }
}
