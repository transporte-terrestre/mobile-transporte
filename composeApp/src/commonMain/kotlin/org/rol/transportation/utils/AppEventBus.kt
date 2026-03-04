package org.rol.transportation.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AppEventBus {
    private val _reloadTripServices = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val reloadTripServices = _reloadTripServices.asSharedFlow()

    fun triggerReload() {
        _reloadTripServices.tryEmit(Unit)
    }
}
