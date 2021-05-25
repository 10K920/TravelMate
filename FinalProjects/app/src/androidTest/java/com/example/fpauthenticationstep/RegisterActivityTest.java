package com.example.fpauthenticationstep;


import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import com.example.fpauthenticationstep.AuthenticationActivities.RegisterActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;

// tests if entering inputs to edittext correctly saves data to it
@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(RegisterActivity.class);

    @Test
    public void testInput() {
        onView(withId(R.id.usernameIn)).perform(typeText("Tester"), closeSoftKeyboard());
        onView(withId(R.id.emailIn)).perform(typeText("Tester@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.pwIn)).perform(typeText("123123"), closeSoftKeyboard());
        onView(withText("Register")).perform(click());
        onView(withId(R.id.usernameIn)).check(matches(withText("Tester")));
        onView(withId(R.id.emailIn)).check(matches(withText("Tester@gmail.com")));
        onView(withId(R.id.pwIn)).check(matches(withText("123123")));
    }
}
