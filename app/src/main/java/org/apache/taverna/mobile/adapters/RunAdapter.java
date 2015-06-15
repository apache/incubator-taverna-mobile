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
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.tavernamobile.Runs;

import java.util.List;


/**
 * Created by root on 6/14/15.
 */
public class RunAdapter extends RecyclerView.Adapter<RunAdapter.RunHolder> {
    private Context context;
    private List<Runs> runList;

    public RunAdapter(Context context, List<Runs> runs) {
        this.context = context;
        this.runList = runs;
    }

    /**
     * Called when RecyclerView needs a new {@link android.support.v7.widget.RecyclerView.ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * . Since it will be re-used to display different
     * items in the data set, it is a good idea to cache references to sub views of the View to
     * avoid unnecessary {@link android.view.View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     */
    @Override
    public RunHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_run_item,parent, false);
        return new RunHolder(v);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link android.support.v7.widget.RecyclerView.ViewHolder#itemView} to reflect the item at
     * the given position.
     * <p/>
     * Note that unlike {@link android.widget.ListView}, RecyclerView will not call this
     * method again if the position of the item changes in the data set unless the item itself
     * is invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside this
     * method and should not keep a copy of it. If you need the position of an item later on
     * (e.g. in a click listener), use {@link android.support.v7.widget.RecyclerView.ViewHolder#getAdapterPosition()} which will have
     * the updated adapter position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RunHolder holder, int position) {
        Runs lRun = runList.get(position);
        holder.runtitle.setText(lRun.getRun_name());
        holder.runstarted.setText(lRun.getRun_started_date());
        holder.runfinished.setText(lRun.getRun_ended_date());

        switch(lRun.getState()){
            case RUNNING:
                holder.runStatus.setImageResource(android.R.drawable.presence_busy);
                holder.textState.setText("Running");
                break;
            case FINISHED:
                holder.runStatus.setImageResource(android.R.drawable.presence_online);
                holder.textState.setText("Finished");
                break;
            case FAILED:
                holder.runStatus.setImageResource(android.R.drawable.presence_offline);
                holder.textState.setText("Failed");
                break;
        }

    }

    public List<Runs> getRunList(){
        return this.runList;
    }

    public void setRunList(List<Runs> runList) {
        this.runList = runList;
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return runList.size();
    }

    public static class RunHolder extends RecyclerView.ViewHolder {
        public final TextView runtitle, runstarted, runfinished,textState;
        public final ImageButton runStatus;

        public RunHolder(View itemView) {
            super(itemView);
            runtitle = (TextView) itemView.findViewById(R.id.runtitle);
            runstarted = (TextView) itemView.findViewById(R.id.runstarted);
            runfinished = (TextView) itemView.findViewById(R.id.runfinished);
            runStatus = (ImageButton) itemView.findViewById(R.id.imageButtonState);
            textState = (TextView) itemView.findViewById(R.id.textState);
        }
    }
}
