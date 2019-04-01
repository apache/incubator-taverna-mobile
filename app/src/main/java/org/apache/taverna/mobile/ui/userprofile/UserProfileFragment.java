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
package org.apache.taverna.mobile.ui.userprofile;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.ui.base.BaseActivity;
import org.apache.taverna.mobile.ui.favouriteworkflow.FavouriteWorkflowsActivity;
import org.apache.taverna.mobile.ui.myworkflows.MyWorkflowActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {

    @Inject DataManager dataManager;

    @BindView(R.id.user_name)
    TextView mUserName;

    @BindView(R.id.user_avatar)
    CircleImageView mUserAvatar;

    @BindView(R.id.user_email)
    TextView mUserEmail;

    @BindView(R.id.user_website)
    TextView mUserWebsite;

    @BindView(R.id.user_description)
    TextView mUserDescription;

    @BindView(R.id.user_city)
    TextView mUserCity;

    @BindView(R.id.user_country)
    TextView mUserCountry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, parent, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, view);

        setUserDetail();

    }

    @OnClick(R.id.my_workflow_layout)
    void myWorkflows(View v) {
        Intent intent = new Intent(getActivity(), MyWorkflowActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.my_favorite_workflow_layout)
    void myFavoriteWorkflow(View v) {
        Intent intent = new Intent(getActivity(), FavouriteWorkflowsActivity.class);
        getActivity().startActivity(intent);
    }

    private void setUserDetail() {

        String userName = dataManager.getPreferencesHelper().getUserName();
        String userDescription = dataManager.getPreferencesHelper().getUserDescription();
        String userEmail = dataManager.getPreferencesHelper().getUserEmail();
        String userWebsite = dataManager.getPreferencesHelper().getUserWebsite();
        String userCity = dataManager.getPreferencesHelper().getUserCity();
        String userCountry = dataManager.getPreferencesHelper().getUserCountry();

        String avatarUrl = dataManager.getPreferencesHelper().getUserAvatarUrl();
        Glide.with(getContext())
                .load(avatarUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(R.drawable.ic_account_circle_black_24dp)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super
                            GlideDrawable> glideAnimation) {
                        mUserAvatar.setImageDrawable(resource);
                    }
                });

        if (userName != null) {
            mUserName.setText(userName);
        } else {
            mUserName.setText(R.string.empty_fields);
        }

        if (userDescription != null) {
            userDescription = android.text.Html.fromHtml(userDescription).toString();
            mUserDescription.setText(userDescription);
        } else {
            mUserDescription.setText("");
        }

        if (userEmail != null) {
            mUserEmail.setText(userEmail);
        } else {
            mUserEmail.setText(R.string.empty_fields);
        }

        if (userWebsite != null) {
            mUserWebsite.setText(userWebsite);
        } else {
            mUserWebsite.setText(R.string.empty_fields);
        }

        if (userCity != null) {
            mUserCity.setText(userCity);
        } else {
            mUserCity.setText(R.string.empty_fields);
        }

        if (userCountry != null) {
            mUserCountry.setText(userCountry);
        } else {
            mUserCountry.setText(R.string.empty_fields);
        }
    }
}
