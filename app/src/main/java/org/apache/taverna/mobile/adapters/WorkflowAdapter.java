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
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thebuzzmedia.sjxp.rule.IRule;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.activities.WorkflowDetailActivity;
import org.apache.taverna.mobile.fragments.workflowdetails.WorkflowdetailFragment;
import org.apache.taverna.mobile.tavernamobile.User;
import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.apache.taverna.mobile.utils.Workflow_DB;
import org.apache.taverna.mobile.utils.xmlparsers.MyExperimentXmlParserRules;
import org.apache.taverna.mobile.utils.xmlparsers.WorkflowDetailParser;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Larry Akah on 6/8/15.
 */
public class WorkflowAdapter extends RecyclerView.Adapter<WorkflowAdapter.ViewHolder>{
    private Context context;
    private List<Workflow> workflowList; //workflow data to bind to the UI
    private WorkflowAdapter.ViewHolder mViewHolder;
    public static final String WORKFLOW_FAVORITE_KEY = "WORKFLOW_FAVORITES"; //workflow key used to save workflows when marked as favorites
    //public static final String WORKFLOW_FAVORITE = "favorited";//workflow key to favorited items tags
    public static final String FAVORITE_LIST_DB = "FAVORITE_LIST";
    public Workflow_DB favDB; //database to hold favorited workflows
    public Workflow_DB favoritedDb; //keep tags about favorited workflows

    public WorkflowAdapter(Context c, List<Workflow> wk) {
        context = c;
        workflowList = wk;
        favDB = new Workflow_DB(context, WORKFLOW_FAVORITE_KEY);
        favoritedDb = new Workflow_DB(context, WORKFLOW_FAVORITE_KEY);
    }

    public WorkflowAdapter(Context c){
        context = c;
        workflowList = new ArrayList<Workflow>();
        favDB = new Workflow_DB(context, WORKFLOW_FAVORITE_KEY);
    }

    public void addItems(List<Workflow> workflow, int position) throws ClassCastException{
        //add items to the current list of list
       //workflowList.add(position,workflow);
        workflowList.addAll(workflow);
        notifyItemRangeInserted(position + 24, 25);
    }

    public void removeItem(Workflow workflow){
        workflowList.remove(workflow);
        //notifyItemRemoved();
    }

    /**
     * Register a new observer to listen for data changes.
     * <p/>
     * <p>The adapter may publish a variety of events describing specific changes.
     * Not all adapters may support all change types and some may fall back to a generic
     * {@link android.support.v7.widget.RecyclerView.AdapterDataObserver#onChanged()
     * "something changed"} event if more specific data is not available.</p>
     * <p/>
     * <p>Components registering observers with an adapter are responsible for
     * {@link #unregisterAdapterDataObserver(android.support.v7.widget.RecyclerView.AdapterDataObserver)
     * unregistering} those observers when finished.</p>
     *
     * @param observer Observer to register
     * @see #unregisterAdapterDataObserver(android.support.v7.widget.RecyclerView.AdapterDataObserver)
     */
    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    /**
     * Unregister an observer currently listening for data changes.
     * <p/>
     * <p>The unregistered observer will no longer receive events about changes
     * to the adapter.</p>
     *
     * @param observer Observer to unregister
     * @see #registerAdapterDataObserver(android.support.v7.widget.RecyclerView.AdapterDataObserver)
     */
    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
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

        final long wid = workflowList.get(i).getId();
        final String author = workflowList.get(i).getWorkflow_author();
//        final String author = workflowList.get(i).getUploader().getName();
        final String title = workflowList.get(i).getWorkflow_title();
        String description  = workflowList.get(i).getWorkflow_description();
        final String uri = workflowList.get(i).getWorkflow_details_url();
        final String desc_full = description;

        if(description.length() > 80) description = description.substring(0, 79)+" ...";
        viewHolder.author_name.setHint(author);
        viewHolder.wk_title.setHint(title);
        viewHolder.wk_description.setText(description);
        Linkify.addLinks(viewHolder.wk_description, Linkify.WEB_URLS);

        final Intent it = new Intent();
        System.out.println("Workflow_uri:"+uri);
        it.setClass(context, WorkflowDetailActivity.class);
//        it.putExtra("workflowid", workflow.get(i).getId()); //workflow_url
        it.putExtra("uri",uri);//uri
        it.putExtra("wtitle", title); //pass this workflow's title to the detail activity so the corresponding run can be fetched
        it.putExtra("wid", wid);
        WorkflowdetailFragment.WORKFLO_ID = title;//workflow.get(i).getId();

