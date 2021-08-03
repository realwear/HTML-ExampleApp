package com.realwear.demowebapp

import androidx.test.platform.app.InstrumentationRegistry

import org.junit.Test

import org.junit.Assert.*

/**
 * Instrumented index.html, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under index.html.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.realwear.demowebapp", appContext.packageName)
    }
}