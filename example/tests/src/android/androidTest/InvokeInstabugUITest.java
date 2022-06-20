package com.instabug.example;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class InvokeInstabugUITest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void ensureInstabugInvocation() throws InterruptedException {
        Thread.sleep(10000);
        onView(withResourceName("instabug_floating_button")).perform(click());

        onView(withText("Report a bug")).perform(click());
        onView(withResourceName("instabug_edit_text_email")).perform(replaceText("inst@bug.com"));
        onView(withResourceName("instabug_bugreporting_send")).perform(click());
        onView(withResourceName("instabug_success_dialog_container")).perform(click());
    }

}
