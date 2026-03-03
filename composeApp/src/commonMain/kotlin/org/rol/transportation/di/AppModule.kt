package org.rol.transportation.di


import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.rol.transportation.domain.usecase.DeleteSegmentUseCase
import org.rol.transportation.domain.usecase.GetChecklistDocumentUseCase
import org.rol.transportation.domain.usecase.GetDocumentInspectionUseCase
import org.rol.transportation.domain.usecase.GetDriverDocumentsUseCase
import org.rol.transportation.domain.usecase.GetFirstAidUseCase
import org.rol.transportation.domain.usecase.GetInspectionSheetUseCase
import org.rol.transportation.domain.usecase.GetLightsAlarmUseCase
import org.rol.transportation.domain.usecase.GetLocationUseCase
import org.rol.transportation.domain.usecase.GetNextStepUseCase
import org.rol.transportation.domain.usecase.GetNotificationsUseCase
import org.rol.transportation.domain.usecase.GetPassengersUseCase
import org.rol.transportation.domain.usecase.GetSeatBeltsUseCase
import org.rol.transportation.domain.usecase.GetSegmentsUseCase
import org.rol.transportation.domain.usecase.GetSpillKitUseCase
import org.rol.transportation.domain.usecase.GetToolsInspectionUseCase
import org.rol.transportation.domain.usecase.ScanDnisPhotosUseCase
import org.rol.transportation.domain.usecase.GetTripChecklistUseCase
import org.rol.transportation.domain.usecase.GetTripDetailUseCase
import org.rol.transportation.domain.usecase.GetTripsPagedUseCase
import org.rol.transportation.domain.usecase.IsLoggedInUseCase
import org.rol.transportation.domain.usecase.LoginUseCase
import org.rol.transportation.domain.usecase.LogoutUseCase
import org.rol.transportation.domain.usecase.RegisterArrivalUseCase
import org.rol.transportation.domain.usecase.RegisterCheckpointUseCase
import org.rol.transportation.domain.usecase.RegisterDepartureUseCase
import org.rol.transportation.domain.usecase.RegisterRestUseCase
import org.rol.transportation.domain.usecase.RegisterStopUseCase
import org.rol.transportation.domain.usecase.UpdateSegmentUseCase
import org.rol.transportation.domain.usecase.UploadImageUseCase
import org.rol.transportation.domain.usecase.UpsertChecklistDocumentUseCase
import org.rol.transportation.domain.usecase.UpsertDocumentInspectionUseCase
import org.rol.transportation.domain.usecase.UpsertFirstAidUseCase
import org.rol.transportation.domain.usecase.UpsertInspectionSheetUseCase
import org.rol.transportation.domain.usecase.UpsertLightsAlarmUseCase
import org.rol.transportation.domain.usecase.UpsertSeatBeltsUseCase
import org.rol.transportation.domain.usecase.UpsertSpillKitUseCase
import org.rol.transportation.domain.usecase.UpsertToolsInspectionUseCase
import org.rol.transportation.domain.usecase.ValidateEmailUseCase
import org.rol.transportation.domain.usecase.VerifyTripChecklistUseCase
import org.rol.transportation.presentation.checklist_document_inspection.DocumentInspectionViewModel
import org.rol.transportation.presentation.checklist_first_aid.FirstAidViewModel
import org.rol.transportation.presentation.checklist_inspection_sheet.InspectionSheetViewModel
import org.rol.transportation.presentation.checklist_item_detail.ChecklistItemDetailViewModel
import org.rol.transportation.presentation.checklist_lights_alarm.LightsAlarmViewModel
import org.rol.transportation.presentation.checklist_seat_belts.SeatBeltsViewModel
import org.rol.transportation.presentation.checklist_spill_kit.SpillKitViewModel
import org.rol.transportation.presentation.checklist_tools_inspection.ToolsInspectionViewModel
import org.rol.transportation.presentation.driver_documents.DriverDocumentsViewModel
import org.rol.transportation.presentation.home.HomeMenuViewModel
import org.rol.transportation.presentation.home_trip.HomeViewModel
import org.rol.transportation.presentation.home_trip_detail.TripDetailViewModel
import org.rol.transportation.presentation.home_trip_detail_checklist.ChecklistViewModel
import org.rol.transportation.presentation.home_trip_detail_passenger.PassengerViewModel
import org.rol.transportation.presentation.home_trip_detail_services.TripServicesViewModel
import org.rol.transportation.presentation.home_trip_detail_services.register_arrival.RegisterArrivalViewModel
import org.rol.transportation.presentation.home_trip_detail_services.register_checkpoint.RegisterCheckpointViewModel
import org.rol.transportation.presentation.home_trip_detail_services.register_departure.RegisterDepartureViewModel
import org.rol.transportation.presentation.home_trip_detail_services.register_rest.RegisterRestViewModel
import org.rol.transportation.presentation.home_trip_detail_services.register_stop.RegisterStopViewModel
import org.rol.transportation.presentation.home_trip_detail_services.scan_passenger_list.ScanPassengerListViewModel
import org.rol.transportation.presentation.home_trip_detail_services.scan_passenger_photo.ScanPassengerViewModel
import org.rol.transportation.presentation.login.LoginViewModel
import org.rol.transportation.presentation.notifications.NotificationsViewModel
import org.rol.transportation.presentation.profile.ProfileViewModel

