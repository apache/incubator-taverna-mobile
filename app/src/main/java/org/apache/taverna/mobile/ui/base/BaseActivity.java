package org.apache.taverna.mobile.ui.base;

import org.apache.taverna.mobile.TavernaApplication;
import org.apache.taverna.mobile.injection.component.ActivityComponent;
import org.apache.taverna.mobile.injection.component.DaggerActivityComponent;
import org.apache.taverna.mobile.injection.module.ActivityModule;

import android.support.v7.app.AppCompatActivity;

/**
 * @author lusifer
 */
public class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;


    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(TavernaApplication.get(this).getComponent())
                    .build();
        }
        return activityComponent;
    }
}
