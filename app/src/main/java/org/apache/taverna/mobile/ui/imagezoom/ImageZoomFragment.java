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


import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.utils.ConnectionInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageZoomFragment extends Fragment implements ImageZoomMvpView {

    public static final String ID = "id";
    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    private static final String SERVER_ERROR = "Sever Error. Please try after sometime";
    private static final String TAG = "ImageZoomFragment";
    @BindView(R.id.ivWorkflowImage)
    ImageView workflowImage;
    @BindView(R.id.ivClose)
    ImageView close;
    PhotoViewAttacher mAttacher;
    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    int mode = NONE;
    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    private String id;
    private String jpgURI;
    private DataManager dataManager;
    private ImageZoomPresenter mImageZoomPresenter;
    private ConnectionInfo mConnectionInfo;

    public static ImageZoomFragment newInstance(String id) {

        Bundle args = new Bundle();

        args.putString(ID, id);

        ImageZoomFragment fragment = new ImageZoomFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getArguments().getString(ID);

        dataManager = new DataManager();
        mImageZoomPresenter = new ImageZoomPresenter(dataManager);
        mConnectionInfo = new ConnectionInfo(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_image_zoom, container, false);

        ButterKnife.bind(this, rootView);

        mImageZoomPresenter.attachView(this);

        return rootView;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mConnectionInfo.isConnectingToInternet()) {

            mImageZoomPresenter.loadImage(id, workflowImage);
        } else {

            showErrorSnackBar(getString(R.string.no_internet));
        }


    }

    @OnClick(R.id.ivClose)
    public void closeActivity(View v) {

        getActivity().finish();
    }


    @Override
    public void showErrorSnackBar(String error) {

        final Snackbar snackbar = Snackbar.make(workflowImage, error, Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                snackbar.dismiss();
                getActivity().finish();
            }
        });

        snackbar.show();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                snackbar.dismiss();
                getActivity().finish();
            }
        }, 2000);
    }

    @Override
    public Context getAppContext() {
        return getContext();
    }

    @Override
    public void setJPGuri(String uri) {
        jpgURI = uri;
    }

    @Override
    public void setJPGImage() {


        Glide.with(getContext())
                .load(jpgURI)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.placeholder)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(new Target<GlideDrawable>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        workflowImage.setImageDrawable(placeholder);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        showErrorSnackBar(SERVER_ERROR);
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        workflowImage.setImageDrawable(resource.getCurrent());
                        addImageAttacher();

                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        workflowImage.setImageDrawable(placeholder);
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

    @Override
    public void addImageAttacher() {
        mAttacher = new PhotoViewAttacher(workflowImage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mImageZoomPresenter.detachView();
    }
}
