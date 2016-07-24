package org.apache.taverna.mobile.ui.imagezoom;


import org.apache.taverna.mobile.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public class ImageZoomActivity extends AppCompatActivity {

    public static final String ID = "id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        ButterKnife.bind(this);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_container
                            , ImageZoomFragment.newInstance(getIntent().getStringExtra(ID)))
                    .commit();
        }

    }
}