val appModule = module {
    // Use Cases
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { IsLoggedInUseCase(get()) }
    factory { ValidateEmailUseCase() }
    factory { GetTripsPagedUseCase(get()) }
    factory { org.rol.transportation.domain.usecase.GetLightTripsPagedUseCase(get()) }
    factory { GetTripDetailUseCase(get()) }
    factory { GetTripChecklistUseCase(get()) }
    factory { GetChecklistDocumentUseCase(get()) }
    factory { UpsertChecklistDocumentUseCase(get()) }
    factory { UploadImageUseCase(get()) }
    factory { GetInspectionSheetUseCase(get()) }
    factory { UpsertInspectionSheetUseCase(get()) }
    factory { GetDocumentInspectionUseCase(get()) }
    factory { UpsertDocumentInspectionUseCase(get()) }
    factory { GetLightsAlarmUseCase(get()) }
    factory { UpsertLightsAlarmUseCase(get()) }
    factory { ScanDnisPhotosUseCase(get()) }
    factory { GetSeatBeltsUseCase(get()) }
    factory { UpsertSeatBeltsUseCase(get()) }
    factory { GetToolsInspectionUseCase(get()) }
    factory { UpsertToolsInspectionUseCase(get()) }
    factory { GetFirstAidUseCase(get()) }
    factory { UpsertFirstAidUseCase(get()) }
    factory { GetSpillKitUseCase(get()) }
    factory { UpsertSpillKitUseCase(get()) }
    factory { GetPassengersUseCase(get()) }
    factory { GetSegmentsUseCase(get()) }
    factory { GetNextStepUseCase(get()) }
    factory { RegisterDepartureUseCase(get()) }
    factory { RegisterArrivalUseCase(get()) }
    factory { RegisterCheckpointUseCase(get()) }
    factory { RegisterStopUseCase(get()) }
    factory { RegisterRestUseCase(get()) }
    factory { UpdateSegmentUseCase(get()) }
    factory { DeleteSegmentUseCase(get()) }
    factory { VerifyTripChecklistUseCase(get()) }
    factory { GetDriverDocumentsUseCase(get(), get()) }
    factory { GetNotificationsUseCase(get(), get()) }
    factory { GetLocationUseCase(get()) }


    // ViewModels
    viewModel { LoginViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { org.rol.transportation.presentation.checklist_trips.ChecklistTripsViewModel(get()) }
    viewModel { HomeMenuViewModel()}
    viewModel { DriverDocumentsViewModel(get()) }
    viewModel { NotificationsViewModel(get()) }

    viewModel { parameters ->
        TripDetailViewModel(
            getTripDetailUseCase = get(),
            tripId = parameters.get(),
            getTripChecklistUseCase = get()
        )
    }

    viewModel { parameters ->
        ChecklistViewModel(
            getTripChecklistUseCase = get(),
            getVerifyTripChecklistUseCase = get(),
            getTripDetailUseCase = get(),
            tripId = parameters.get(),
            tipo = parameters.get(),
            vehiculoIdParam = parameters.get()
        )
    }

    viewModel { parameters ->
        PassengerViewModel(
            tripId = parameters.get(),
            getPassengersUseCase = get()
        )
    }

    viewModel { parameters ->
        ScanPassengerListViewModel(
            tripId = parameters[0],
            viajeTramoId = parameters[1],
            getPassengersUseCase = get()
        )
    }

    viewModel { parameters ->
        ChecklistItemDetailViewModel(
            getChecklistDocumentUseCase = get(),
            upsertChecklistDocumentUseCase = get(),
            uploadImageUseCase = get(),
            tripId = parameters.get(),
            checklistItemId = parameters.get(),
            vehiculoId = parameters.get(),
            tipo = parameters.get(),
            safeDocumentId = parameters.get()
        )
    }

    viewModel { parameters ->
        InspectionSheetViewModel(
            getInspectionSheetUseCase = get(),
            upsertInspectionSheetUseCase = get(),
            vehiculoId = parameters.get(),
            viajeId = parameters.get(),
            tipo = parameters.get(),
            safeDocumentId = parameters.get()
        )
    }

    viewModel { parameters ->
        DocumentInspectionViewModel(
            getUseCase = get(),
            upsertUseCase = get(),
            vehiculoId = parameters.get(),
            viajeId = parameters.get(),
            tipo = parameters.get(),
            safeDocumentId = parameters.get()
        )
    }

    viewModel { parameters ->
        LightsAlarmViewModel(
            getUseCase = get(),
            upsertUseCase = get(),
            vehiculoId = parameters.get(),
            viajeId = parameters.get(),
            tipo = parameters.get(),
            safeDocumentId = parameters.get()
        )
    }

    viewModel { parameters ->
        SeatBeltsViewModel(
            getUseCase = get(),
            upsertUseCase = get(),
            vehiculoId = parameters.get(),
            viajeId = parameters.get(),
            tipo = parameters.get(),
            safeDocumentId = parameters.get()
        )
    }

    viewModel { parameters ->
        ToolsInspectionViewModel(
            getUseCase = get(),
            upsertUseCase = get(),
            vehiculoId = parameters.get(),
            viajeId = parameters.get(),
            tipo = parameters.get(),
            safeDocumentId = parameters.get()
        )
    }

    viewModel { parameters ->
        FirstAidViewModel(
            getUseCase = get(),
            upsertUseCase = get(),
            vehiculoId = parameters.get(),
            viajeId = parameters.get(),
            tipo = parameters.get(),
            safeDocumentId = parameters.get()
        )
    }

    viewModel { parameters ->
        SpillKitViewModel(
            getUseCase = get(),
            upsertUseCase = get(),
            vehiculoId = parameters.get(),
            viajeId = parameters.get(),
            tipo = parameters.get(),
            safeDocumentId = parameters.get()
        )
    }

    viewModel { parameters ->
        TripServicesViewModel(
            tripId = parameters.get(),
            getSegmentsUseCase = get(),
            getNextStepUseCase = get(),
            getLocationUseCase = get(),
            updateSegmentUseCase = get(),
            deleteSegmentUseCase = get()
        )
    }

    viewModel { parameters ->
        RegisterDepartureViewModel(
            tripId = parameters.get(),
            getNextStepUseCase = get(),
            registerDepartureUseCase = get()
            //getLocationUseCase = get()
        )
    }

    viewModel { parameters ->
        RegisterArrivalViewModel(
            tripId = parameters.get(),
            getNextStepUseCase = get(),
            registerArrivalUseCase = get()
            //getLocationUseCase = get()
        )
    }

    viewModel { parameters ->
        RegisterCheckpointViewModel(
            tripId = parameters.get(),
            getNextStepUseCase = get(),
            registerCheckpointUseCase = get()
            //getLocationUseCase = get()
        )
    }

    viewModel { parameters ->
        RegisterStopViewModel(
            tripId = parameters.get(),
            getNextStepUseCase = get(),
            registerStopUseCase = get(),
            getLocationUseCase = get()
        )
    }

    viewModel { parameters ->
        RegisterRestViewModel(
            tripId = parameters.get(),
            getNextStepUseCase = get(),
            registerRestUseCase = get(),
            getLocationUseCase = get()
        )
    }

    viewModel { (tripId: Int, viajeTramoId: Int) -> 
        ScanPassengerViewModel(
            tripId = tripId, 
            viajeTramoId = viajeTramoId, 
            uploadImageUseCase = get(),
            scanDnisPhotosUseCase = get()
        ) 
    }

}
