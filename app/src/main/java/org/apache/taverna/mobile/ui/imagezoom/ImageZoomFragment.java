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


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
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
import org.apache.taverna.mobile.utils.ConnectionInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageZoomFragment extends Fragment implements ImageZoomMvpView {

    public static final String JPG_URI = "jpgURI";

    public static final String SVG_URI = "svgURI";

    private static final String SERVER_ERROR = "Sever Error. Please try after sometime";

    int id = 1;

    private NotificationManager mNotifyManager;

    private NotificationCompat.Builder mBuilder;


    @BindView(R.id.ivWorkflowImage)
    ImageView workflowImage;

    @BindView(R.id.ivClose)
    ImageView close;

    @BindView(R.id.ivdots)
    ImageView options;

    PhotoViewAttacher mAttacher;

    private String svgURI;

    private String jpgURI;

    private ImageZoomPresenter mImageZoomPresenter;



    public static ImageZoomFragment newInstance(String jpgURI, String svgURI) {
        Bundle args = new Bundle();
        args.putString(JPG_URI, jpgURI);
        args.putString(SVG_URI, svgURI);
        ImageZoomFragment fragment = new ImageZoomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        svgURI = getArguments().getString(SVG_URI);
        jpgURI = getArguments().getString(JPG_URI);
        mImageZoomPresenter = new ImageZoomPresenter();

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

        if (ConnectionInfo.isConnectingToInternet(getContext())) {
            mImageZoomPresenter.loadImage(svgURI, workflowImage);
        } else {
            showErrorSnackBar(getString(R.string.no_internet_connection));
        }
    }

    @OnClick(R.id.ivClose)
    public void closeActivity(View v) {
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

    }

    @OnClick(R.id.ivdots)
    public void openOptions(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getAppContext());
        builder.setItems(R.array.workflow_images_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        downloadImage(svgURI);
                        break;
                    case 1:
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        } else {
                            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                            shareIntent.setType("image/*");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, svgURI);
                            startActivity(Intent.createChooser(shareIntent, "Share via"));
                        }
                        break;
                    case 2:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(svgURI));
                        startActivity(browserIntent);
                        break;
                }

            }
        });
        builder.show();
    }

    public void downloadImage(String url) {
        mNotifyManager = (NotificationManager) getAppContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getAppContext());
        mBuilder.setContentTitle(url)
                .setSmallIcon(android.R.drawable.stat_sys_download);
        DownloadImage download = new DownloadImage();
        download.execute(url);
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

    public class DownloadImage extends AsyncTask<String, Integer, String> {

        int calculatedProgress = 0;
        int imageLength = 0;

        @Override
        protected void onPreExecute() {
            mBuilder.setProgress(100, 0, false);
            mNotifyManager.notify(id, mBuilder.build());

        }

        @Override
        protected String doInBackground(String... params) {

            String path = params[0];
            File downloadedImages = new File(
                    Environment
                            .getExternalStoragePublicDirectory(
                                    Environment
                                            .DIRECTORY_DOWNLOADS)
                            .getAbsolutePath() + "/" + Uri.parse(path)
                            .getLastPathSegment());
            int total = 0;
            int count = 0;

            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                imageLength = urlConnection.getContentLength();
                boolean isFileCreated = downloadedImages.exists();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                byte[] data = new byte[1024];
                OutputStream outputStream = new FileOutputStream(downloadedImages);

                if (!isFileCreated) {
                    isFileCreated = downloadedImages.createNewFile();
                }
                while ((count = inputStream.read(data)) != -1) {
                    total = total + count;
                    outputStream.write(data, 0, count);
                    calculatedProgress = (int) total * 100 / imageLength;
                    publishProgress(total);
                }
                inputStream.close();
                outputStream.close();

            } catch (MalformedURLException e) {

            } catch (IOException e) {

            }

            return String.valueOf(downloadedImages);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            mBuilder.setProgress(100, values[0], true);
            mNotifyManager.notify(id, mBuilder.build());
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String result) {

            mBuilder.setContentText("Download complete");
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
            mBuilder.setProgress(0, 0, false);
            File file = new File(result);
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "image/*");
            PendingIntent pIntent = PendingIntent.getActivity(getAppContext(), 0, intent, 0);
            mBuilder.setContentIntent(pIntent).build();
            mBuilder.setAutoCancel(true);
            mNotifyManager.notify(id, mBuilder.build());

        }
    }
}
