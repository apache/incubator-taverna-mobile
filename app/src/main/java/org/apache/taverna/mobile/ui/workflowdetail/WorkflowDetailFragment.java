package org.apache.taverna.mobile.ui.workflowdetail;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.DetailWorkflow;
import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.utils.ConnectionInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
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
        mWorkflowDetailPresenter.loadWorkflowDetail(id);

        return rootView;
    }

    @Override
    public void showProgressbar(boolean b) {

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
    public void onDestroyView() {
        super.onDestroyView();
        mWorkflowDetailPresenter.detachView();
    }
}
