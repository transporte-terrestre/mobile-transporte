package org.rol.transportation.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import org.rol.transportation.data.remote.dto.storage.StorageUploadResponseDto
import org.rol.transportation.utils.Constants

class StorageApi(private val client: HttpClient) {

    suspend fun uploadImage(imageBytes: ByteArray, fileName: String): StorageUploadResponseDto {
        return client.post("${Constants.STORAGE_ENDPOINT}?folder=imagenes") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("file", imageBytes, Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                        })
                    }
                )
            )
        }.body()
    }
}