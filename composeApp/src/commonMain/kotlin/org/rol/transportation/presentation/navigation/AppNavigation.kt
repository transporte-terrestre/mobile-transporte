package org.rol.transportation.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.koinInject
import org.rol.transportation.data.local.TokenManager
import org.rol.transportation.presentation.checklist_document_inspection.DocumentInspectionScreen
import org.rol.transportation.presentation.checklist_first_aid.FirstAidScreen
import org.rol.transportation.presentation.checklist_inspection_sheet.InspectionSheetScreen
import org.rol.transportation.presentation.checklist_item_detail.ChecklistItemDetailScreen
import org.rol.transportation.presentation.checklist_lights_alarm.LightsAlarmScreen
import org.rol.transportation.presentation.checklist_seat_belts.SeatBeltsScreen
import org.rol.transportation.presentation.checklist_spill_kit.SpillKitScreen
import org.rol.transportation.presentation.checklist_tools_inspection.ToolsInspectionScreen
import org.rol.transportation.presentation.driver_documents.DriverDocumentsScreen
import org.rol.transportation.presentation.driver_documents_pdfs.PdfViewerScreen
import org.rol.transportation.presentation.home.HomeMenuScreen
import org.rol.transportation.presentation.home_trip.HomeScreen
import org.rol.transportation.presentation.home_trip_detail.TripDetailScreen
import org.rol.transportation.presentation.home_trip_detail_checklist.ChecklistScreen
import org.rol.transportation.presentation.home_trip_detail_passenger.PassengerScreen
import org.rol.transportation.presentation.home_trip_detail_services.TripServicesScreen
import org.rol.transportation.presentation.login.LoginScreen
import org.rol.transportation.presentation.notifications.NotificationsScreen
import org.rol.transportation.presentation.profile.ProfileScreen
import kotlin.time.Clock


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val tokenManager: TokenManager = koinInject()

    val startDestination = if (tokenManager.isLoggedIn()) {
        Screen.HomeMenu
    } else {
        Screen.Login
    }


    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Login> {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.HomeMenu) {
                        popUpTo<Screen.Login> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Screen.HomeMenu> {
            HomeMenuScreen(
                onNavigate = { screen ->
                    navController.navigate(screen)
                }
            )
        }

        composable<Screen.ChecklistTrips> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.ChecklistTrips>()
            org.rol.transportation.presentation.checklist_trips.ChecklistTripsScreen(
                refreshTrigger = args.refreshKey,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToChecklist = { tripId, tipo, vehiculoId ->
                    navController.navigate(Screen.Checklist(tripId, tipo, vehiculoId, "ChecklistTrips"))
                }
            )
        }

        composable<Screen.Home> {
            HomeScreen(
                onNavigateToTripDetail = { tripId ->
                    navController.navigate(Screen.TripDetail(tripId))
                },
                onNavigateBack = {
                    navController.navigate(Screen.HomeMenu)
                }
            )
        }


        composable<Screen.TripDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.TripDetail>()
            TripDetailScreen(
                tripId = args.tripId,
                refreshTrigger = args.refreshKey,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToChecklist = { tripId, tipo, vehiculoId ->
                    navController.navigate(Screen.Checklist(tripId, tipo, vehiculoId))
                },
                onNavigateToPassengers = { tripId ->
                    navController.navigate(Screen.Passengers(tripId))
                },
                onNavigateToTripServices = { tripId ->
                    navController.navigate(Screen.TripServices(tripId))
                }
            )
        }

        composable<Screen.Passengers> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.Passengers>()
            PassengerScreen(
                tripId = args.tripId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<Screen.TripServices> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.TripServices>()
            TripServicesScreen(
                tripId = args.tripId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<Screen.Checklist> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.Checklist>()
            ChecklistScreen(
                tripId = args.tripId,
                tipo = args.tipo,
                vehiculoId = args.vehiculoId,
                onNavigateBack = {
                    navController.popBackStack()
                    val refreshKey = Clock.System.now().toEpochMilliseconds()
                    if (args.source == "ChecklistTrips") {
                        navController.navigate(Screen.ChecklistTrips(refreshKey = refreshKey)) {
                            popUpTo<Screen.ChecklistTrips> { inclusive = true }
                        }
                    } else {
                        navController.navigate(
                            Screen.TripDetail(
                                tripId = args.tripId,
                                refreshKey = refreshKey
                            )
                        ) {
                            popUpTo<Screen.TripDetail> { inclusive = true }
                        }
                    }
                },
                //onNavigateBack = { navController.navigateUp() },
                onNavigateToItemDetail = { tripId, checklistItemId, vehiculoId, itemName, tipo, documentId ->
                    navController.navigate(
                        Screen.ChecklistItemDetail(
                            tripId = tripId,
                            checklistItemId = checklistItemId,
                            vehiculoId = vehiculoId,
                            itemName = itemName,
                            tipo = tipo,
                            vehiculoChecklistDocumentId = documentId
                        )
                    )
                },
                onNavigateToInspectionSheet = { tripId, vehiculoId, tipo, documentId ->
                    navController.navigate(
                        Screen.InspectionSheet(
                            tripId = tripId,
                            vehiculoId = vehiculoId,
                            tipo = tipo,
                            vehiculoChecklistDocumentId = documentId
                        )
                    )
                },
                onNavigateToDocumentInspection = { tripId, vehiculoId, tipo, documentId ->
                    navController.navigate(
                        Screen.DocumentInspection(
                            tripId = tripId,
                            vehiculoId = vehiculoId,
                            tipo = tipo,
                            vehiculoChecklistDocumentId = documentId
                        )
                    )
                },
                onNavigateToLightsAlarm = { tripId, vehiculoId, tipo, documentId ->
                    navController.navigate(
                        Screen.LightsAlarm(
                            tripId = tripId,
                            vehiculoId = vehiculoId,
                            tipo = tipo,
                            vehiculoChecklistDocumentId = documentId
                        )
                    )
                },
                onNavigateToSeatBelts = { tripId, vehiculoId, tipo, documentId ->
                    navController.navigate(
                        Screen.SeatBelts(
                            tripId = tripId,
                            vehiculoId = vehiculoId,
                            tipo = tipo,
                            vehiculoChecklistDocumentId = documentId
                        )
                    )
                },
                onNavigateToToolsInspection = { tripId, vehiculoId, tipo, documentId ->
                    navController.navigate(
                        Screen.ToolsInspection(
                            tripId = tripId,
                            vehiculoId = vehiculoId,
                            tipo = tipo,
                            vehiculoChecklistDocumentId = documentId
                        )
                    )
                },
                onNavigateToFirstAid = { tripId, vehiculoId, tipo, documentId ->
                    navController.navigate(
                        Screen.FirstAid(
                            tripId = tripId,
                            vehiculoId = vehiculoId,
                            tipo = tipo,
                            vehiculoChecklistDocumentId = documentId
                        )
                    )
                },
                onNavigateToSpillKit = { tripId, vehiculoId, tipo, documentId ->
                    navController.navigate(
                        Screen.SpillKit(
                            tripId = tripId,
                            vehiculoId = vehiculoId,
                            tipo = tipo,
                            vehiculoChecklistDocumentId = documentId
                        )
                    )
                }
            )
        }

        composable<Screen.ChecklistItemDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.ChecklistItemDetail>()
            ChecklistItemDetailScreen(
                tripId = args.tripId,
                checklistItemId = args.checklistItemId,
                vehiculoId = args.vehiculoId,
                itemName = args.itemName,
                tipo = args.tipo,
                vehiculoChecklistDocumentId = args.vehiculoChecklistDocumentId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<Screen.InspectionSheet> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.InspectionSheet>()
            InspectionSheetScreen(
                vehiculoId = args.vehiculoId,
                tripId = args.tripId,
                tipo = args.tipo,
                vehiculoChecklistDocumentId = args.vehiculoChecklistDocumentId,
                onNavigateBack = { navController.navigateUp() }
            )
        }


        composable<Screen.DocumentInspection> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.DocumentInspection>()
            DocumentInspectionScreen(
                vehiculoId = args.vehiculoId,
                tripId = args.tripId,
                tipo = args.tipo,
                vehiculoChecklistDocumentId = args.vehiculoChecklistDocumentId,
                onNavigateBack = { navController.navigateUp() }
            )
        }


        composable<Screen.LightsAlarm> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.LightsAlarm>()
            LightsAlarmScreen(
                vehiculoId = args.vehiculoId,
                tripId = args.tripId,
                tipo = args.tipo,
                vehiculoChecklistDocumentId = args.vehiculoChecklistDocumentId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<Screen.SeatBelts> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.SeatBelts>()
            SeatBeltsScreen(
                vehiculoId = args.vehiculoId,
                tripId = args.tripId,
                tipo = args.tipo,
                vehiculoChecklistDocumentId = args.vehiculoChecklistDocumentId,
                onNavigateBack = { navController.navigateUp() }
            )
        }


        composable<Screen.ToolsInspection> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.ToolsInspection>()
            ToolsInspectionScreen(
                vehiculoId = args.vehiculoId,
                tripId = args.tripId,
                tipo = args.tipo,
                vehiculoChecklistDocumentId = args.vehiculoChecklistDocumentId,
                onNavigateBack = { navController.navigateUp() }
            )
        }


        composable<Screen.FirstAid> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.FirstAid>()
            FirstAidScreen(
                vehiculoId = args.vehiculoId,
                tripId = args.tripId,
                tipo = args.tipo,
                vehiculoChecklistDocumentId = args.vehiculoChecklistDocumentId,
                onNavigateBack = { navController.navigateUp() }
            )
        }


        composable<Screen.SpillKit> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.SpillKit>()
            SpillKitScreen(
                vehiculoId = args.vehiculoId,
                tripId = args.tripId,
                tipo = args.tipo,
                vehiculoChecklistDocumentId = args.vehiculoChecklistDocumentId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<Screen.Profile> {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Screen.Login) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<Screen.DriverDocuments> {
            DriverDocumentsScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToPdf = { url, title ->
                    navController.navigate(Screen.PdfViewer(url = url, title = title))
                }
            )
        }

        composable<Screen.Notifications> {
            NotificationsScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<Screen.PdfViewer> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.PdfViewer>()
            PdfViewerScreen(
                url = args.url,
                title = args.title,
                onNavigateBack = { navController.navigateUp() }
            )
        }

    }
}
