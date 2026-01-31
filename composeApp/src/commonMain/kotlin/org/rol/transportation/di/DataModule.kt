package org.rol.transportation.di

import com.russhwolf.settings.Settings
import kotlinx.coroutines.NonCancellable.get
import org.koin.dsl.module
import org.rol.transportation.data.local.TokenManager
import org.rol.transportation.data.remote.api.AuthApi
import org.rol.transportation.data.remote.api.TripApi
import org.rol.transportation.domain.repository.AuthRepository
import org.rol.transportation.domain.repository.AuthRepositoryImpl
import org.rol.transportation.domain.repository.TripRepository
import org.rol.transportation.domain.repository.TripRepositoryImpl

val dataModule = module {
    // Settings (Multiplataforma)
    single { Settings() }

    // Token Manager
    single { TokenManager(get()) }

    // API
    single { AuthApi(get()) }

    single { TripApi(get()) }


    // Repository
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<TripRepository> { TripRepositoryImpl(get()) }
}