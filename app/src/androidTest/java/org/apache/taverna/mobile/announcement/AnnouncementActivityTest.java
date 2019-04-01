/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.taverna.mobile.announcement;

import org.apache.taverna.mobile.FakeRemoteDataSource;
import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.SingleFragmentActivity;
import org.apache.taverna.mobile.TestComponentRule;
import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.ui.anouncements.AnnouncementFragment;
import org.apache.taverna.mobile.utils.RecyclerViewItemCountAssertion;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AnnouncementActivityTest {

    private Announcements announcements;
    private Map<String, String> option;

    private final TestComponentRule component =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());
    private final ActivityTestRule<SingleFragmentActivity> mAnnouncementActivityTestRule =
            new ActivityTestRule<SingleFragmentActivity>(SingleFragmentActivity.class,
                    false, false) {
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
        announcements = FakeRemoteDataSource.getAnnouncements();
        option = new HashMap<>();
        option.put("order", "reverse");
        option.put("page", String.valueOf(1));
    }

    @Test
    public void CheckIfRecyclerViewIsLoaded() {


        Mockito.when(component.getMockDataManager().getAllAnnouncement(option))
                .thenReturn(Observable.just(announcements));
        mAnnouncementActivityTestRule.launchActivity(null);
        mAnnouncementActivityTestRule.getActivity().setFragment(new AnnouncementFragment());

        onView(withId(R.id.rv_movies)).check(new RecyclerViewItemCountAssertion(5));



    }


}
