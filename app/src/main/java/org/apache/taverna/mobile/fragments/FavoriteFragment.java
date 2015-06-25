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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.adapters.FavoriteWorkflowAdapter;
import org.apache.taverna.mobile.adapters.WorkflowAdapter;
import org.apache.taverna.mobile.utils.Workflow_DB;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry Akah on 6/6/15.
 */
public class FavoriteFragment extends Fragment implements RecyclerView.OnCreateContextMenuListener, RecyclerView.OnClickListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "SECTION_NUMBER";
    private FavoriteWorkflowAdapter favoriteAdapter;
    private RecyclerView wFavoriteListView;
   // private FavoriteItemSelected favItemListener;
    public Workflow_DB myWorkflowDb;

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
        myWorkflowDb = new Workflow_DB(getActivity(), WorkflowAdapter.WORKFLOW_FAVORITE_KEY);
//TODO Add support for loading all the favorites and adding to the required list
      //  try {
         //   List<ArrayList<Object>> mydata = myWorkflowDb.get();
         //   ArrayList<Object> m = mydata.get(0);
            List<String[]> sampledata = new ArrayList<String[]>();
            sampledata.add(new String[]{"test fav","test data", "test"});//new String[]{(String) m.get(1), (String) m.get(2)});
            favoriteAdapter = new FavoriteWorkflowAdapter(getActivity(), sampledata);
   //     } catch (JSONException e) {
   //         e.printStackTrace();
       // }
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

    /**
     * Called when a context menu for the {@code view} is about to be shown.
     * Unlike {@link #onCreateOptionsMenu}, this will be called every
     * time the context menu is about to be shown and should be populated for
     * the view (or item inside the view for {@link android.widget.AdapterView} subclasses,
     * this can be found in the {@code menuInfo})).
     * <p/>
     * Use {@link #onContextItemSelected(android.view.MenuItem)} to know when an
     * item has been selected.
     * <p/>
     * The default implementation calls up to
     * {@link android.app.Activity#onCreateContextMenu Activity.onCreateContextMenu}, though
     * you can not call this implementation if you don't want that behavior.
     * <p/>
     * It is not safe to hold onto the context menu after this method returns.
     * {@inheritDoc}
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Select");
        menu.add("Comment ...");
        menu.add("Delete");
        menu.setHeaderIcon(R.mipmap.ic_launcher);
    }

    /**
     * This hook is called whenever an item in a context menu is selected. The
     * default implementation simply returns false to have the normal processing
     * happen (calling the item's Runnable or sending a message to its Handler
     * as appropriate). You can use this method for any items for which you
     * would like to do processing without those other facilities.
     * <p/>
     * Use {@link android.view.MenuItem#getMenuInfo()} to get extra information set by the
     * View that added this menu item.
     * <p/>
     * Derived classes should call through to the base class for it to perform
     * the default menu handling.
     *
     * @param item The context menu item that was selected.
     * @return boolean Return false to allow normal context menu processing to
     * proceed, true to consume it here.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String title = (String) item.getTitle();
        if(title.equals("Select")){
            Toast.makeText(getActivity(),"Selected", Toast.LENGTH_SHORT).show();
            return true;
        }else if (title.equals("Comment ...")){
            Toast.makeText(getActivity(),"Commenting", Toast.LENGTH_SHORT).show();
            return true;
        }else if (title.equals("Delete")){
            Toast.makeText(getActivity(),"Deleted", Toast.LENGTH_SHORT).show();
            return true;
        }else
            return super.onContextItemSelected(item);
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link android.app.Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
//        wFavoriteListView.setKeepScreenOn();
        wFavoriteListView.setOnClickListener(this);
        registerForContextMenu(wFavoriteListView);
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
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

    @Override
    public void onClick(View view) {
        Toast.makeText(getActivity(),"Clicked", Toast.LENGTH_SHORT).show();
    }
}
