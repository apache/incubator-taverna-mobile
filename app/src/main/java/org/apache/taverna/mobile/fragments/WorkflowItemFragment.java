package org.apache.taverna.mobile.fragments;
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
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.activities.DashboardMainActivity;
import org.apache.taverna.mobile.adapters.WorkflowAdapter;
import org.apache.taverna.mobile.tavernamobile.User;
import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.apache.taverna.mobile.utils.AvatarLoader;
import org.apache.taverna.mobile.utils.WorkflowLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class WorkflowItemFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Animation in;
    private ProgressDialog mProgressDialog; //progressbar used to indicate the state of the workflow loaders

    private String mParam1;
    private String mParam2;

    /**
     * The fragment's ListView/GridView.
     */
    private static RecyclerView mListView;
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private WorkflowAdapter workflowAdapter;
    private static WorkflowAdapter searchAdpater;
    private static View rootView;
    public static Context cx;
    private static boolean STATE_ON = false;

    public static WorkflowItemFragment newInstance(String param1, String param2) {
        WorkflowItemFragment fragment = new WorkflowItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WorkflowItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        cx = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        in = AnimationUtils.loadAnimation(getActivity(),android.R.anim.slide_in_left);
       List<Workflow> mlist = new ArrayList<Workflow>();
   //    mlist.add(new Workflow(getActivity(),"Testing title","Larry","Ok testing",0,"http://127.0.0.1"));
   /*     mlist.add(new Workflow(getActivity(),"Testing title","Larry","Ok testing",0,"http://127.0.0.1"));
*/
        workflowAdapter = new WorkflowAdapter(getActivity(), mlist );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_item, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        // Set the adapter
        mListView = (RecyclerView) rootView.findViewById(android.R.id.list);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setAnimation(in);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {

            ((DashboardMainActivity) activity).onSectionAttached(1);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Called when the view previously created by {@link #onCreateView} has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
     * <em>regardless</em> of whether {@link #onCreateView} returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!STATE_ON)
        new WorkflowLoader(getActivity(), swipeRefreshLayout).execute();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_ON = true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //menu.clear();
        if(menu.size() == 1) {
            //get the searchview and set the searchable configuration
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            //assuming this activity is the searchable activity
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setSubmitButtonEnabled(true);
            searchView.setOnQueryTextListener(this);
//            searchView.setOnSearchClickListener(this);
//            searchView.setIconifiedByDefault(false);
            MenuItem mit = menu.add("Refresh");
            mit.setIcon(android.R.drawable.stat_notify_sync);
            mit.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().equals("Refresh")){
            new WorkflowLoader(getActivity(),swipeRefreshLayout).execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getChildAt(2);

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }
//handle a request to query for given workflows
    private void performSearch(String search){
        WorkflowAdapter ladapter = new WorkflowAdapter(getActivity());
        WorkflowAdapter wk = searchAdpater;//workflowAdapter;

        if(!TextUtils.isEmpty(search)) {
            if (null != wk)
                for (int i = 0; i < wk.getItemCount(); i++) {
                    Workflow workflow = wk.getItem(i);
                    if (workflow.getWorkflow_title().toLowerCase().contains(search.toLowerCase())) {
                        ladapter.addWorkflow(workflow);
                    }
                }
            else {
                Toast.makeText(getActivity(), "No workflows available", Toast.LENGTH_SHORT).show();
            }
            mListView.swapAdapter(ladapter, true);
            if (ladapter.getItemCount() == 0)
                Toast.makeText(getActivity(), "No workflows found matching criteria", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        new WorkflowLoader(getActivity(),swipeRefreshLayout).execute();
    }

    /**
     * Search action triggered, handle the search request. Filter the workflows by name/title and swap current adapter with the new adapter
     * @param query Search string criteria
     * @return whether or not user handled request 'manually'
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        performSearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        performSearch(s);
        return true;
    }

    public static void updateWorkflowUI(final List<Workflow> data) {

        ((Activity)cx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WorkflowItemFragment.searchAdpater = new WorkflowAdapter(cx,data);
                WorkflowItemFragment.mListView.setAdapter(WorkflowItemFragment.searchAdpater);
                if(data.size() == 0){
                    Toast.makeText(cx, cx.getResources().getString(R.string.err_workflow_conn), Toast.LENGTH_LONG).show();
                }
                System.out.println("workflows: "+data.size());
            }
        });
    }
    public static void startLoadingAvatar(final User author) {

        ((Activity)cx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    new AvatarLoader().execute(author.getDetails_uri());
                    System.out.println(author.getDetails_uri());
                }
            }
        });
    }

    public static void updateAvatar(final User author) {

        ((Activity)cx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        ((TextView) rootView.findViewById(R.id.workflow_author)).setText(author.getName());
                        new LoadAuthorAvatar((ImageView) rootView.findViewById(R.id.author_profile_image)).execute(author.getAvatar_url());
                    }catch(NullPointerException np){

                    }
                }
            }
        });
    }
    /**
     * Load the Author Avatar from a background Task
     */
    private static class LoadAuthorAvatar extends AsyncTask<String, Void, Bitmap> {
        ImageView img;

        public LoadAuthorAvatar(ImageView imageView) {
            img = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap myBitmap = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = null;
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
                input.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return myBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            img.setImageBitmap(bitmap);
///            img.setBackground();
//            notify();
        }
        private Bitmap ProcessingBitmap(Bitmap bmp){
            Bitmap bm1 = null;
            Bitmap newBitmap = null;

                bm1 = bmp;
                int w = bm1.getWidth();
                int h = bm1.getHeight();

                Bitmap.Config config = bm1.getConfig();
                if(config == null){
                    config = Bitmap.Config.ARGB_8888;
                }

                newBitmap = Bitmap.createBitmap(w, h, config);
                Canvas newCanvas = new Canvas(newBitmap);
                newCanvas.drawColor(Color.WHITE);

                Paint paint = new Paint();
                paint.setColor(Color.TRANSPARENT);
                Rect frame = new Rect(
                        (int)(w*0.05),
                        (int)(w*0.05),
                        (int)(w*0.95),
                        (int)(h*0.95));
                RectF frameF = new RectF(frame);
                newCanvas.drawRoundRect(frameF, (float)(w*0.5), (float)(h*0.05), paint);

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
                newCanvas.drawBitmap(bm1, 0, 0, paint);

            return newBitmap;
        }
    }
}
