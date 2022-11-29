package org.example

import org.example.model.EncryptedMessageRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface BobClient {
    @POST("/message")
    suspend fun sendMessage(
        @Header("x-algorithm") algorithm: String,
        @Body message: EncryptedMessageRequest
    )
}
