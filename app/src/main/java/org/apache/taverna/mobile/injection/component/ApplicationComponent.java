package org.apache.taverna.mobile.injection.component;

import android.app.Application;
import android.content.Context;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.local.PreferencesHelper;
import org.apache.taverna.mobile.injection.ApplicationContext;
import org.apache.taverna.mobile.injection.module.ActivityModule;
import org.apache.taverna.mobile.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();
    Application application();
    DataManager dataManager();
    PreferencesHelper preferencesHelper();

}
