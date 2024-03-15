package edu.ucsd.cse110.successorator;

import android.view.View;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import org.junit.Test;

import edu.ucsd.cse110.successorator.ui.tasklist.SuccessoratorTaskListFragment;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MainActivityTest {
    @Test
    public void testTaskListFragmentIsDisplayed() {
        Espresso.onView(withId(R.id.fragment_container))
                .check(matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.task_list))
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testAddTaskFragmentIsDisplayedAfterButtonClick() {
        Espresso.onView(withId(R.id.add_task_button))
                .perform(ViewActions.click());
        // ensure create task fragment appears
    }

    @Test
    public void testSwitchContextFragmentIsDisplayedAfterButtonClick() {
        Espresso.onView(withId(R.id.hamburger_menu_icon))
                .perform(ViewActions.click());
        // ensure switch context fragment appears
    }
}