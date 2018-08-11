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
package org.apache.taverna.mobile.login;

import org.apache.taverna.mobile.FakeRemoteDataSource;
import org.apache.taverna.mobile.TestComponentRule;

import org.apache.taverna.mobile.R;

import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.ui.login.LoginActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import io.reactivex.Observable;
import retrofit2.HttpException;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    private User testUser;


    private final TestComponentRule component =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());
    private final ActivityTestRule<LoginActivity> mLoginActivityActivityTestRule =
            new ActivityTestRule<LoginActivity>(LoginActivity.class, false, false) {
                @Override
                protected Intent getActivityIntent() {

                    return new Intent(InstrumentationRegistry.getTargetContext(), LoginActivity
                            .class);
                }
            };

    /**
     * TestComponentRule needs to go first to make sure the Dagger ApplicationTestComponent is set
     * in the Application before any Activity is launched.
     */
    @Rule
    public final TestRule chain = RuleChain.outerRule(component)
            .around(mLoginActivityActivityTestRule);

    @Before
    public void setUp() {
        testUser = FakeRemoteDataSource.getLoginUser();
    }

    /**
     * Checks if all the views are visible on the login activity
     */
    @Test
    public void checkAllViewAreVisible() throws Exception {
        onView(withId(R.id.logo)).check(matches(isDisplayed()));
        onView(withId(R.id.tvAppName)).check(matches(withText(R.string.app_name)));
        onView(withId(R.id.loginlayout)).check(matches(isDisplayed()));
        onView(withId(R.id.myExperimentIcon)).check(matches(isDisplayed()));
        onView(withId(R.id.input_layout_email)).check(matches(isDisplayed()));
        onView(withId(R.id.input_layout_password)).check(matches(isDisplayed()));
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.bLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.bRegister)).check(matches(isDisplayed()));
    }

    /**
     * This test demonstrates that when user enters the invalid username and password both then
     * it verify if the correct snackbar is shown or not
     */
    @Test
    public void invalidLoginCredentials_showErrorSnackBar() throws Exception {

        testUser = new User();
        Mockito.when(component.getMockDataManager()
                .getLoginUserDetail("Basic cG9zdG1hbjpwYXNzd29yZA==", true))
                .thenReturn(Observable.<User>error(new Throwable()));
        mLoginActivityActivityTestRule.launchActivity(null);

        onView(withId(R.id.etEmail))
                .perform(typeText("postman"), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText("password"), closeSoftKeyboard());

        onView(withId(R.id.bLogin)).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("Please enter valid credential")))
                .check(matches(isDisplayed()));
    }

    /**
     * This test demonstrates that when user enters the valid/invalid password and empty username
     * then it verify if the correct snackbar is shown or not
     */
    @Test
    public void emptyUsername_invalidCredentials_showErrorSnackBar() throws Exception {

        testUser = new User();
        Mockito.when(component.getMockDataManager()
                .getLoginUserDetail("Basic OnRlc3QNCg==", true))
                .thenReturn(Observable.just(testUser));
        mLoginActivityActivityTestRule.launchActivity(null);

        onView(withId(R.id.etEmail))
                .perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.bLogin)).perform(click());
        sleep(2000);

        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("Please enter valid credential")))
                .check(matches(isDisplayed()));
    }

    /**
     * This test demonstrates that when user enters the valid/invalid username and empty password
     * then it verify if the correct snackbar is shown or not
     */
    @Test
    public void invalidPassword_invalidLoginCredentials_showErrorSnackBar() throws Exception {

        testUser = new User();
        Mockito.when(component.getMockDataManager()
                .getLoginUserDetail("Basic dGVzdDoNCg==", true))
                .thenReturn(Observable.just(testUser));
        mLoginActivityActivityTestRule.launchActivity(null);

        onView(withId(R.id.etEmail))
                .perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText(""), closeSoftKeyboard());

        onView(withId(R.id.bLogin)).perform(click());
        sleep(2000);

        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("Please enter valid credential")))
                .check(matches(isDisplayed()));
    }

    /**
     * This test demonstrates that when user does not enters anything inside the username
     * and password then it verify if the correct snackbar is shown or not
     */
    @Test
    public void nullLoginCredentials_showErrorSnackBar() throws Exception {

        mLoginActivityActivityTestRule.launchActivity(null);

        onView(withId(R.id.etEmail))
                .perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText(""), closeSoftKeyboard());

        onView(withId(R.id.bLogin)).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("Please enter valid credential")))
                .check(matches(isDisplayed()));
    }
}