package org.apache.taverna.mobile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.apache.taverna.mobile.ui.base.BaseActivity;

public class SingleFragmentActivity extends BaseActivity {
    FrameLayout content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = new FrameLayout(this);
        content.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        content.setId(R.id.container1);
        setContentView(content);
    }

    public void setFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container1, fragment, "TEST")
                .commit();
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container1, fragment).commit();
    }

}
