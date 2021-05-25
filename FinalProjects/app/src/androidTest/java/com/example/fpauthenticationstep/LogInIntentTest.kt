package com.example.fpauthenticationstep

import android.provider.ContactsContract.Directory.PACKAGE_NAME
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.runner.AndroidJUnit4
import com.example.fpauthenticationstep.AuthenticationActivities.LoginActivity
import com.example.fpauthenticationstep.AuthenticationActivities.RegisterActivity
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.EnumSet.allOf

// Tests if login properly works with a valid email address and pw
@RunWith(AndroidJUnit4::class)
class LogInIntentTest {

    @get:Rule
    var intentsRule: IntentsTestRule<LoginActivity> = IntentsTestRule(LoginActivity::class.java)

    @Test
    fun verifyLogInToMenuActivity() {
        onView(withId(R.id.emailIn)).perform(typeText("Witch@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.pwIn)).perform(typeText("123123"), closeSoftKeyboard())
        onView(withText("Log In")).perform(click())

        intended(
            CoreMatchers.allOf(
            hasComponent(hasShortClassName(".PathActivities.Menu"))
        ))

    }
}