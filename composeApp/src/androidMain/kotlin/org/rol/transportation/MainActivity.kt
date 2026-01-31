package org.rol.transportation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.GlobalContext.startKoin
import org.rol.transportation.di.appModule
import org.rol.transportation.di.dataModule
import org.rol.transportation.di.imageModule
import org.rol.transportation.di.networkModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Koin solo si no está inicializado
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidContext(this@MainActivity)
                modules(networkModule, dataModule, appModule, imageModule)
            }
        }

        setContent {
            App()
        }
    }
}