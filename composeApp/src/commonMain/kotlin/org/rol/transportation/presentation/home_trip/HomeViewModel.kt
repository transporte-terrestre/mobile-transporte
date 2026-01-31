package org.rol.transportation.presentation.home_trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.usecase.GetTripsUseCase
import org.rol.transportation.utils.Resource

class HomeViewModel(
    private val getTripsUseCase: GetTripsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTrips()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.length >= 3 || query.isEmpty()) {
            loadTrips(search = query.ifEmpty { null })
        }
    }

    fun onRefresh() {
        loadTrips()
    }

    private fun loadTrips(page: Int = 1, search: String? = null) {
        viewModelScope.launch {
            getTripsUseCase(page = page, limit = 10, search = search)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _uiState.update { it.copy(isLoading = true, error = null) }
                        }
                        is Resource.Success -> {
                            val (viajes, total) = result.data
                            val totalPages = (total + 9) / 10 // ceil(total / 10)

                            _uiState.update { it.copy(
                                viajes = viajes,
                                isLoading = false,
                                error = null,
                                page = page,
                                hasMorePages = page < totalPages
                            )}
                        }
                        is Resource.Error -> {
                            _uiState.update { it.copy(
                                isLoading = false,
                                error = result.message
                            )}
                        }
                    }
                }
        }
    }
}