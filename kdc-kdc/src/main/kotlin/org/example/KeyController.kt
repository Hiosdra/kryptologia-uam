package org.example

import org.example.model.Algorithm
import org.example.model.KeyResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
class KeyController(
    private val keyService: KeyService
) {

    @GetMapping("/algorithm/{algorithm}/session-keys")
    suspend fun getKey(@PathVariable algorithm: String, @RequestHeader("x-identity") identity: String): KeyResponse {
        val algorithm = getAlgorithm(algorithm)
        val keys: Keys = when (algorithm) {
            Algorithm.AES -> AESKeys
            Algorithm.DESede -> DESKeys
            Algorithm.Blowfish -> BlowfishKeys
        }
        val sessionKey = keyService.generateKey(algorithm).encodeToString()
        val iv = keyService.generateIv()
        println("Iv size: " + iv.iv.size)
        println("Session key: $sessionKey")
        val encryptedByAliceKey = keyService.encryptKey(sessionKey, keys.AliceKey, algorithm, iv)
        val encryptedByBobKey = keyService.encryptKey(sessionKey, keys.BobKey, algorithm, iv)
        val encryptedIdentity = keyService.encryptKey(identity, keys.BobKey, algorithm, iv)
        return KeyResponse(encryptedByAliceKey, encryptedByBobKey, encryptedIdentity, iv.iv.asBase64())
    }

    private fun getAlgorithm(algorithm: String) = try {
        Algorithm.valueOf(algorithm)
    } catch (e: IllegalArgumentException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Algorithm not supported: $algorithm")
    }
}
