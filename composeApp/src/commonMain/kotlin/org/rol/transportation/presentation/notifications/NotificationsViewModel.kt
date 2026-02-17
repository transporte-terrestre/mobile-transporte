package org.rol.transportation.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.usecase.GetNotificationsUseCase

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _uiState.update { it.copy(page = 1, notifications = emptyList()) }
            }
            
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val result = getNotificationsUseCase(
                    page = _uiState.value.page,
                    limit = 10
                )
                
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        notifications = if (isRefresh) result.notifications else state.notifications + result.notifications,
                        hasNextPage = result.meta.hasNextPage,
                        page = state.page + 1
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al cargar notificaciones") }
            }
        }
    }
}
