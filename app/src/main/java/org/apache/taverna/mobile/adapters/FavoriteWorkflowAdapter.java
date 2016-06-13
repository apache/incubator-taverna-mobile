package org.apache.taverna.mobile.adapters;
/**
 * Apache Taverna Mobile
 * Copyright 2015 The Apache Software Foundation
 *
 * This product includes software developed at
 * The Apache Software Foundation (http://www.apache.org/).
 *
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

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.utils.WorkflowDB;
import org.json.JSONException;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Larry Akah on 6/9/15.
 */
public class FavoriteWorkflowAdapter extends RecyclerView.Adapter<FavoriteWorkflowAdapter
        .FViewHolder> {
    private static final String TAG = "FavoriteWorkflowAdapter";
    public WorkflowDB favDB;
    private Context context;
    private List<ArrayList<Object>> dataSet;

    public FavoriteWorkflowAdapter(Context c, List<ArrayList<Object>> data) {
        context = c;
        dataSet = data;
        favDB = new WorkflowDB(context, WorkflowAdapter.WORKFLOW_FAVORITE_KEY);
    }

    @Override
    public FViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.favorite_item_layout,
                viewGroup, false);
        FViewHolder vh = new FViewHolder(itemview);
        return vh;
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
     * {@link #unregisterAdapterDataObserver(android.support.v7.widget.RecyclerView
     * .AdapterDataObserver)
     * unregistering} those observers when finished.</p>
     *
     * @param observer Observer to register
     * @see #unregisterAdapterDataObserver(android.support.v7.widget.RecyclerView
     * .AdapterDataObserver)
     */
    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        //observer.onChanged();
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
    public void onBindViewHolder(FViewHolder fViewHolder, int i) {
        //get data 0,1,3 from set;
        final ArrayList<Object> data = dataSet.get(i);

        //String[] mdata = dataSet.get(i);
        fViewHolder.author.setText((CharSequence) data.get(6));
        fViewHolder.title.setText((CharSequence) data.get(2));
        fViewHolder.dateMarked.setText((CharSequence) data.get(4));
        fViewHolder.btn_delete.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(context, String.format("%s", "Removed "), Toast.LENGTH_SHORT)
                            .show();
                    //removeMarkedWorkflow(String.valueOf(data.get(0)));
                    favDB.delete(String.valueOf(data.get(0)));
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "onClick: ", e);
                } catch (Exception ex) {
                    Log.e(TAG, "onClick: ", ex);
                }

            }
        });
        fViewHolder.btn_view_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog d = new Dialog(context);
                TextView textView = new TextView(context);
                String text = "Author -> " + (String) data.get(6) + "\nTitle: " + data.get(2) +
                        "\nDescription\n" + data.get(3);
                textView.setText(text);
                textView.setTextSize(22);
                textView.setTextColor(Color.BLACK);
                d.setTitle("" + data.get(2));
                d.setContentView(textView);
                d.show();
            }
        });
    }

    //remove a workflow from the marked state
    private void removeMarkedWorkflow(String strToRemove) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences
                (context);
        char[] charsequence = sharedPreferences.getString(WorkflowAdapter.FAVORITE_LIST_DB, "")
                .toCharArray();
        for (int i = 0; i < charsequence.length; i++) {
            if (charsequence[i] == strToRemove.charAt(0)) {
                charsequence[i] = '0';
                break;
            }
        }
        sharedPreferences.edit().putString(WorkflowAdapter.FAVORITE_LIST_DB, Arrays.toString
                (charsequence)
        ).apply();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public ArrayList<Object> getDataItemAt(int position) {
        return dataSet.size() == 0 ? null : dataSet.get(position);
    }

    public class FViewHolder extends RecyclerView.ViewHolder {

        public final ImageView favorite_thumb;
        public final TextView author, title, dateMarked; // dateAdd;
        public final FloatingActionButton btn_delete;
        public final Button btn_view_fav;

        public FViewHolder(View itemView) {
            super(itemView);
            favorite_thumb = (ImageView) itemView.findViewById(R.id.author_profile_image);
            author = (TextView) itemView.findViewById(R.id.author);
            title = (TextView) itemView.findViewById(R.id.favorite_title);
            dateMarked = (TextView) itemView.findViewById(R.id.date_set);
            btn_delete = (FloatingActionButton) itemView.findViewById(R.id.favoriteButtonDelete);
            btn_view_fav = (Button) itemView.findViewById(R.id.buttonOpenFavorite);
        }
    }
}
