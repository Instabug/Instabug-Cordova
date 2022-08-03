package com.instabug.example;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import android.os.Environment;
import java.io.File;

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
        onView(isRoot()).perform(new WaitUntilVisibleAction(withResourceName("instabug_floating_button"), 5000));
        onView(withResourceName("instabug_floating_button")).perform(click());

        Thread.sleep(1000);
        onView(isRoot()).perform(new WaitUntilVisibleAction(withText("Report a bug"), 3000));
        onView(withText("Report a bug")).perform(click());

        onView(isRoot()).perform(new WaitUntilVisibleAction(withResourceName("instabug_edit_text_email"), 1000));
        onView(withResourceName("instabug_edit_text_email")).perform(replaceText("inst@bug.com"));

        onView(isRoot()).perform(new WaitUntilVisibleAction(withResourceName("instabug_bugreporting_send"), 1000));
        onView(withResourceName("instabug_bugreporting_send")).perform(click());

        onView(isRoot()).perform(new WaitUntilVisibleAction(withResourceName("instabug_success_dialog_container"), 5000));
    }
}
