package org.example

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.example.Config.ALGORITHM
import org.example.model.Algorithm
import org.example.model.EncryptedMessageRequest
import org.example.model.Message
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import javax.crypto.spec.IvParameterSpec

private val BOB_KEY = when (ALGORITHM) {
    Algorithm.AES -> "h1bQ81EmUf9fjeHOtnpJCA=="
    Algorithm.DESede -> "a14LJbaGv5cTATQ0OCwg1uo3SpuKmzTT"
    Algorithm.Blowfish -> "lIrJ/gSJN3d4b7qZE+VNeFojVGZoG2N3M6sAe6tadzG7p+XS4Qdz5fNUDUQ75XS+3u907EeBKWE="
}

@RestController
class MessageController(
    private val keyService: KeyService,
    private val fileService: FileService
) {

    @PostMapping("/message")
    suspend fun getKey(
        @RequestHeader("x-algorithm") algorithm: String,
        @RequestBody messageRequest: EncryptedMessageRequest
    ) {
        val mapper = jacksonObjectMapper()
        val iv = IvParameterSpec(messageRequest.iv.fromBase64())
        val identity = keyService.decryptKey(messageRequest.encryptedIdentity, BOB_KEY, ALGORITHM, iv)
        val sessionKey = keyService.decryptKey(messageRequest.encryptedSessionKey, BOB_KEY, ALGORITHM, iv)
        val message: Message =
            mapper.readValue(keyService.decryptKey(messageRequest.encryptedMessage, sessionKey, ALGORITHM, iv))
        if (message.isFile) {
            println(messageRequest)
            println("Identity: $identity")
            println("Session key: $sessionKey")
            println("Message: $message")
            fileService.saveFile(message)

        } else {
            println(messageRequest)
            println("Identity: $identity")
            println("Session key: $sessionKey")
            println("Message: ${message.message}")

        }
    }
}
