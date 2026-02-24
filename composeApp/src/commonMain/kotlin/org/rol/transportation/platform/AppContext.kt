package org.rol.transportation.platform

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppContext {
    fun init(context: Any)
    fun get(): Any
}
