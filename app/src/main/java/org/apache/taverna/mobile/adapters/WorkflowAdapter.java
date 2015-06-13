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
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.activities.DashboardMainActivity;
import org.apache.taverna.mobile.activities.WorkflowDetailActivity;
import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.apache.taverna.mobile.utils.WorkflowDownloadManager;

import java.io.File;
import java.util.List;

/**
 * Created by root on 6/8/15.
 */
public class WorkflowAdapter extends RecyclerView.Adapter<WorkflowAdapter.ViewHolder> implements View.OnClickListener{
    private Context context;
    private List<Workflow> workflow;
    private WorkflowAdapter.ViewHolder mViewHolder;

    public WorkflowAdapter(Context c, List<Workflow> wk) {
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
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final int j = i; //position of workflow item that has workflow data
        final Context c = this.context;
        viewHolder.author_name.setText(workflow.get(i).getWorkflow_author());
        viewHolder.wk_title.setText(workflow.get(i).getWorkflow_title());
        viewHolder.wk_modified.append(workflow.get(i).getWorkflow_datemodified());
        viewHolder.wk_created.append(workflow.get(i).getWorkflow_datecreated());
        viewHolder.wk_description.setText( workflow.get(i).getWorkflow_description());
        //viewHolder.author_profile.setImageBitmap(workflow[i].getWorkflow_author_bitmap());
        Intent it = new Intent();
        viewHolder.btn_view_workflow.setOnClickListener(this);
        viewHolder.btn_download_workflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wkflow_url = workflow.get(j).getWorkflow_remote_url();
                try {

                    String workflow_name = Uri.parse(wkflow_url).getLastPathSegment();
                    WorkflowDownloadManager dm = new WorkflowDownloadManager(c);
                    File destinationFile = new File(PreferenceManager.getDefaultSharedPreferences(c)
                            .getString(DashboardMainActivity.APP_DIRECTORY_NAME, "/"));
                    Log.i("Workflow Name ", destinationFile.getAbsolutePath());
                    dm.downloadWorkflow(destinationFile, wkflow_url);
                } catch(NullPointerException np){
                    np.printStackTrace();
                }catch (IllegalArgumentException ill){
                    ill.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        viewHolder.btn_mark_workflow.setOnClickListener(this);
        viewHolder.wk_showmore.setText(Html.fromHtml(context.getResources().getString(R.string.seemore)));
        viewHolder.wk_showmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.infolayout.getVisibility() == View.GONE)
                    viewHolder.infolayout.setVisibility(View.VISIBLE);
                else
                    viewHolder.infolayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return workflow.get(i).getId();
    }

    @Override
    public int getItemCount() {
        return workflow.size();
    }

    public Workflow getItem(int position){
        return workflow.get(position);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_view_workflow:
                context.startActivity(new Intent(context, WorkflowDetailActivity.class));
                ((Activity) context).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                break;
            case R.id.button_mark_workflow:
                break;

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView author_profile;
        public final TextView author_name, wk_title,wk_showmore,wk_created,wk_modified,wk_description;
        public final Button btn_view_workflow;
        public final Button btn_download_workflow;
        public final Button btn_mark_workflow;
        public final LinearLayout infolayout;

        public ViewHolder(View v) {
            super(v);
            infolayout = (LinearLayout) v.findViewById(R.id.layoutinfo);
            //cache text fields
            author_profile = (ImageView) v.findViewById(R.id.author_profile_image);
            author_name = (TextView) v.findViewById(R.id.workflow_author);
            wk_title = (TextView) v.findViewById(R.id.workflow_title);
            wk_showmore = (TextView) v.findViewById(R.id.show_more);
            wk_created = (TextView) v.findViewById(R.id.workflow_datecreated);
            wk_modified = (TextView) v.findViewById(R.id.workflow_dateupdated);
            wk_description = (TextView) v.findViewById(R.id.workflow_brief_description);

            //cache buttons
            btn_download_workflow = (Button) v.findViewById(R.id.button_download_workflow);
            btn_mark_workflow = (Button) v.findViewById(R.id.button_mark_workflow);
            btn_view_workflow = (Button) v.findViewById(R.id.button_view_workflow);
        }
    }
}
