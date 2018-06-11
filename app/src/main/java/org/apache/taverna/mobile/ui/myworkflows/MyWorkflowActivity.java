package org.apache.taverna.mobile.ui.myworkflows;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.ui.base.BaseActivity;
import org.apache.taverna.mobile.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyWorkflowActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_workflows);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();

        if (actionbar != null) {
            actionbar.setHomeButtonEnabled(true);
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle(R.string.title_nav_my_workflows);
        }

        if (savedInstanceState == null) {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    new MyWorkflowFragment(), R.id.frame_container);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
