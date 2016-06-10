package org.apache.taverna.mobile.fragments;

import org.apache.taverna.mobile.R;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajan on 8/3/16.
 */
public class WorkflowViewpager extends Fragment {

    private final String LOG_TAG = getClass().getSimpleName();

    ViewPager viewPager;
    TabLayout tabLayout;


    public static WorkflowViewpager getInstance(int position) {
        WorkflowViewpager myFragment = new WorkflowViewpager();
        Bundle args = new Bundle();
        args.putInt("position", position);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View workflow_pager = inflater.inflate(R.layout.viewpager_workflow, container, false);

        /**
         * Setting the tool bar in MainActivity for all fragment
         */
        Toolbar toolbar = (Toolbar) workflow_pager.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) workflow_pager.findViewById(R.id.mviewpager);
        final Adapter adapter = new Adapter(getChildFragmentManager());

        /**
         * Dynamically Adding tabs
         * To add the new Tab "Go to res/values/category_id and add new title and category id"
         */
        adapter.addFragment(new WorkflowItemFragment(), getResources().getString(R.string
                .title_explore));
        adapter.addFragment(new FavoriteFragment(), getResources().getString(R.string
                .title_favorite));

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout = (TabLayout) workflow_pager.findViewById(R.id.mtablayout);
        tabLayout.setupWithViewPager(viewPager);

        return workflow_pager;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(LOG_TAG, "Workflow_viewpager.onCreate");

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
