package org.rol.transportation.presentation.home_trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.rol.transportation.domain.model.Trip
import org.rol.transportation.domain.usecase.GetTripsPagedUseCase

class HomeViewModel(
    private val getTripsPagedUseCase: GetTripsPagedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val tripsPagingFlow: Flow<PagingData<Trip>> =
        getTripsPagedUseCase()
            .cachedIn(viewModelScope)
}