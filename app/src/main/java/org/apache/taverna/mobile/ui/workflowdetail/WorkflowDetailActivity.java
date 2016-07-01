package org.apache.taverna.mobile.ui.workflowdetail;


import org.apache.taverna.mobile.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class WorkflowDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_workflow);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_container, WorkflowDetailFragment.newInstance(getIntent().getStringExtra("id")))
                    .commit();
        }

    }
}
