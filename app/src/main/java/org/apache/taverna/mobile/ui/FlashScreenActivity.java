package org.apache.taverna.mobile.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.activities.DashboardMainActivity;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.local.PreferencesHelper;
import org.apache.taverna.mobile.ui.login.LoginActivity;

public class FlashScreenActivity extends AppCompatActivity {

    private DataManager dataManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);

        dataManager = new DataManager(new PreferencesHelper(this));

        if (!dataManager.getPreferencesHelper().getLoggedInFlag()) {
            dataManager.getPreferencesHelper().clear();
            startActivity(new Intent(FlashScreenActivity.this, LoginActivity.class));
            (FlashScreenActivity.this).finish();
        } else {
            startActivity(new Intent(FlashScreenActivity.this, DashboardMainActivity.class));
            (FlashScreenActivity.this).finish();
        }
    }



}
