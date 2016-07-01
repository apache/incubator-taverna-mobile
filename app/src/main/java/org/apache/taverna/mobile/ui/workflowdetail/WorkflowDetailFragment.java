package org.apache.taverna.mobile.ui.workflowdetail;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.DetailWorkflow;
import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.utils.ConnectionInfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkflowDetailFragment extends Fragment implements WorkflowDetailMvpView {

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

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.scrollView)
    ScrollView mScrollView;

    @BindView(R.id.rootLayout)
    RelativeLayout rootLayout;

    private DataManager dataManager;

    private WorkflowDetailPresenter mWorkflowDetailPresenter;

    private ConnectionInfo mConnectionInfo;

    private static final String ID = "id";

    private String id;

    public static WorkflowDetailFragment newInstance(String id) {

        Bundle args = new Bundle();

        args.putString(ID, id);

        WorkflowDetailFragment fragment = new WorkflowDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getArguments().getString(ID);

        dataManager = new DataManager();
        mWorkflowDetailPresenter = new WorkflowDetailPresenter(dataManager);
        mConnectionInfo = new ConnectionInfo(getContext());
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

        if (mConnectionInfo.isConnectingToInternet()) {

            mWorkflowDetailPresenter.loadWorkflowDetail(id);
        } else {

            mProgressBar.setVisibility(View.GONE);
            showErrorSnackBar(getString(R.string.no_internet));
        }

        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_workflow_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.licence:

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
    public void showWorkflowDetail(DetailWorkflow detailWorkflow) {

        uploaderName.setText(detailWorkflow.getUploader().getContent());
        date.setText(detailWorkflow.getUpdatedAt());
        type.setText(detailWorkflow.getType().getContent());
        title.setText(detailWorkflow.getTitle());
        description.loadData(detailWorkflow.getDescription(), "text/html", "utf-8");

        Glide.with(getContext())
                .load(detailWorkflow.getPreviewUri())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(workflowImage);
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
    public void onDestroyView() {
        super.onDestroyView();
        mWorkflowDetailPresenter.detachView();
    }
}
