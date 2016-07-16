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
package org.apache.taverna.mobile.data.local;


import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.data.model.Workflow_Table;
import org.apache.taverna.mobile.data.model.Workflows;

import android.support.annotation.Nullable;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;


public class DBHelper {


    public DBHelper() {

    }

    @Nullable
    public Observable<Workflows> syncWorkflows(final Workflows workflows) {
        return Observable.create(new Observable.OnSubscribe<Workflows>() {
            @Override
            public void call(Subscriber<? super Workflows> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                for (Workflow workflow : workflows.getWorkflowList()) {
                    if (!workflow.exists()) {
                        workflow.setFavourite(false);
                        workflow.save();

                    } else {

                        updateWorkflow(workflow).save();
                    }

                }
                subscriber.onNext(workflows);
                subscriber.onCompleted();
            }
        });
    }

    private Workflow updateWorkflow(Workflow workflow) {

        Workflow workflow1 = SQLite.select()
                .from(Workflow.class)
                .where(Workflow_Table.id.eq(workflow.getId()))
                .querySingle();

        if (workflow1 != null) {
            if (workflow.getDescription() != null) {

                workflow1.setDescription(workflow.getDescription());
            }
            if (workflow.getUpdatedAt() != null) {

                workflow1.setUpdatedAt(workflow.getUpdatedAt());
            }
            if (workflow.getSvgUri() != null) {

                workflow1.setSvgUri(workflow.getSvgUri());
            }
            if (workflow.getLicenseType() != null) {

                workflow1.setLicenseType(workflow.getLicenseType());
            }
            if (workflow.getContentUri() != null) {

                workflow1.setContentUri(workflow.getContentUri());
            }
            if (workflow.getContentType() != null) {

                workflow1.setContentUri(workflow.getContentType());
            }
            if (workflow.getElementId() != null) {

                workflow1.setElementId(workflow.getElementId());
            }
            workflow1.setFavourite(workflow1.isFavourite());
            workflow1.setTitle(workflow.getTitle());
            workflow1.setType(workflow.getType());
            workflow1.setUploader(workflow.getUploader());
            workflow1.setPreviewUri(workflow.getPreviewUri());
            workflow1.setCreatedAt(workflow.getCreatedAt());
            workflow1.setResource(workflow.getResource());
            workflow1.setUri(workflow.getUri());
            workflow1.setId(workflow.getId());
            workflow1.setVersion(workflow.getVersion());

        }
        return workflow1;
    }


    public Observable<Workflow> syncWorkflow(final Workflow workflow) {
        return Observable.create(new Observable.OnSubscribe<Workflow>() {
            @Override
            public void call(Subscriber<? super Workflow> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (!workflow.exists()) {
                    workflow.save();

                } else {

                    updateWorkflow(workflow).save();
                }
                subscriber.onNext(workflow);
                subscriber.onCompleted();
            }
        });
    }


    public Observable<Boolean> setFavouriteWorkflow(final String id) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                subscriber.onNext(updateFavouriteWorkflow(id));
                subscriber.onCompleted();
            }
        });
    }


    public Observable<Boolean> getFavouriteWorkflow(final String id) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Workflow workflow1 = SQLite.select()
                        .from(Workflow.class)
                        .where(Workflow_Table.id.eq(id))
                        .querySingle();

                if (workflow1 != null) {

                    subscriber.onNext(workflow1.isFavourite());
                    subscriber.onCompleted();
                } else {

                    subscriber.onError(null);
                }


            }
        });
    }

    public boolean updateFavouriteWorkflow(String id) {

        Workflow workflow1 = SQLite.select()
                .from(Workflow.class)
                .where(Workflow_Table.id.eq(id))
                .querySingle();

        if (workflow1 != null) {
            workflow1.setFavourite(!workflow1.isFavourite());
            workflow1.save();
            return true;
        }

        return false;
    }

    public Observable<List<Workflow>> getFavouriteWorkflow() {
        return Observable.create(new Observable.OnSubscribe<List<Workflow>>() {
            @Override
            public void call(Subscriber<? super List<Workflow>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                List<Workflow> workflows = SQLite.select()
                        .from(Workflow.class)
                        .where(Workflow_Table.favourite.eq(true))
                        .queryList();

                subscriber.onNext(workflows);
                subscriber.onCompleted();

            }
        });

    }

    public Observable<Workflow> getFavouriteWorkflowDetail(final String id) {
        return Observable.defer(new Func0<Observable<Workflow>>() {
            @Override
            public Observable<Workflow> call() {
                return Observable
                        .just(SQLite.select()
                                .from(Workflow.class)
                                .where(Workflow_Table.id.eq(id))
                                .querySingle());
            }
        });

    }


}

