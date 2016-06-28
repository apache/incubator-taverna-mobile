package org.apache.taverna.mobile.ui.adapter;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.utils.SvgDecoder;
import org.apache.taverna.mobile.utils.SvgDrawableTranscoder;
import org.apache.taverna.mobile.utils.SvgSoftwareLayerSetter;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkflowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private static final String TAG = WorkflowAdapter.class.getName();

    private final List<Workflow> mWorkflowList;

    private final Context context;

    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    public WorkflowAdapter(List<Workflow> mWorkflowList, Context context) {
        this.mWorkflowList = mWorkflowList;
        this.context = context;

        requestBuilder = Glide.with(context)
                .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .placeholder(R.drawable.tavernalogo)
                .error(R.drawable.tavernalogo)
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());
    }

    public void addWorkflow(Workflow workflow) {
        this.mWorkflowList.add(workflow);
        this.notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_recyclerview_dashboard, parent, false);

            vh = new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_progressbar, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

            Workflow workflow = mWorkflowList.get(position);
            String date = workflow.getCreatedAt()
                    .substring(0, workflow.getCreatedAt().indexOf(' '));

            ((ViewHolder) holder).tvDate.setText(date);
            ((ViewHolder) holder).tvTitle.setText(workflow.getTitle());
            ((ViewHolder) holder).tvType.setText(workflow.getType().getContent());
            ((ViewHolder) holder).tvUploader.setText(workflow.getUploader().getContent());

            Uri uri = Uri.parse(workflow.getSvgUri());

            requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .load(uri)
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

    @Override
    public int getItemViewType(int position) {
        return mWorkflowList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
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

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progressBar1)
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
