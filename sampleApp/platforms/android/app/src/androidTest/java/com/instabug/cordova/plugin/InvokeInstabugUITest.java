package com.instabug.cordova.plugin;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.instabug.library.Instabug;
import com.instabug.library.ui.onboarding.WelcomeMessage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        Thread.sleep(5000);
        Instabug.setWelcomeMessageState(WelcomeMessage.State.DISABLED);
        int count = 100;
        while (count > 0) {
            count --;
            try {
                onView(withResourceName("instabug_floating_button")).perform(click());
                Thread.sleep(2000);
            } catch (Exception e) {
                break;
            }
        }
        onView(withText("Report a bug")).perform(click());
        onView(withResourceName("instabug_edit_text_email")).perform(replaceText("inst@bug.com"));
        onView(withResourceName("instabug_bugreporting_send")).perform(click());
        onView(withResourceName("instabug_success_dialog_container")).perform(click());
    }

}
