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
package org.apache.taverna.mobile;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.injection.component.DaggerTestComponent;
import org.apache.taverna.mobile.injection.component.TestComponent;
import org.apache.taverna.mobile.injection.module.ApplicationTestModule;


import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import android.content.Context;

import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class TestComponentRule implements TestRule {

    private final TestComponent mTestComponent;
    private final Context mContext;

    private Scheduler schedulerInstance = Schedulers.trampoline();

    private Function<Scheduler, Scheduler> schedulerMapper = new Function<Scheduler, Scheduler>() {
        @Override
        public Scheduler apply(Scheduler scheduler) throws Exception {
            return schedulerInstance;
        }
    };
    private Function<Callable<Scheduler>, Scheduler> schedulerMapperLazy =
            new Function<Callable<Scheduler>, Scheduler>() {


        @Override
        public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
            return schedulerInstance;
        }
    };

    public TestComponentRule(Context context) {
        mContext = context;
        TavernaApplication application = TavernaApplication.get(context);
        mTestComponent = DaggerTestComponent.builder()
                .applicationTestModule(new ApplicationTestModule(application))
                .build();
    }

    public Context getContext() {
        return mContext;
    }

    public DataManager getMockDataManager() {
        return mTestComponent.dataManager();
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxAndroidPlugins.reset();
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerMapperLazy);

                RxJavaPlugins.reset();
                RxJavaPlugins.setIoSchedulerHandler(schedulerMapper);
                RxJavaPlugins.setNewThreadSchedulerHandler(schedulerMapper);
                RxJavaPlugins.setComputationSchedulerHandler(schedulerMapper);

                TavernaApplication application = TavernaApplication.get(mContext);
                application.setComponent(mTestComponent);

                base.evaluate();
                application.setComponent(null);
                RxAndroidPlugins.reset();
                RxJavaPlugins.reset();
            }
        };
    }
}