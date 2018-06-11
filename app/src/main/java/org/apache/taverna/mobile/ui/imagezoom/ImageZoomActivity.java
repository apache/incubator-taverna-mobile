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
package org.apache.taverna.mobile.ui.imagezoom;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.ui.base.BaseActivity;
import org.apache.taverna.mobile.utils.ActivityUtils;

import butterknife.ButterKnife;

public class ImageZoomActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        ButterKnife.bind(this);


        if (savedInstanceState == null) {

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ImageZoomFragment.newInstance(
                            getIntent().getStringExtra(ImageZoomFragment.JPG_URI),
                            getIntent().getStringExtra(ImageZoomFragment.SVG_URI)),
                    R.id.frame_container);

        }

    }
}
