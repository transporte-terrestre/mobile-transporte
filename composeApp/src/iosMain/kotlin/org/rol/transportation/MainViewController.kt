package org.rol.transportation

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform
import org.rol.transportation.di.appModule
import org.rol.transportation.di.dataModule
import org.rol.transportation.di.imageModule
import org.rol.transportation.di.networkModule

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}

fun initKoin() {
    // Esta es la forma más segura en Kotlin Multiplatform actual
    val koinApp = KoinPlatform.getKoinOrNull()
    if (koinApp == null) {
        startKoin {
            modules(
                networkModule,
                dataModule,
                appModule,
                imageModule
            )
        }
    }
}