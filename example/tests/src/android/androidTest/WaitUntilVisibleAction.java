package com.instabug.example;

import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

import android.view.View;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.Matcher;

import java.util.concurrent.TimeoutException;

class WaitUntilVisibleAction implements ViewAction {
    int loopTime = 400;
    long tries = 0;
    long millis;
    Matcher<View> viewMatcher;

    WaitUntilVisibleAction(final Matcher<View> viewMatcher, final long millis) {
        this.millis = millis;
        this.viewMatcher = viewMatcher;
    }

    @Override
    public Matcher<View> getConstraints() {
        return isRoot();
    }

    @Override
    public String getDescription() {
        return "wait for a specific view with the condition of <" + viewMatcher.toString() + "> during " + millis + " millis. Tries: " + this.tries + '.';
    }

    @Override
    public void perform(final UiController uiController, final View view) {
        uiController.loopMainThreadUntilIdle();
        final long endTime = System.currentTimeMillis() + millis;

        do {
            for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                if (viewMatcher.matches(child)) {
                    return;
                }
            }

            this.tries++;
            uiController.loopMainThreadForAtLeast(loopTime);
        }
        while (System.currentTimeMillis() < endTime);

        throw new PerformException.Builder()
                .withActionDescription(this.getDescription())
                .withViewDescription(HumanReadables.describe(view))
                .withCause(new TimeoutException())
                .build();
    }
}
