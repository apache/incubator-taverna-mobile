/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.taverna.mobile.ui.licence;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.model.licence.LicenceContent;
import org.apache.taverna.mobile.ui.adapter.LicenceRecyclerViewAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A fragment representing a list of Licence Items.
 */
public class LicenceFragment extends Fragment {


    private static final String TAG = LicenceFragment.class.getSimpleName();

    private Gson gson;

    public static LicenceFragment newInstance() {

        return new LicenceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_licence_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (view instanceof RecyclerView) {

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new LicenceRecyclerViewAdapter(loadJSONFromAsset()));
        }
    }

    private List<LicenceContent> loadJSONFromAsset() {
        String json = null;
        List<LicenceContent> itemList = null;
        try {
            InputStream is = getActivity().getAssets().open("licences.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.i(TAG, IOException.class.getSimpleName());
            return null;
        }
        TypeToken<List<LicenceContent>> typeToken = new TypeToken<List<LicenceContent>>() {
        };
        itemList = gson.fromJson(json, typeToken.getType());

        return itemList;
    }


}
