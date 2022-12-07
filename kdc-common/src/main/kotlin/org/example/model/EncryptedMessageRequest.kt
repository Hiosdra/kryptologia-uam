package org.example.model

data class EncryptedMessageRequest(
    val encryptedMessage: String,
    val encryptedSessionKey: String,
    val encryptedIdentity: String,
    val iv: String
)
