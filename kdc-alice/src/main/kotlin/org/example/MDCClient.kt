package org.example

import org.example.model.KeyResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MDCClient {
    @GET("/algorithm/{algorithm}/session-keys")
    suspend fun getKeys(@Path("algorithm") algorithm: String, @Header("x-identity") identity: String): KeyResponse
}
