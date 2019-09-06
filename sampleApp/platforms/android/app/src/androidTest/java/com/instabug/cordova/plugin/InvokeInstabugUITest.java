package com.instabug.cordova.plugin;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.cordova.hellocordova.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class InvokeInstabugUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ensureInstabugInvocati1on() throws InterruptedException {
        Thread.sleep(1000);
        onView(withResourceName("instabug_floating_button")).perform(click());
        Thread.sleep(3000);
        onView(withText("Report a bug")).perform(click());
        onView(withResourceName("instabug_edit_text_email")).perform(replaceText("inst@bug.com"));
        onView(withResourceName("instabug_bugreporting_send")).perform(click());
        onView(withResourceName("instabug_success_dialog_container")).perform(click());
    }

}
