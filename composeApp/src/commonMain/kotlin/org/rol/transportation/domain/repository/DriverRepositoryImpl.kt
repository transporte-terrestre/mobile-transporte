package org.rol.transportation.domain.repository

import org.rol.transportation.data.remote.api.DriverApi
import org.rol.transportation.domain.model.DriverDetail
import org.rol.transportation.domain.model.DriverDocument

class DriverRepositoryImpl(private val api: DriverApi) : DriverRepository {
    override suspend fun getDriverById(id: Int): DriverDetail {
        val response = api.getDriverById(id)
        
        val allDocuments = response.documentos.values.flatten().map { doc ->
            DriverDocument(
                id = doc.id,
                tipo = doc.tipo,
                nombre = doc.nombre,
                url = doc.url,
                fechaExpiracion = doc.fechaExpiracion,
                fechaEmision = doc.fechaEmision
            )
        }

        return DriverDetail(
            id = response.id,
            dni = response.dni,
            nombres = response.nombres,
            apellidos = response.apellidos,
            nombreCompleto = response.nombreCompleto,
            email = response.email,
            celular = response.celular,
            numeroLicencia = response.numeroLicencia,
            documentos = allDocuments
        )
    }
}
