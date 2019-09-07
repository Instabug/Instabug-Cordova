package com.instabug.cordova.plugin;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

import com.instabug.library.Instabug;
import com.instabug.library.ui.onboarding.WelcomeMessage;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import io.cordova.hellocordova.MainActivity;
import io.cordova.hellocordova.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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
        onView(withResourceName("instabug_floating_button")).perform(click());
        // onView(withText("Report a bug")).perform(click());
        // onView(withResourceName("instabug_edit_text_email")).perform(replaceText("inst@bug.com"));
        // onView(withResourceName("instabug_bugreporting_send")).perform(click());
        // onView(withResourceName("instabug_success_dialog_container")).perform(click());
    }

    public static ViewAction click() {
        return actionWithAssertions(
                new GeneralClickAction(
                        Tap.SINGLE,
                        GeneralLocation.VISIBLE_CENTER,
                        Press.FINGER,
                        InputDevice.SOURCE_UNKNOWN,
                        MotionEvent.BUTTON_PRIMARY));
    }

}
