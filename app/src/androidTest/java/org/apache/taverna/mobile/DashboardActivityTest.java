package org.apache.taverna.mobile;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.apache.taverna.mobile.ui.DashboardActivity;
import org.apache.taverna.mobile.ui.login.LoginActivity;
import org.apache.taverna.mobile.ui.usage.UsageActivity;
import org.apache.taverna.mobile.ui.userprofile.UserProfileActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class DashboardActivityTest {

    @Rule
    public ActivityTestRule<DashboardActivity> mActivityTestRule
            = new ActivityTestRule<>(DashboardActivity.class);

    @Before
    public void setUp() {
        mActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }

    /**
     * Check if the username, password and userAvatar are visible and verify when click on
     * userAvatar UserProfileActivity will open
     */
    @Test
    public void checkAllViewsVisible_and_OnClickAvatar_openUserProfileActivity() throws Exception {

        Intents.init();
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        //Please Login first otherwise it will not find username and email
        onView(withId(R.id.nav_user_avatar)).check(matches((isDisplayed())));
        onView(withId(R.id.nav_user_name)).check(matches((isDisplayed())));
        onView(withId(R.id.nav_user_email)).check(matches((isDisplayed())));

        onView(withId(R.id.nav_user_avatar)).perform(click());

        intended(hasComponent(UserProfileActivity.class.getName()));
        Intents.release();
    }

    /**
     * Checks if the Workflow fragment is launched when we click on Workflow in nav drawer
     */
    @Test
    public void onClickNavAllWorkflows_openWorkflowsFragment() throws Exception {

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_workflows));

        onView(withId(R.id.frame_container)).check(matches((isDisplayed())));

    }

    /**
     * Checks if the myWorkflow fragment is launched when we click on myWorkflow in nav drawer.
     * Without login, The app does not have any user then it also does not have any myworkflow
     * Without login it will fail
     */
    @Test
    public void onClickNavMyWorkflows_openMyWorkflowActivity() throws Exception {

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_my_workflows));

        onView(withId(R.id.frame_container)).check(matches((isDisplayed())));

    }

    /**
     * Checks if the favoriteWorkflow fragment is launched when we click on
     * favoriteWorkflow in nav drawer
     */
    @Test
    public void onClickNavFavWorkflows_openFavoriteWorkflowActivity() throws Exception {

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_favourite_workflow));

        sleep(3000);

        onView(withId(R.id.frame_container)).check(matches((isDisplayed())));

    }

    /**
     * Checks if the Announcement fragment is launched when we click on
     * announcement in nav drawer
     */
    @Test
    public void onClickNavAnnouncement_openAnnouncementActivity() throws Exception {

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_announcement));

        sleep(3000);

        onView(withId(R.id.frame_container)).check(matches((isDisplayed())));

    }

    /**
     * Checks if the usage activity is launched when we click on usage in nav drawer
     */
    @Test
    public void onClickNavUsage_openUsageActivity() throws Exception {

        Intents.init();

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_usage));

        intended(hasComponent(UsageActivity.class.getName()));

        Intents.release();
    }

    /**
     * Checks if the About dialouge is launched when we click on about in nav drawer
     */
    @Test
    public void onClickNavAbout_checkAboutDialogue() throws Exception {

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_about));

        onView(withId(R.id.about_dialouge_layout)).check(matches((isDisplayed())));

    }

    /**
     * Checks if the Licence dialouge is launched when we click on licence in nav drawer
     */
    @Test
    public void onClickNavLicence_openLicenceDialouge() throws Exception {

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.os_licences));

        onView(withText(R.string.title_nav_os_licences)).check(matches(isDisplayed()));

    }

    /**
     * Checks if the Licence dialogue is launched when we click on licence in nav drawer
     */
    @Test
    public void onClickNavApacheLicence_openApacheLicence_NoticeDialouge() throws Exception {

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.apache_licences));

        onView(withText(R.string.title_nav_apache_licences)).check(matches(isDisplayed()));

    }

    /**
     * Checks if the setting fragment is launched
     * when we click on settings in navigation drawer
     */
    @Test
    public void onClickNavSetting_openSettingFragment() throws Exception {

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings));

        onView(withId(R.id.frame_container)).check(matches((isDisplayed())));

    }

    /**
     * Checks if the login is launched when click on logout in nav drawer and click on
     * sign out in alert dialouge box
     */
    @Test
    public void onClickLogout_ClickSignOut_OpenLoginScreen() throws Exception {

        Intents.init();
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_logout));

        onView(withId(android.R.id.button1)).perform(click());

        intended(hasComponent(LoginActivity.class.getName()));

        Intents.release();
    }

    /**
     * Checks if alert dialogue box is dismiss when click on cancel in logout alert sign in box
     */
    @Test
    public void onClickLogout_clickCancel_dismissDialogueBoz() throws Exception {

        Intents.init();
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_logout));

        onView(withId(android.R.id.button2)).perform(click());

        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(Gravity.LEFT)));

        Intents.release();
    }


}