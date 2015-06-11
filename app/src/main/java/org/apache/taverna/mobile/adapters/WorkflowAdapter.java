package org.apache.taverna.mobile.adapters;
/**
 * Apache Taverna Mobile
 * Copyright 2015 The Apache Software Foundation

 * This product includes software developed at
 * The Apache Software Foundation (http://www.apache.org/).

 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.activities.WorkflowDetailActivity;
import org.apache.taverna.mobile.utils.Workflow;

/**
 * Created by root on 6/8/15.
 */
public class WorkflowAdapter extends RecyclerView.Adapter<WorkflowAdapter.ViewHolder> implements View.OnClickListener{
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
        //viewHolder.author_profile.setImageBitmap(workflow[i].getWorkflow_author_bitmap());
        viewHolder.btn_view_workflow.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_view_workflow:
                context.startActivity(new Intent(context, WorkflowDetailActivity.class));
                ((Activity) context).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView author_profile;
        public final TextView author_name;
        public final Button btn_view_workflow;

        public ViewHolder(View v) {
            super(v);
            author_profile = (ImageView) v.findViewById(R.id.author_profile_image);
            author_name = (TextView) v.findViewById(R.id.workflow_author);
            btn_view_workflow = (Button) v.findViewById(R.id.button_view_workflow);
        }
    }
}
