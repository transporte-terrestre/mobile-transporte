package org.rol.transportation.presentation.profile

data class ProfileUiState(
    val userName: String? = null,
    val userEmail: String? = null,
    val userPhoto: String? = null,
    val token: String? = null,
    val dniDriver: String? = null,
    val licenseDriver: String? = null,
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false
)