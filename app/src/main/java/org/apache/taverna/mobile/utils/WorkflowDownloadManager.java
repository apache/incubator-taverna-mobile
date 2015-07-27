package org.apache.taverna.mobile.utils;
/**
 * Apache Taverna Mobile
 * Copyright 2015 The Apache Software Foundation

 * This product includes software developed at
 * The Apache Software Foundation (http://www.apache.org/).

 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import org.apache.taverna.mobile.R;

import java.io.File;

/**
 * Created by root on 6/11/15.
 */
public class WorkflowDownloadManager {
    private DownloadManager downloadManager;
    private Context context;
    private boolean isDownloading;

    public WorkflowDownloadManager(Context ctx, DownloadManager downloadManager) {
        this.context = ctx;
        this.downloadManager = downloadManager;
        this.isDownloading = false;
    }

    public WorkflowDownloadManager(Context context) {
        this.context = context;
        this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        this.isDownloading = false;
    }

    /**
     * Download the given workflow
     * @param destination The destination file in which to save the downloaded file
     */
    public void downloadWorkflow(File destination, String sourceurl) throws Exception{

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(DownloadManager.STATUS_PAUSED|
                DownloadManager.STATUS_PENDING|
                DownloadManager.STATUS_RUNNING|DownloadManager.STATUS_FAILED|
                DownloadManager.STATUS_SUCCESSFUL);
        Cursor cur = this.downloadManager.query(query);
        int col = cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);

        for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
            this.isDownloading = this.isDownloading || (destination.getName() == cur.getString(col));
        }
       // cur.close();
        if (!this.isDownloading) {
            Uri source = Uri.parse(sourceurl);
            //extract the file name from the source url and append it to the workflow storage directory to be used to download the file into.
            Uri destinationurl = Uri.withAppendedPath(Uri.fromFile(destination), Uri.parse(sourceurl).getLastPathSegment());

            DownloadManager.Request request = new DownloadManager.Request(source);
            request.setTitle("Workflow");
            request.setDescription("Downloading workflow");
            request.setDestinationUri(destinationurl);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED |
                    DownloadManager.Request.VISIBILITY_VISIBLE);

            long id = this.downloadManager.enqueue(request);

            if(id != 0)
                sendNotification(this.context.getResources().getString(R.string.downloadprogress));
            cur.close();
        }
    }

    public void sendNotification(String message){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.context);
        notificationBuilder.setContentText(message)
                .setContentTitle("Workflow Download")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setWhen(0)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        Notification nf  = notificationBuilder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, nf);
    }
}
