package com.funckyhacker.githubrepoviewer.view

import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Suppress("unused")
    @get:Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

}
