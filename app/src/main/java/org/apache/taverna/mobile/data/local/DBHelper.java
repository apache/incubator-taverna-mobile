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


import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.data.model.Workflow_Table;
import org.apache.taverna.mobile.data.model.Workflows;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;


public class DBHelper {

    public static final String SVG_URI = "svgURI";

    public static final String JPG_URI = "jpgURI";

    public DBHelper() {

    }

    @Nullable
    public Observable<Workflows> syncWorkflows(final Workflows workflows) {
        return Observable.defer(new Callable<ObservableSource<? extends Workflows>>() {
            @Override
            public ObservableSource<? extends Workflows> call() throws Exception {

                for (Workflow workflow : workflows.getWorkflowList()) {
                    if (!workflow.exists()) {
                        workflow.setFavourite(false);
                        workflow.save();
                    } else {

                        updateWorkflow(workflow).save();
                    }
                }

                return Observable.just(workflows);
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
        return Observable.defer(new Callable<ObservableSource<? extends Workflow>>() {
            @Override
            public ObservableSource<? extends Workflow> call() throws Exception {
                if (!workflow.exists()) {
                    workflow.save();
                } else {
                    updateWorkflow(workflow).save();
                }
                return Observable.just(workflow);
            }
        });
    }

    public Observable<Boolean> setFavouriteWorkflow(final String id) {
        return Observable.defer(new Callable<ObservableSource<? extends Boolean>>() {
            @Override
            public ObservableSource<? extends Boolean> call() throws Exception {
                return Observable.just(updateFavouriteWorkflow(id));
            }
        });
    }


    public Observable<Boolean> getFavouriteWorkflow(final String id) {
        return Observable.defer(new Callable<ObservableSource<? extends Boolean>>() {
            @Override
            public ObservableSource<? extends Boolean> call() throws Exception {
                Workflow workflow = SQLite.select()
                        .from(Workflow.class)
                        .where(Workflow_Table.id.eq(id))
                        .querySingle();
                if (workflow != null) {
                    return Observable.just(workflow.isFavourite());
                } else {
                    return Observable.just(null);
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
        return Observable.defer(new Callable<ObservableSource<? extends List<Workflow>>>() {
            @Override
            public ObservableSource<? extends List<Workflow>> call() throws Exception {
                return Observable.just(SQLite.select()
                        .from(Workflow.class)
                        .where(Workflow_Table.favourite.eq(true))
                        .queryList());
            }
        });
    }

    public Observable<Workflow> getFavouriteWorkflowDetail(final String id) {
        return Observable.defer(new Callable<ObservableSource<? extends Workflow>>() {
            @Override
            public ObservableSource<? extends Workflow> call() throws Exception {
                return Observable.just(SQLite.select()
                                .from(Workflow.class)
                                .where(Workflow_Table.id.eq(id))
                                .querySingle());
            }
        });
    }


}

