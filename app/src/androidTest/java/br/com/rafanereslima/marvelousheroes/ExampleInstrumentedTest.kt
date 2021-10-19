package br.com.rafanereslima.marvelousheroes

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.rafanereslima.marvelousheroes.presentation.activities.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        Thread.sleep(10000)
        onView(withId(R.id.listCharactersRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            0,
            ViewActions.click()
        ))
        Thread.sleep(5000)
        swipeDownRecycler()
        Thread.sleep(10000)
        onView(withContentDescription("Navigate up")).perform(ViewActions.click())
        Thread.sleep(1000)
        onView(withContentDescription("Navigate up")).perform(ViewActions.click())
    }

    private fun swipeDownRecycler() {
        onView(withId(R.id.listCharactersRecyclerView)).perform(ViewActions.swipeDown())
        Thread.sleep(1000)
    }
}