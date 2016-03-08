package org.apache.taverna.mobile.activities;

/**
 * Apache Taverna Mobile
* Copyright 2015 The Apache Software Foundation

* This product includes software developed at
* The Apache Software Foundation (http://www.apache.org/).

* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*/

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.fragments.FavoriteFragment;
import org.apache.taverna.mobile.fragments.NavigationDrawerFragment;
import org.apache.taverna.mobile.fragments.WorkflowItemFragment;
import org.apache.taverna.mobile.fragments.Workflow_viewpager;
import org.apache.taverna.mobile.utils.WorkflowOpen;

import java.io.File;

public class DashboardMainActivity extends AppCompatActivity
{

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private final int SELECT_WORKFLOW = 10;
    public static final String APP_DIRECTORY_NAME = "TavernaMobile";
    private  Dialog aboutDialog;
	private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main);

	    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
	    if (navigationView != null) {
		    setupDrawerContent(navigationView);
	    }


	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setUpWorkflowDirectory(this);
        aboutDialog = new Dialog(this);

	    /**
	     * Setting the Fragment in FrameLayout
	     */
	    if (savedInstanceState == null) {
		    Fragment view = new Workflow_viewpager();

		    getSupportFragmentManager()
				    .beginTransaction().replace(R.id.frame_container, view)
				    .commit();

	    }

    }


	/**
	 *
	 * @param navigationView Design Support NavigationView  OnClick Listener Event
	 */
	private void setupDrawerContent(final NavigationView navigationView) {
		navigationView.setNavigationItemSelectedListener(
				new NavigationView.OnNavigationItemSelectedListener() {

					@Override
					public boolean onNavigationItemSelected(MenuItem menuItem) {


						FragmentManager fragmentManager = getSupportFragmentManager();
						Fragment fragment;

						switch (menuItem.getItemId()) {
							case R.id.nav_dashboard:

								fragment = new Workflow_viewpager();
								fragmentManager.beginTransaction()
										.replace(R.id.frame_container, fragment)
										.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
										.commit();

								menuItem.setChecked(true);
								mDrawerLayout.closeDrawers();
								return true;

							case R.id.nav_openworkflow:

								Intent workflowSelectIntent =
										new Intent(Intent.ACTION_GET_CONTENT)
										.setDataAndTypeAndNormalize(Uri.parse(String.format("%s%s%s",
														Environment.getExternalStorageDirectory(),
														File.separator, APP_DIRECTORY_NAME)),
												"application/vnd.taverna.t2flow+xml");

								Intent loadWorkflowIntent = Intent.createChooser(workflowSelectIntent,
										"Choose Workflow (t2flow or xml)");
								startActivityForResult(loadWorkflowIntent, SELECT_WORKFLOW);
								menuItem.setChecked(true);
								mDrawerLayout.closeDrawers();
								return true;
							case R.id.nav_usage:

								aboutDialog.setTitle("USage");
								aboutDialog.setContentView(R.layout.usage_layout);
								aboutDialog.show();

								menuItem.setChecked(true);
								mDrawerLayout.closeDrawers();
								return true;

							case R.id.nav_about:


								TextView about = new TextView(getApplicationContext());
								about.setTextSize(21);
								about.setTextColor(Color.BLACK);
								about.setPadding(3,3,3,3);
								about.setText(getResources().getString(R.string.about));

								aboutDialog.setTitle("About Taverna Mobile");
								aboutDialog.setContentView(about);
								aboutDialog.show();

								menuItem.setChecked(true);
								mDrawerLayout.closeDrawers();
								return true;

							case R.id.nav_settings:

								startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
								overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

								menuItem.setChecked(true);
								mDrawerLayout.closeDrawers();
								return true;

							case R.id.nav_logout:

								finish();
								menuItem.setChecked(true);
								mDrawerLayout.closeDrawers();
								return true;

						}
						return true;
					}
				});
	}





    @Override
    public void onActivityResult(int requestCode , int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_WORKFLOW){
                String workflowPath = data.getData().getPath();
             //   Toast.makeText(getBaseContext(), "Path: "+workflowPath, Toast.LENGTH_LONG).show();
                String type = getMimeType(data.getData().getPath());
                if (type == "text/xml" || type == "application/vnd.taverna.t2flow+xml"){

                    new WorkflowOpen(this).execute(workflowPath);
                }else {
                    Toast.makeText(getBaseContext(), "Invalid worklow. Please try again", Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    /**
     * Return the mimetype of the file selected to be run as a workflow
     * @param url the path to the seleted file
     * @return the mimetype of the file selected
     */
    private String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }



    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    private void setUpWorkflowDirectory(Context context){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File workflowDirectory = new File(Environment.getExternalStorageDirectory()+File.separator+APP_DIRECTORY_NAME);
            if (!workflowDirectory.exists()) {
                boolean state = workflowDirectory.mkdirs();
                if (state) {
                    Toast.makeText(context, "Storage Ready", Toast.LENGTH_SHORT).show();
                    sp.edit().putString(APP_DIRECTORY_NAME, workflowDirectory.getAbsolutePath()).commit();
                    Toast.makeText(context, "Home dir: "+workflowDirectory.getAbsolutePath(), Toast.LENGTH_LONG).show();
                } else { //directory can't be created either because of restricted access or lack of an external storage media.
                    //we assume the lack of secondary storage so we have to switch to internal storage
                    //   File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.))
            //        Toast.makeText(context, "Storage Error. Directory not created", Toast.LENGTH_SHORT).show();
                }
//            workflowDirectory.list();
            }else {
          //      Toast.makeText(context, "Directory exists. Home dir: "+workflowDirectory.getAbsolutePath(), Toast.LENGTH_LONG).show();
                sp.edit().putString(APP_DIRECTORY_NAME, workflowDirectory.getAbsolutePath()).commit();
            /*else {
                File mainDir = new File(Environment.getExternalStorageDirectory() + File.separator + APP_DIRECTORY_NAME);
                if (mainDir.mkdirs())
                    sp.edit().putString(APP_DIRECTORY_NAME, mainDir.getAbsolutePath()).commit();
                else
                    Toast.makeText(context, "Workflow home not created. Permission issues", Toast.LENGTH_SHORT).show();
            }*/
            }
        }else{//use internal memory to save the data
            File home = context.getDir("Workflows", Context.MODE_PRIVATE);
            sp.edit().putString(APP_DIRECTORY_NAME, home.getAbsolutePath()).commit();
       //     Toast.makeText(context, "Home dir: "+home.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            MenuInflater mi = getMenuInflater();
                mi.inflate(R.menu.dashboard_main, menu);
          /*  //get the searchview and set the searchable configuration
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            //assuming this activity is the searchable activity
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setSubmitButtonEnabled(true);
//            searchView.setIconifiedByDefault(false);*/

            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case android.R.id.home:
			    mDrawerLayout.openDrawer(GravityCompat.START);
			    return true;
	    }
	    return super.onOptionsItemSelected(item);
    }

}
