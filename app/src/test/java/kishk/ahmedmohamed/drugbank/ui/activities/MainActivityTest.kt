package kishk.ahmedmohamed.drugbank.ui.activities

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kishk.ahmedmohamed.drugbank.R
import kishk.ahmedmohamed.drugbank.ui.fragments.drug.DrugRecyclerViewAdapter
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private lateinit var navController: NavController

    @Before
    fun init() {
        hiltRule.inject()
        activityScenario = ActivityScenario.launch(MainActivity::class.java).onActivity {
            navController = Navigation.findNavController(it.findViewById(R.id.nav_host_frame))
        }
    }

    @After
    fun teardown() {
        activityScenario.close()
    }

    @Test
    fun `MainActivity should contain visible toolbar and nav host`() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(
            allOf(
                withId(R.id.nav_host_frame),
                withTagValue(`is`("NavHostFrame"))
            )
        ).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun `Start destination should be drugsListFragment`() {
        activityScenario.onActivity {
            assertEquals(R.id.drugsListFragment, navController.currentDestination?.id)
        }
        onView(
            allOf(
                withId(R.id.list),
                withTagValue(`is`("DrugsList"))
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun `When clicking on DrugItem, current destination should be drugInfoFragment`() {
        onView(withId(R.id.list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<DrugRecyclerViewAdapter.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )
        activityScenario.onActivity {
            assertEquals(R.id.drugInfoFragment, navController.currentDestination?.id)
        }
    }

    @Test
    fun `When clicking on DrugItem, actionBar's title should be as the name of the clicked item`() {
        onView(withId(R.id.list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<DrugRecyclerViewAdapter.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )
        activityScenario.onActivity {
            assertEquals("drug 0", it.supportActionBar?.title)
        }
    }
}