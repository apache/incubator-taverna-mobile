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

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.taverna.mobile.data.model.TutorialSliderEnum;


public class TutorialSliderAdapter extends PagerAdapter {

    private Context context;
    public TutorialSliderAdapter(Context context) {
        this.context = context;
    }

    public Object instantiateItem(ViewGroup collection, int position) {
        TutorialSliderEnum tutorialSliderEnum = TutorialSliderEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater
                .inflate(tutorialSliderEnum.getLayoutId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    public int getCount() {
        return TutorialSliderEnum.values().length;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}