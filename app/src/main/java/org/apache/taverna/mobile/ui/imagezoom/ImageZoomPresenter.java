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


import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.caverock.androidsvg.SVG;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.ui.base.BasePresenter;
import org.apache.taverna.mobile.utils.SvgDecoder;
import org.apache.taverna.mobile.utils.SvgDrawableTranscoder;

import java.io.InputStream;

import javax.inject.Inject;

public class ImageZoomPresenter extends BasePresenter<ImageZoomMvpView> {

    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    @Inject
    public ImageZoomPresenter() {

    }

    @Override
    public void attachView(ImageZoomMvpView mvpView) {

        super.attachView(mvpView);

        requestBuilder = Glide.with(getMvpView().getAppContext())
                .using(Glide.buildStreamModelLoader(Uri.class,
                        getMvpView().getAppContext()), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .animate(android.R.anim.fade_in);

    }

    @Override
    public void detachView() {
        super.detachView();

    }

    public void loadImage(String svgURI, final ImageView imageView) {

        setSVG(svgURI, imageView);
    }

    private void setSVG(String imageURI, final ImageView imageView) {

        requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .load(Uri.parse(imageURI))
                .into(new Target<PictureDrawable>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        imageView.setImageDrawable(placeholder);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        imageView.setImageDrawable(errorDrawable);

                        if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
                            imageView.setLayerType(ImageView.LAYER_TYPE_NONE, null);
                        }

                        getMvpView().setJPGImage();
                    }

                    @Override
                    public void onResourceReady(PictureDrawable resource,
                                                GlideAnimation<? super PictureDrawable>
                                                        glideAnimation) {
                        if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
                            imageView.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null);
                        }
                        imageView.setImageDrawable(resource);
                        getMvpView().addImageAttacher();
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {

                    }

                    @Override
                    public void getSize(SizeReadyCallback cb) {

                    }

                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void setRequest(Request request) {

                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }
                });

    }


}
