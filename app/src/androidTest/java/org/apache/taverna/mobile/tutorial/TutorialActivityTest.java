package org.apache.taverna.mobile.tutorial;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.ui.login.LoginActivity;
import org.apache.taverna.mobile.ui.tutorial.TutorialActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class TutorialActivityTest {

    @Rule
    public ActivityTestRule<TutorialActivity> mActivityTestRule
            = new ActivityTestRule<>(TutorialActivity.class);

    @Before
    public void setUp() {
        mActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }

    /**
     * Check all the views present are visible
     */
    @Test
    public void CheckAllViewAreVisible() throws Exception {

        onView(withId(R.id.layoutDots)).check(matches((isDisplayed())));
        onView(withId(R.id.btn_next)).check(matches((isDisplayed())));
        onView(withId(R.id.btn_skip)).check(matches((isDisplayed())));
        onView(withId(R.id.layoutDots)).check(matches((isDisplayed())));
        onView(withId(R.id.slide_pager)).check(matches((isDisplayed())));
    }

    /**
     * Checks while clicking on skip button should start login activity
     */
    @Test
    public void clickingSkip_shouldStartLoginActivity() throws Exception {

        Intents.init();
        onView(withId(R.id.btn_skip)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
        Intents.release();
    }

    /**
     * Check swipes are working on tutorial screens and on last tutorial screen while clicking on
     * GOT IT it should go to login activity
     */
    @Test
    public void clickingNext_shouldGotoNextTutorial_onClickGotIt_ShouldGoToLoginActivity()
            throws Exception {

        Intents.init();
        onView(withText(R.string.next)).check(matches(isDisplayed()));
        onView(withId(R.id.slide_pager)).perform(swipeLeft());
        onView(withId(R.id.slide_pager)).perform(swipeLeft());
        onView(withId(R.id.slide_pager)).perform(swipeLeft());
        onView(withId(R.id.slide_pager)).perform(swipeLeft());
        onView(withText("SKIP")).check(matches(not(isDisplayed())));
        onView(withText("GOT IT")).check(matches(isDisplayed()));
        onView(withText("GOT IT")).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
        Intents.release();

    }

    /**
     * First it swipes two times and then swipe back. Then this test will check while clicking
     * on the skip button should go the login activity
     */
    @Test
    public void swipeRightLeft_clickOnSkip_shouldGoToLoginActivity() throws Exception {

        Intents.init();
        onView(withText(R.string.next)).check(matches(isDisplayed()));
        onView(withId(R.id.slide_pager)).perform(swipeLeft());
        onView(withId(R.id.slide_pager)).perform(swipeLeft());
        onView(withId(R.id.slide_pager)).perform(swipeRight());
        onView(withText("SKIP")).check(matches(isDisplayed()));
        onView(withId(R.id.btn_skip)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
        Intents.release();
    }

}
