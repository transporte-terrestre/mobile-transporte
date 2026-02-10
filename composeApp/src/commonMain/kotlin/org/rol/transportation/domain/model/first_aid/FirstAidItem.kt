package org.rol.transportation.domain.model.first_aid

data class FirstAidItem(
    val label: String,
    val habilitado: Boolean,
    val fechaVencimiento: String,
    val fechaSalida: String,
    val fechaReposicion: String
)