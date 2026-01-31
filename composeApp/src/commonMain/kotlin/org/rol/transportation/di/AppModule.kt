package org.rol.transportation.di

import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import org.rol.transportation.domain.usecase.GetTripDetailUseCase
import org.rol.transportation.domain.usecase.GetTripsUseCase
import org.rol.transportation.domain.usecase.IsLoggedInUseCase
import org.rol.transportation.domain.usecase.LoginUseCase
import org.rol.transportation.domain.usecase.LogoutUseCase
import org.rol.transportation.domain.usecase.ValidateEmailUseCase
import org.rol.transportation.presentation.home_trip.HomeViewModel
import org.rol.transportation.presentation.home_trip_detail.TripDetailViewModel
import org.rol.transportation.presentation.profile.ProfileViewModel
import org.rol.transportation.presentation.login.LoginViewModel


val appModule = module {
    // Use Cases
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { IsLoggedInUseCase(get()) }
    factory { ValidateEmailUseCase() }
    factory { GetTripsUseCase(get()) }
    factory { GetTripDetailUseCase(get()) }

    // ViewModels
    viewModel { LoginViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { parameters ->
        TripDetailViewModel(
            getTripDetailUseCase = get(),
            tripId = parameters.get()
        )
    }

}