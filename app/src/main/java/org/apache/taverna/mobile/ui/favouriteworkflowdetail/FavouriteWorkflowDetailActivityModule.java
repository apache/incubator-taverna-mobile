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
package org.apache.taverna.mobile.ui.favouriteworkflowdetail;

import org.apache.taverna.mobile.data.remote.TavernaService;
import org.apache.taverna.mobile.ui.favouriteworkflow.FavouriteWorkflowsMvpView;
import org.apache.taverna.mobile.ui.favouriteworkflow.FavouriteWorkflowsPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class FavouriteWorkflowDetailActivityModule {

    FavouriteWorkflowDetailMvpView favouriteWorkflowDetailMvpView;
    TavernaService tavernaService;

    @Provides
    FavouriteWorkflowDetailMvpView favouriteWorkflowDetailMvpView() {
        this.favouriteWorkflowDetailMvpView = favouriteWorkflowDetailMvpView();
        return favouriteWorkflowDetailMvpView;
    }

    @Provides
    TavernaService tavernaService() {
        this.tavernaService = tavernaService();
        return tavernaService;
    }

    @Binds
    abstract FavouriteWorkflowsMvpView provideFavouriteWorkflowsMvpView(FavouriteWorkflowDetailActivity favouriteWorkflowDetailActivity);
}
