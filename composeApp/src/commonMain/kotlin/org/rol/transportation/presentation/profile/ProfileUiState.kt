package org.rol.transportation.presentation.profile

data class ProfileUiState(
    val userName: String? = null,
    val userEmail: String? = null,
    val token: String? = null,
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false
)