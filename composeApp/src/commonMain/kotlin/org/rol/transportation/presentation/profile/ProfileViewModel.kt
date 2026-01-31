package org.rol.transportation.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.data.local.TokenManager
import org.rol.transportation.domain.usecase.LogoutUseCase

class ProfileViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val userName = tokenManager.getUserName()
        val userEmail = tokenManager.getUserEmail()
        val token = tokenManager.getToken()

        _uiState.update { it.copy(
            userName = userName,
            userEmail = userEmail,
            token = token
        )}
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            logoutUseCase()

            _uiState.update { it.copy(
                isLoading = false,
                isLoggedOut = true
            )}
        }
    }

    fun resetLogoutState() {
        _uiState.update { it.copy(isLoggedOut = false) }
    }
}