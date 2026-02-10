package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.remote.api.ChecklistDocumentApi
import org.rol.transportation.data.remote.dto.check_document.ChecklistDocumentDto
import org.rol.transportation.data.remote.dto.check_document.PhotoDto
import org.rol.transportation.data.remote.dto.check_document.UpsertChecklistDocumentRequest
import org.rol.transportation.domain.model.ChecklistDocument
import org.rol.transportation.domain.model.DocumentContent
import org.rol.transportation.domain.model.Photo
import org.rol.transportation.domain.model.enums.ChecklistDocumentType
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.utils.Resource

class ChecklistDocumentRepositoryImpl(
    private val checklistDocumentApi: ChecklistDocumentApi
) : ChecklistDocumentRepository {

    override suspend fun getChecklistDocument(
        vehiculoId: Int,
        documentType: ChecklistDocumentType,
        documentId: Int?
    ): Flow<Resource<ChecklistDocument>> = flow {
        try {
            emit(Resource.Loading)
            val documentDto = checklistDocumentApi.getChecklistDocument(
                vehiculoId,
                documentType.endpoint,
                documentId
            )

            if (documentDto != null) {
                emit(Resource.Success(documentDto.toDomain()))
            } else {
                val emptyDocument = ChecklistDocument(
                    viajeId = null,
                    vehiculoId = vehiculoId,
                    version = null,
                    viajeTipo = null,
                    document = DocumentContent(
                        photo = Photo(url = "")
                    )
                )
                emit(Resource.Success(emptyDocument))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener el documento"))
        }
    }

    override suspend fun upsertChecklistDocument(
        vehiculoId: Int,
        viajeId: Int,
        documentType: ChecklistDocumentType,
        viajeTipo: TripType,
        photoUrl: String
    ): Flow<Resource<ChecklistDocument>> = flow {
        try {
            emit(Resource.Loading)
            val request = UpsertChecklistDocumentRequest(
                photo = PhotoDto(url = photoUrl)
            )
            val document = checklistDocumentApi.upsertChecklistDocument(
                vehiculoId,
                viajeId,
                documentType.endpoint,
                viajeTipo.value,
                request
            )
            emit(Resource.Success(document.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al guardar el documento"))
        }
    }

    private fun ChecklistDocumentDto.toDomain() = ChecklistDocument(
        viajeId = viajeId,
        vehiculoId = vehiculoId,
        version = version,
        viajeTipo = viajeTipo,
        document = DocumentContent(
            photo = Photo(url = document.photo.url)
        )
    )
}