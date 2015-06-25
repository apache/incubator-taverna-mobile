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
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.activities.DashboardMainActivity;
import org.apache.taverna.mobile.activities.WorkflowDetailActivity;
import org.apache.taverna.mobile.fragments.workflowdetails.WorkflowdetailFragment;
import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.apache.taverna.mobile.utils.WorkflowDownloadManager;
import org.apache.taverna.mobile.utils.Workflow_DB;
import org.json.JSONException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Larry Akah on 6/8/15.
 */
public class WorkflowAdapter extends RecyclerView.Adapter<WorkflowAdapter.ViewHolder>{
    private Context context;
    private List<Workflow> workflow; //workflow data to bind to the UI
    private WorkflowAdapter.ViewHolder mViewHolder;
    public static final String WORKFLOW_FAVORITE_KEY = "WORKFLOW_FAVORITES"; //workflow key used to save workflows when marked as favorites
    public Workflow_DB favDB;

    public WorkflowAdapter(Context c, List<Workflow> wk) {
        context = c;
        workflow = wk;
        favDB = new Workflow_DB(context, WORKFLOW_FAVORITE_KEY);
    }

    public WorkflowAdapter(Context c){
        context = c;
        workflow = new ArrayList<Workflow>();
        favDB = new Workflow_DB(context, WORKFLOW_FAVORITE_KEY);
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

        final long wid = workflow.get(i).getId();
        final String author = workflow.get(i).getWorkflow_author();
        final String title = workflow.get(i).getWorkflow_title();
        String description  = workflow.get(i).getWorkflow_description();
        String uri = workflow.get(i).getWorkflow_details_url();
        final String desc_full = description;
        ArrayList<Object> mfav = new ArrayList<Object>();

        //save current workflow as favorite
            mfav.add(wid); mfav.add(author);mfav.add(title);mfav.add(desc_full); mfav.add(SimpleDateFormat.getDateTimeInstance().format(new Date()).toString());

//        if(description.length() > 80) description = description.substring(0, 79);
        viewHolder.author_name.setText(author);
        viewHolder.wk_title.setText(title);
        viewHolder.wk_description.setText( description+" ... ");
        Linkify.addLinks(viewHolder.wk_description, Linkify.WEB_URLS);
        final String wkflow_url = workflow.get(j).getWorkflow_remote_url();
        final Intent it = new Intent();
        it.setClass(context, WorkflowDetailActivity.class);
//        it.putExtra("workflowid", workflow.get(i).getId());
        it.putExtra("uri",uri);
        WorkflowdetailFragment.WORKFLO_ID = workflow.get(i).getId();

        viewHolder.btn_view_workflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent detailsIntent = new Intent(context, WorkflowDetailActivity.class);

                //detailsIntent.putExtras(null);
                context.startActivity(it);
                ((Activity) context).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
            }
        });
        /*viewHolder.btn_download_workflow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {

                    //String workflow_name = Uri.parse(wkflow_url).getLastPathSegment();
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
        });*/
        viewHolder.btn_mark_workflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean saved =  favDB.save();
                if(saved) {
                    Toast.makeText(context, "Workflow marked as favorite", Toast.LENGTH_SHORT).show();
                    viewHolder.btn_mark_workflow.setCompoundDrawables(context.getResources().getDrawable(android.R.drawable.btn_star_big_on),null,null,null);
                }else
                    Toast.makeText(context,"Error!, please try again",Toast.LENGTH_SHORT).show();
            }
        });
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
        try {
            favDB.put(mfav);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setData(List<Workflow> workflowList){
        this.workflow = workflowList;
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

    public void addWorkflow(Workflow wk){
        workflow.add(wk);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView author_profile;
        public final TextView author_name, wk_title,wk_showmore,wk_description;
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
           // wk_created = (TextView) v.findViewById(R.id.workflow_datecreated);
           // wk_modified = (TextView) v.findViewById(R.id.workflow_dateupdated);
            wk_description = (TextView) v.findViewById(R.id.workflow_brief_description);

            //cache buttons
            btn_download_workflow = (Button) v.findViewById(R.id.button_download_workflow);
            btn_mark_workflow = (Button) v.findViewById(R.id.button_mark_workflow);
            btn_view_workflow = (Button) v.findViewById(R.id.button_view_workflow);
        }
    }
}
