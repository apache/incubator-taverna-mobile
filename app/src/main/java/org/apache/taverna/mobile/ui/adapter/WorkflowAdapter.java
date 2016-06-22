package org.apache.taverna.mobile.ui.adapter;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.model.DetailWorkflow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkflowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = WorkflowAdapter.class.getName();
    private final List<DetailWorkflow> detailWorkflowList;
    private final Context context;

    public WorkflowAdapter(List<DetailWorkflow> detailWorkflowList, Context context) {
        this.detailWorkflowList = detailWorkflowList;
        this.context = context;
    }

    public void addWorkflow(DetailWorkflow detailWorkflow) {
        this.detailWorkflowList.add(detailWorkflow);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recyclerview_dashboard, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            DetailWorkflow detailWorkflow = detailWorkflowList.get(position);
            ((ViewHolder) holder).tvDate.setText(detailWorkflow.getCreatedAt());
            ((ViewHolder) holder).tvTitle.setText(detailWorkflow.getTitle());
            ((ViewHolder) holder).tvType.setText(detailWorkflow.getType().getContent());
            ((ViewHolder) holder).tvUploader.setText(detailWorkflow.getUploader().getContent());
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + detailWorkflowList.size());
        return detailWorkflowList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvUploader)
        TextView tvUploader;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
