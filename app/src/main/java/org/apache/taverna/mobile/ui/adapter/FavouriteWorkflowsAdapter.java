package org.apache.taverna.mobile.ui.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.model.Workflow;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteWorkflowsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = FavouriteWorkflowsAdapter.class.getName();

    private final List<Workflow> mWorkflowList;

    private final Context context;

    public FavouriteWorkflowsAdapter(List<Workflow> mWorkflowList, Context context) {
        this.mWorkflowList = mWorkflowList;
        this.context = context;

    }

    public void addWorkflow(Workflow workflow) {
        this.mWorkflowList.add(workflow);
        this.notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recyclerview_favourite_workflow_list, parent, false);
        vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

            Workflow workflow = mWorkflowList.get(position);
            String date = workflow.getCreatedAt()
                    .substring(0, workflow.getCreatedAt().indexOf(' '));

            String title = workflow.getTitle().substring(0,
                    Math.min(workflow.getTitle().length(), 50));

            if (workflow.getTitle().length() > 50) {
                title = title + " ... ";
            }

            ((ViewHolder) holder).tvDate.setText(date);
            ((ViewHolder) holder).tvTitle.setText(title);
            ((ViewHolder) holder).tvType.setText(workflow.getType().getContent());
            ((ViewHolder) holder).tvUploader.setText(workflow.getUploader().getContent());

            Uri uri = Uri.parse(workflow.getPreviewUri());

            Glide.with(context)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(((ViewHolder) holder).ivWorkflowImage);
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + mWorkflowList.size());
        return mWorkflowList.size();
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

        @BindView(R.id.ivWorkflowImage)
        ImageView ivWorkflowImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
