package com.example.fpauthenticationstep

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.runner.AndroidJUnit4
import com.example.fpauthenticationstep.AuthenticationActivities.LoginActivity
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// checks if starting a message activity with intent from the menu activity correctly works
@RunWith(AndroidJUnit4::class)
class menuToMessageCheck {
    @get:Rule
    var intentsRule: IntentsTestRule<LoginActivity> = IntentsTestRule(LoginActivity::class.java)

    @Test
    fun verifyLogInToMenuActivity() {
       Espresso.onView(ViewMatchers.withId(R.id.emailIn))
           .perform(ViewActions.typeText("Witch@gmail.com"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.pwIn))
            .perform(ViewActions.typeText("123123"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withText("Log In")).perform(ViewActions.click())

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasComponent(ComponentNameMatchers.hasShortClassName(".PathActivities.Menu"))
            )
        )

        onView(ViewMatchers.withId(R.id.toCurM_btn)).perform(ViewActions.click())

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasComponent(ComponentNameMatchers.hasShortClassName(".MessagingActivities.CurrentMessages"))
            )
        )

    }
}