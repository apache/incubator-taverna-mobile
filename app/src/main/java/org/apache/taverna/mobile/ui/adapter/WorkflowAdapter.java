package org.apache.taverna.mobile.ui.adapter;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.model.DetailWorkflow;
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
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkflowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = WorkflowAdapter.class.getName();
    private final List<DetailWorkflow> detailWorkflowList;
    private final Context context;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    public WorkflowAdapter(List<DetailWorkflow> detailWorkflowList, Context context) {
        this.detailWorkflowList = detailWorkflowList;
        this.context = context;
    }

    public void addWorkflow(DetailWorkflow detailWorkflow) {
        this.detailWorkflowList.add(detailWorkflow);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
            Uri uri = Uri.parse(detailWorkflow.getSvgUri());
            requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .load(uri)
                    .into(((ViewHolder) holder).ivWorkflowImage);
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
        @BindView(R.id.ivWorkflowImage)
        ImageView ivWorkflowImage;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
