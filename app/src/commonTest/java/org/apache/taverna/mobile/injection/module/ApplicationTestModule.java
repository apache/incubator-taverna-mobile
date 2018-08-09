package org.apache.taverna.mobile.injection.module;

import android.app.Application;
import android.content.Context;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.remote.TavernaService;
import org.apache.taverna.mobile.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * Provides application-level dependencies for an app running on a testing environment
 * This allows injecting mocks if necessary.
 */
@Module
public class ApplicationTestModule {

    private final Application mApplication;

    public ApplicationTestModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    /************* MOCKS *************/

    @Provides
    @Singleton
    DataManager provideDataManager() {
        return mock(DataManager.class);
    }

    @Provides
    @Singleton
    TavernaService provideRibotsService() {
        return mock(TavernaService.class);
    }

}
