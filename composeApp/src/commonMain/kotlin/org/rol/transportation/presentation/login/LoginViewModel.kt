package org.rol.transportation.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.usecase.LoginUseCase
import org.rol.transportation.domain.usecase.ValidateEmailUseCase
import org.rol.transportation.utils.Resource


class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(
            email = email,
            emailError = null,
            errorMessage = null
        )}
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(
            password = password,
            passwordError = null,
            errorMessage = null
        )}
    }

    fun onRememberSessionChanged(remember: Boolean) {
        _uiState.update { it.copy(rememberSession = remember) }
    }

    fun onLoginClick() {
        val currentState = _uiState.value

        if (!validateEmailUseCase(currentState.email)) {
            _uiState.update { it.copy(emailError = "Email inválido") }
            return
        }

        if (currentState.password.length < 6) {
            _uiState.update { it.copy(passwordError = "La contraseña debe tener al menos 6 caracteres") }
            return
        }

        viewModelScope.launch {
            loginUseCase(
                email = currentState.email,
                password = currentState.password,
                rememberSession = currentState.rememberSession
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(
                            isLoading = true,
                            errorMessage = null
                        )}
                    }
                    is Resource.Success -> {
                        _uiState.update { it.copy(
                            isLoading = false,
                            isLoginSuccessful = true,
                            errorMessage = null
                        )}
                    }
                    is Resource.Error -> {
                        val errorMsg = when {
                            result.message.contains("405") || result.message.contains("Method Not Allowed") ->
                                "Error del servidor: Endpoint incorrecto."
                            result.message.contains("401") || result.message.contains("Unauthorized") ->
                                "Email o contraseña incorrectos"
                            result.message.contains("404") ->
                                "Servicio no encontrado."
                            result.message.contains("timeout") ->
                                "Tiempo de espera agotado."
                            else ->
                                "Error: ${result.message}"
                        }

                        _uiState.update { it.copy(
                            isLoading = false,
                            errorMessage = errorMsg
                        )}
                    }
                }
            }
        }
    }

    fun resetLoginSuccess() {
        _uiState.update { it.copy(isLoginSuccessful = false) }
    }
}