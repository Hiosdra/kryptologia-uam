package org.example.model

data class KeyResponse(
    val encryptedByAliceKey: String,
    val encryptedByBobKey: String,
    val encryptedIdentity: String,
    val iv: String
)
