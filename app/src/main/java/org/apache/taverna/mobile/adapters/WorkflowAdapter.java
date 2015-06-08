package org.apache.taverna.mobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.utils.Workflow;

/**
 * Created by root on 6/8/15.
 */
public class WorkflowAdapter extends RecyclerView.Adapter<WorkflowAdapter.ViewHolder>{
    private Context context;
    private Workflow[] workflow;
    private WorkflowAdapter.ViewHolder mViewHolder;
    public WorkflowAdapter(Context c, Workflow[] wk) {
        context = c;
        workflow = wk;
    }

    @Override
    public WorkflowAdapter.ViewHolder onCreateViewHolder(ViewGroup parentViewGroup, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_item_layout,parentViewGroup,false);
        mViewHolder = new ViewHolder(v);
        return mViewHolder;
    }

    /**
     * Bind data set items for each data
     * @param viewHolder the recycled view used to bind data (Overwrite data values)
     * @param i position of data in the dataset to use.
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.author_name.setText(workflow[i].getWorkflow_author());
        viewHolder.author_profile.setImageBitmap(workflow[i].getWorkflow_author_bitmap());
    }

    @Override
    public long getItemId(int i) {
        return workflow[i].getid();
    }

    @Override
    public int getItemCount() {
        int size = 0;
        for(Workflow w: workflow){
            size++;
        }
        return size;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View workflow_root  = LayoutInflater.from(context).inflate(R.layout.workflow_item_layout, viewGroup,false);

        return workflow_root;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView author_profile;
        public final TextView author_name;

        public ViewHolder(View v) {
            super(v);
            author_profile = (ImageView) v.findViewById(R.id.author_profile_image);
            author_name = (TextView) v.findViewById(R.id.workflow_author);
        }
    }
}
