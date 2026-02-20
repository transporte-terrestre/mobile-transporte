package org.rol.transportation.presentation.checklist_trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.TripLight
import org.rol.transportation.domain.usecase.GetLightTripsPagedUseCase

class ChecklistTripsViewModel(
    private val getLightTripsPagedUseCase: GetLightTripsPagedUseCase
) : ViewModel() {

    val tripsPagingFlow: Flow<PagingData<TripLight>> = getLightTripsPagedUseCase()
        .cachedIn(viewModelScope)
}
