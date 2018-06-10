package org.apache.taverna.mobile.ui.favouriteworkflow;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.utils.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class FavouriteWorkflowsActivity extends DaggerAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    FavouriteWorkflowsFragment favouriteWorkflowsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_workflows);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();

        if (actionbar != null) {
            actionbar.setHomeButtonEnabled(true);
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle(R.string.title_nav_favourite_workflows);
        }

        if (savedInstanceState == null) {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    new FavouriteWorkflowsFragment(), R.id.frame_container);
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
