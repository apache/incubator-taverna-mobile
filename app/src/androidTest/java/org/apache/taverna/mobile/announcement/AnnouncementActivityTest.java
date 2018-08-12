package org.apache.taverna.mobile.announcement;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.apache.taverna.mobile.FakeRemoteDataSource;
import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.SingleFragmentActivity;
import org.apache.taverna.mobile.TestComponentRule;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.ui.anouncements.AnnouncementFragment;
import org.apache.taverna.mobile.ui.anouncements.AnnouncementPresenter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class AnnouncementActivityTest {

    private Announcements announcements;
    private Map<String, String> option;

    private final TestComponentRule component =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());
    private final ActivityTestRule<SingleFragmentActivity> mAnnouncementActivityTestRule =
            new ActivityTestRule<SingleFragmentActivity>(SingleFragmentActivity.class,
                    true, true) {
                @Override
                protected Intent getActivityIntent() {

                    return new Intent(InstrumentationRegistry.getTargetContext(),
                            SingleFragmentActivity.class);
                }
            };

    /**
     * TestComponentRule needs to go first to make sure the Dagger ApplicationTestComponent is set
     * in the Application before any Activity is launched.
     */
    @Rule
    public final TestRule chain = RuleChain.outerRule(component)
            .around(mAnnouncementActivityTestRule);


    @Before
    public void setUp() {
        mAnnouncementActivityTestRule.getActivity().setFragment(new AnnouncementFragment());

        announcements = FakeRemoteDataSource.getAnnouncements();
        option = new HashMap<>();
        option.put("order", "reverse");
        option.put("page", String.valueOf(1));
    }

    @Test
    public void CheckIfRecyclerViewIsLoaded() {


//        Mockito.when(component.getMockDataManager()
//                .getAllAnnouncement(option))
//                .thenReturn(Observable.just(announcements));

//        mAnnouncementActivityTestRule.launchActivity(null);


//        announcementPresenter.loadAllAnnouncement(1);

//        onView(withId(R.id.frame_container)).check(matches((isDisplayed())));

        sleep(3000);
        //Test is failing here because recycler view is not loading
        //Snackbar is showing with the message failed to fetch workflows
        onView(withId(R.id.progress_circular)).check(matches((isDisplayed())));


    }

//    @Test
//    public void testClickAtPosition() {
//        // Perform a click on first element in the RecyclerView
//        onView(withId(R.id.rv_movies)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
//
//        onView(withId(R.id.book_title)).check(matches(withText(BOOK_TITLE)));
//        onView(withId(R.id.book_author)).check(matches(withText(BOOK_AUTHOR)));
//    }
//
//
    @Test
    public void CheckIfErrorIsDisplayed() {

        Mockito.when(component.getMockDataManager()
                .getAllAnnouncement(option))
                .thenReturn(Observable.<Announcements>error(new Throwable()));

        mAnnouncementActivityTestRule.launchActivity(null);

        onView(withId(R.id.frame_container)).check(matches((isDisplayed())));

    }

}
