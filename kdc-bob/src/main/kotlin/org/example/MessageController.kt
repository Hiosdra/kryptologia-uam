package org.example

import org.example.Config.ALGORITHM
import org.example.model.Algorithm
import org.example.model.EncryptedMessageRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

private val BOB_KEY = when (ALGORITHM) {
    Algorithm.AES -> "h1bQ81EmUf9fjeHOtnpJCA=="
    Algorithm.DESede -> "a14LJbaGv5cTATQ0OCwg1uo3SpuKmzTT"
    Algorithm.Blowfish -> "lIrJ/gSJN3d4b7qZE+VNeFojVGZoG2N3M6sAe6tadzG7p+XS4Qdz5fNUDUQ75XS+3u907EeBKWE="
}

@RestController
class MessageController(
    private val keyService: KeyService
) {

    @PostMapping("/message")
    suspend fun getKey(
        @RequestHeader("x-algorithm") algorithm: String,
        @RequestBody messageRequest: EncryptedMessageRequest
    ) {
        println(messageRequest)
        val identity = keyService.decryptKey(messageRequest.encryptedIdentity, BOB_KEY, ALGORITHM)
        println("Identity: $identity")
        val sessionKey = keyService.decryptKey(messageRequest.encryptedSessionKey, BOB_KEY, ALGORITHM)
        println("Session key: $sessionKey")
        val message = keyService.decryptKey(messageRequest.encryptedMessage, sessionKey, ALGORITHM)
        println("Message: $message")
    }
}