            //determine whether to mark button as favorited or not
        /*
        final String favs = PreferenceManager.getDefaultSharedPreferences(context).getString(FAVORITE_LIST_DB, "");
            String[] ids = favs.split(",");
            if(ids.length > 0) {
                for (String id : ids)
                    if (id.equalsIgnoreCase("" + wid)){
                        viewHolder.btn_mark_workflow.setBackgroundResource(R.drawable.abc_list_selector_disabled_holo_light);
                        break;
                    }
            } */
        try {
            if (favoritedDb.get(String.valueOf(wid)).size() > 0) {
                viewHolder.btn_mark_workflow.setBackgroundResource(R.drawable.abc_list_selector_disabled_holo_light);
            }
        }catch (JSONException | NullPointerException ex){
            ex.printStackTrace();
        }

        viewHolder.btn_view_workflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(it);
                ((Activity) context).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
            }
        });

        viewHolder.btn_mark_workflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Object> mfav = new ArrayList<>();
                //save current workflow as favorite
                mfav.add(wid); mfav.add(author);mfav.add(title);mfav.add(desc_full);
                mfav.add(SimpleDateFormat.getDateTimeInstance().format(new Date()));
                mfav.add(uri);
                mfav.add(viewHolder.author_name.getText());
                int saved = favDB.insert(mfav);

                if(saved >0) {
                    viewHolder.btn_mark_workflow.setBackgroundResource(R.drawable.abc_list_selector_disabled_holo_light);
                    ArrayList<Object> flist = new ArrayList<Object>();
                    flist.add(wid);
                    favoritedDb.insert(flist);
                 //   PreferenceManager.getDefaultSharedPreferences(context).edit().putString(FAVORITE_LIST_DB, favs+wid+",").apply();
                    //refresh fragment since data has changed
                    FavoriteWorkflowAdapter favoriteWorkflowAdapter = (FavoriteWorkflowAdapter) ((RecyclerView) ((Activity) context).findViewById(R.id.favoriteList)).getAdapter();
                   //try {
                    if(null != favoriteWorkflowAdapter)
                       favoriteWorkflowAdapter.notifyDataSetChanged();
                   //}catch(NullPointerException np){
                   //     np.printStackTrace();
                  // }
                    Toast.makeText(context, "Workflow marked as favorite", Toast.LENGTH_SHORT).show();
                }else if(saved == -1){
                    Toast.makeText(context,"Sorry! This workflow has already been marked as a favourite",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(context,"Error! Please try again",Toast.LENGTH_SHORT).show();

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

        synchronized (this){
            new DetailLinkLoader(viewHolder).execute(uri, String.valueOf(i));
        }
    }

    public void setData(List<Workflow> workflowList){
        this.workflowList = workflowList;
    }
    @Override
    public long getItemId(int i) {
        return workflowList.get(i).getId();
    }

    @Override
    public int getItemCount() {
        return workflowList.size();
    }

    public Workflow getItem(int position){
        return workflowList.get(position);
    }

    public void addWorkflow(Workflow wk){
        workflowList.add(wk);
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

    /**
     * Loads partially details of a given workflow to retrieve author information
     */
    private class DetailLinkLoader extends AsyncTask<String, Void, Void>{
        ViewHolder mViewHolder;

        public DetailLinkLoader(ViewHolder vh){
            this.mViewHolder = vh;
        }

        @Override
        protected Void doInBackground(String ... strings) {
            URL url;
            HttpURLConnection connection;
            try {
                url = new URL(strings[0]); //fetch workflow detail
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                IRule avatarRule = new MyExperimentXmlParserRules.UploaderRule(IRule.Type.ATTRIBUTE,"/workflow/uploader", "resource","uri","id");
                IRule uploaderRule = new MyExperimentXmlParserRules.UploaderRule(IRule.Type.CHARACTER,"/workflow/uploader");
                WorkflowDetailParser detailMinParser = new WorkflowDetailParser(new IRule[]{avatarRule,uploaderRule});
                detailMinParser.parse(input, new User(strings[1], this.mViewHolder));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
}
