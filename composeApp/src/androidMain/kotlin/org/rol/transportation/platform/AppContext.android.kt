package org.rol.transportation.platform

import android.content.Context

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object AppContext {
    private lateinit var appContext: Context

    actual fun init(context: Any) {
        appContext = (context as Context).applicationContext
    }

    actual fun get(): Any = appContext
}
