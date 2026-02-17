package org.rol.transportation.di

import com.russhwolf.settings.Settings
import org.koin.dsl.module
import org.rol.transportation.data.local.TokenManager
import org.rol.transportation.data.remote.api.AuthApi
import org.rol.transportation.data.remote.api.ChecklistApi
import org.rol.transportation.data.remote.api.ChecklistDocumentApi
import org.rol.transportation.data.remote.api.DocumentInspectionApi
import org.rol.transportation.data.remote.api.DriverApi
import org.rol.transportation.data.remote.api.FirstAidApi
import org.rol.transportation.data.remote.api.InspectionSheetApi
import org.rol.transportation.data.remote.api.LightsAlarmApi
import org.rol.transportation.data.remote.api.NotificationApi
import org.rol.transportation.data.remote.api.PassengerApi
import org.rol.transportation.data.remote.api.SeatBeltsApi
import org.rol.transportation.data.remote.api.SpillKitApi
import org.rol.transportation.data.remote.api.StorageApi
import org.rol.transportation.data.remote.api.ToolsInspectionApi
import org.rol.transportation.data.remote.api.TripApi
import org.rol.transportation.data.remote.api.TripServicesApi
import org.rol.transportation.domain.repository.AuthRepository
import org.rol.transportation.domain.repository.AuthRepositoryImpl
import org.rol.transportation.domain.repository.ChecklistDocumentRepository
import org.rol.transportation.domain.repository.ChecklistDocumentRepositoryImpl
import org.rol.transportation.domain.repository.ChecklistRepository
import org.rol.transportation.domain.repository.ChecklistRepositoryImpl
import org.rol.transportation.domain.repository.DocumentInspectionRepository
import org.rol.transportation.domain.repository.DocumentInspectionRepositoryImpl
import org.rol.transportation.domain.repository.DriverRepository
import org.rol.transportation.domain.repository.DriverRepositoryImpl
import org.rol.transportation.domain.repository.FirstAidRepository
import org.rol.transportation.domain.repository.FirstAidRepositoryImpl
import org.rol.transportation.domain.repository.InspectionSheetRepository
import org.rol.transportation.domain.repository.InspectionSheetRepositoryImpl
import org.rol.transportation.domain.repository.LightsAlarmRepository
import org.rol.transportation.domain.repository.LightsAlarmRepositoryImpl
import org.rol.transportation.domain.repository.NotificationRepository
import org.rol.transportation.domain.repository.NotificationRepositoryImpl
import org.rol.transportation.domain.repository.PassengerRepository
import org.rol.transportation.domain.repository.PassengerRepositoryImpl
import org.rol.transportation.domain.repository.SeatBeltsRepository
import org.rol.transportation.domain.repository.SeatBeltsRepositoryImpl
import org.rol.transportation.domain.repository.SpillKitRepository
import org.rol.transportation.domain.repository.SpillKitRepositoryImpl
import org.rol.transportation.domain.repository.StorageRepository
import org.rol.transportation.domain.repository.StorageRepositoryImpl
import org.rol.transportation.domain.repository.ToolsInspectionRepository
import org.rol.transportation.domain.repository.ToolsInspectionRepositoryImpl
import org.rol.transportation.domain.repository.TripRepository
import org.rol.transportation.domain.repository.TripRepositoryImpl
import org.rol.transportation.domain.repository.TripServicesRepository
import org.rol.transportation.domain.repository.TripServicesRepositoryImpl

val dataModule = module {
    // Settings (Multiplataforma)
    single { Settings() }

    single { TokenManager(get()) }

    // API
    single { AuthApi(get()) }
    single { TripApi(get()) }
    single { ChecklistApi(get()) }
    single { ChecklistDocumentApi(get()) }
    single { StorageApi(get()) }
    single { InspectionSheetApi(get()) }
    single { DocumentInspectionApi(get()) }
    single { LightsAlarmApi(get()) }
    single { SeatBeltsApi(get()) }
    single { ToolsInspectionApi(get()) }
    single { FirstAidApi(get()) }
    single { SpillKitApi(get()) }
    single { PassengerApi(get()) }
    single { TripServicesApi(get()) }
    single { DriverApi(get()) }
    single { NotificationApi(get()) }


    // Repository
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<TripRepository> { TripRepositoryImpl(get()) }
    single<ChecklistRepository> { ChecklistRepositoryImpl(get()) }
    single<ChecklistDocumentRepository> { ChecklistDocumentRepositoryImpl(get()) }
    single<StorageRepository> { StorageRepositoryImpl(get()) }
    single<InspectionSheetRepository> { InspectionSheetRepositoryImpl(get()) }
    single<DocumentInspectionRepository> { DocumentInspectionRepositoryImpl(get()) }
    single<LightsAlarmRepository> { LightsAlarmRepositoryImpl(get()) }
    single<SeatBeltsRepository> { SeatBeltsRepositoryImpl(get()) }
    single<ToolsInspectionRepository> { ToolsInspectionRepositoryImpl(get()) }
    single<FirstAidRepository> { FirstAidRepositoryImpl(get()) }
    single<SpillKitRepository> { SpillKitRepositoryImpl(get()) }
    single<PassengerRepository> { PassengerRepositoryImpl(get()) }
    single<TripServicesRepository> { TripServicesRepositoryImpl(get()) }
    single<DriverRepository> { DriverRepositoryImpl(get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(get()) }

}
