package org.apache.taverna.mobile.broadcastreceivers;
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.utils.WorkflowDownloadManager;

public class WorkflowDownloadReceiver extends BroadcastReceiver {
    public WorkflowDownloadReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        long receivedID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        WorkflowDownloadManager wdm = new WorkflowDownloadManager(context, mgr);

        DownloadManager.Query query = new DownloadManager.Query(); //ask for information about the download queue
        query.setFilterById(receivedID);
        Cursor cur = mgr.query(query);
        int index = cur.getColumnIndex(DownloadManager.COLUMN_STATUS);
        String workflow = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));

        if(cur.moveToFirst()) {
            if(cur.getInt(index) == DownloadManager.STATUS_SUCCESSFUL){
                wdm.sendNotification(workflow+context.getResources().getString(R.string.downloadcomplete));
            }else{
                wdm.sendNotification(context.getResources().getString(R.string.downloadfailed));
            }
        }else{
            wdm.sendNotification(context.getResources().getString(R.string.downloadfailed));
        }
        cur.close();
    }
}
