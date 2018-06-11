package org.apache.taverna.mobile.injection.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.raizlabs.android.dbflow.annotation.Database;

import org.apache.taverna.mobile.data.remote.TavernaService;
import org.apache.taverna.mobile.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 */

@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
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

}
