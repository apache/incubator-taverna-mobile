package org.apache.taverna.mobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.taverna.mobile.R;

import java.util.List;

/**
 * Created by root on 6/9/15.
 */
public class FavoriteWorkflowAdapter extends RecyclerView.Adapter<FavoriteWorkflowAdapter.FViewHolder> {

    private Context context;
    private List<String[]> dataSet;

    public FavoriteWorkflowAdapter(Context c, List<String[]> data) {
        context = c;
        dataSet = data;
    }

    @Override
    public FViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.favorite_item_layout, viewGroup, false);
        FViewHolder vh = new FViewHolder(itemview);
        return vh;
    }

    @Override
    public void onBindViewHolder(FViewHolder fViewHolder, int i) {
        String[] mdata = dataSet.get(i);
        fViewHolder.author.setText(mdata[0]);
        fViewHolder.title.setText(mdata[1]);
        fViewHolder.dateMarked.setText(mdata[2]);
        fViewHolder.dateAdd.setText(mdata[3]);
        fViewHolder.dateModified.setText(mdata[4]);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class FViewHolder extends RecyclerView.ViewHolder {

        public final ImageView favorite_thumb;
        public final TextView author, title, dateMarked, dateAdd, dateModified;
        public FViewHolder(View itemView) {
            super(itemView);
            favorite_thumb = (ImageView) itemView.findViewById(R.id.author_profile_image);
            author = (TextView) itemView.findViewById(R.id.author);
            title = (TextView) itemView.findViewById(R.id.favorite_title);
            dateMarked = (TextView) itemView.findViewById(R.id.date_set);
            dateAdd = (TextView) itemView.findViewById(R.id.date_created);
            dateModified = (TextView) itemView.findViewById(R.id.date_modified);
        }
    }
}
