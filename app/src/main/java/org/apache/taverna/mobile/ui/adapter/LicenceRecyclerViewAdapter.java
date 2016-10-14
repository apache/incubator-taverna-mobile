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

package org.apache.taverna.mobile.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.model.licence.LicenceContent;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link LicenceContent}
 */
public class LicenceRecyclerViewAdapter extends RecyclerView.Adapter<LicenceRecyclerViewAdapter
        .ViewHolder> {

    private final List<LicenceContent> mValues;

    public LicenceRecyclerViewAdapter(List<LicenceContent> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_licence, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        holder.mLicenceNameView.setText(mValues.get(position).getName());
        holder.mLicenceVersionView.setText(mValues.get(position).getVersion());
        holder.mLicenceLicenceView.setText(mValues.get(position).getLicence());


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mLicenceNameView;
        private final TextView mLicenceVersionView;
        private final TextView mLicenceLicenceView;


        public ViewHolder(View view) {
            super(view);
            mLicenceNameView = (TextView) view.findViewById(R.id.licence_name);
            mLicenceVersionView = (TextView) view.findViewById(R.id.licence_version);
            mLicenceLicenceView = (TextView) view.findViewById(R.id.licence_licence);
        }

        @Override
        public String toString() {
            return super.toString() + " " + mLicenceNameView.getText() + " " +
                    mLicenceVersionView.getText() + " " + mLicenceLicenceView.getText();
        }
    }
}
