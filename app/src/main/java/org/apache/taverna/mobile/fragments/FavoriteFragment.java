package org.apache.taverna.mobile.fragments;

/*
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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.activities.DashboardMainActivity;
import org.apache.taverna.mobile.adapters.FavoriteWorkflowAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Larry Akah on 6/6/15.
 */
public class FavoriteFragment extends Fragment implements AdapterView.OnItemClickListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "SECTION_NUMBER";
    private FavoriteWorkflowAdapter favoriteAdapter;
    private RecyclerView wFavoriteListView;
    private FavoriteItemSelected favItemListener;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FavoriteFragment newInstance(int sectionNumber) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FavoriteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //TODO : Collect data through  API (ICEBASE)
        //create a sample list of data. future data come s from an API on local storage
        List<String[]> sampledata = new ArrayList<String[]>();
               sampledata.add(new String[]{"Larry", "Prokaryotic symbiosis",
                       new SimpleDateFormat().format(new Date()),
                       new SimpleDateFormat().format(new Date()),
                       new SimpleDateFormat().format(new Date()),
                       new SimpleDateFormat().format(new Date())});
        sampledata.add(new String[]{"Meeze Ball", "Fluid Traffic analysis",
                new SimpleDateFormat().format(new Date()),
                new SimpleDateFormat().format(new Date()),
                new SimpleDateFormat().format(new Date()),
                new SimpleDateFormat().format(new Date())});
        sampledata.add(new String[]{"Halway Law", "Photosynthetic tissue disengagement",
                new SimpleDateFormat().format(new Date()),
                new SimpleDateFormat().format(new Date()),
                new SimpleDateFormat().format(new Date()),
                new SimpleDateFormat().format(new Date())});

        favoriteAdapter = new FavoriteWorkflowAdapter(getActivity(), sampledata);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_main, container, false);
        wFavoriteListView = (RecyclerView)rootView.findViewById(android.R.id.list);
        wFavoriteListView.setHasFixedSize(true);
        wFavoriteListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        wFavoriteListView.setAdapter(favoriteAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            favItemListener = (FavoriteItemSelected) activity;
            ((DashboardMainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }catch (ClassCastException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        favItemListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        favItemListener.onFavoriteItemSelected(i);
    }

    /**
     * Causes the empty textView to be set and become visible
     */
    private void setEmptyText(){
        View emptyView = wFavoriteListView.getChildAt(1);
        if(emptyView instanceof TextView){
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    public interface FavoriteItemSelected{
        //implemented by activity when sending click events to this fragments views
        public void onFavoriteItemSelected(int position);
    }
}
