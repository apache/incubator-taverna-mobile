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
package org.apache.taverna.mobile.ui.favouriteworkflow;


import org.apache.taverna.mobile.FakeRemoteDataSource;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.data.model.Workflows;
import org.apache.taverna.mobile.utils.RxSchedulersOverrideRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FavouriteWorkflowsPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule rxSchedulersOverrideRule = new
            RxSchedulersOverrideRule();
    @Mock
    DataManager dataManager;

    @Mock
    FavouriteWorkflowsMvpView favouriteWorkflowsMvpView;

    private Workflows workflows;

    private FavouriteWorkflowsPresenter favouriteWorkflowsPresenter;

    @Before
    public void setUp() {

        favouriteWorkflowsPresenter = new FavouriteWorkflowsPresenter(dataManager);
        favouriteWorkflowsPresenter.attachView(favouriteWorkflowsMvpView);
        workflows = FakeRemoteDataSource.getWorkflowList();
    }

    @After
    public void tearDown() {

        favouriteWorkflowsPresenter.detachView();
    }

    @Test
    public void loadAllWorkflow_validWorkflowList_ReturnsResults() {

        when(dataManager.getFavoriteWorkflowList()).thenReturn(
                Observable.just(workflows.getWorkflowList()));

        favouriteWorkflowsPresenter.loadAllWorkflow();

        verify(favouriteWorkflowsMvpView).showWorkflows(workflows.getWorkflowList());
        verify(favouriteWorkflowsMvpView, never()).showEmptyWorkflow();
        verify(favouriteWorkflowsMvpView, never()).showErrorSnackBar();

    }

    @Test
    public void loadAllWorkflow_EmptyWorkflowList_ReturnsNoWorkflowResults() {

        Workflows workflows = new Workflows();
        workflows.setWorkflowList(new ArrayList<Workflow>());

        when(dataManager.getFavoriteWorkflowList()).thenReturn(
                Observable.just(workflows.getWorkflowList()));

        favouriteWorkflowsPresenter.loadAllWorkflow();

        verify(favouriteWorkflowsMvpView, never()).showWorkflows(workflows.getWorkflowList());
        verify(favouriteWorkflowsMvpView).showEmptyWorkflow();
        verify(favouriteWorkflowsMvpView, never()).showErrorSnackBar();

    }

    @Test
    public void loadAllWorkflow_RuntimeError_ShowError() {

        when(dataManager.getFavoriteWorkflowList()).thenReturn(
                Observable.<List<Workflow>>error(new RuntimeException()));

        favouriteWorkflowsPresenter.loadAllWorkflow();

        verify(favouriteWorkflowsMvpView, never()).showWorkflows(workflows.getWorkflowList());
        verify(favouriteWorkflowsMvpView, never()).showEmptyWorkflow();
        verify(favouriteWorkflowsMvpView).showErrorSnackBar();

    }


}