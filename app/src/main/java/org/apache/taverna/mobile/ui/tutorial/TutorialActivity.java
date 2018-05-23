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
package org.apache.taverna.mobile.ui.tutorial;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.local.PreferencesHelper;
import org.apache.taverna.mobile.data.model.TutorialSliderEnum;
import org.apache.taverna.mobile.ui.adapter.TutorialSliderAdapter;
import org.apache.taverna.mobile.ui.login.LoginActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;


public class TutorialActivity extends DaggerAppCompatActivity implements ViewPager.OnPageChangeListener {

    @Inject
    PreferencesHelper preferencesHelper;

    @BindView(R.id.slide_pager)
    ViewPager slidePager;

    @BindView(R.id.layoutDots)
    LinearLayout dotsLayout;

    @BindView(R.id.btn_skip)
    Button bSkip;

    @BindView(R.id.btn_next)
    Button bNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesHelper = new PreferencesHelper(this);

        setContentView(R.layout.activity_tutorial);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        addBottomDots(0);

        TutorialSliderAdapter tutorialSliderAdapter = new TutorialSliderAdapter(this);
        slidePager.setAdapter(tutorialSliderAdapter);
        slidePager.addOnPageChangeListener(this);

    }

    @OnClick(R.id.btn_skip)
    public void skipClick(View v) {
        launchLoginScreen();
    }

    @OnClick(R.id.btn_next)
    public void nextClick(View v) {
        int current = getItem(+1);
        if (current < TutorialSliderEnum.values().length) {
            slidePager.setCurrentItem(current);
        } else {
            launchLoginScreen();
        }
    }

    public void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[TutorialSliderEnum.values().length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    public int getItem(int i) {
        return slidePager.getCurrentItem() + i;
    }

    public void launchLoginScreen() {
        preferencesHelper.setFirstTimeLaunch(false);
        startActivity(new Intent(TutorialActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        addBottomDots(position);

        if (position == TutorialSliderEnum.values().length - 1) {
            bNext.setText(getString(R.string.start));
            bSkip.setVisibility(View.GONE);
        } else {
            bNext.setText(getString(R.string.next));
            bSkip.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

