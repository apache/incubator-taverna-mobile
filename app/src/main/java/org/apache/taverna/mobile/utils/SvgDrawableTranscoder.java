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
package org.apache.taverna.mobile.utils;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.caverock.androidsvg.SVG;

import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;

/**
 * Convert the {@link SVG}'s internal representation to an Android-compatible one ({@link Picture}).
 */
public class SvgDrawableTranscoder implements ResourceTranscoder<SVG, PictureDrawable> {
    @Override
    public Resource<PictureDrawable> transcode(Resource<SVG> toTranscode) {
        SVG svg = toTranscode.get();
        Picture picture = svg.renderToPicture();
        PictureDrawable drawable = new PictureDrawable(picture);
        return new SimpleResource<PictureDrawable>(drawable);
    }

    @Override
    public String getId() {
        return "";
    }
}