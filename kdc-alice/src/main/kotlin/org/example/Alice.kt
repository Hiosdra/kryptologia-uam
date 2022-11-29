package org.example

import org.example.Config.ALGORITHM
import org.example.Config.ALICE_IDENTITY
import org.example.model.Algorithm
import org.example.model.EncryptedMessageRequest
import retrofit2.create
import java.util.*
import kotlin.system.exitProcess

private val ALICE_KEY = when (ALGORITHM) {
    Algorithm.AES -> "AKoClfhi7fMdQkieciQLbA=="
    Algorithm.DESede -> "SmJKHMun03C6v4xAbqvIirXZHzukDRUH"
    Algorithm.Blowfish -> "aZNgXIXDvKrrsOhNZnYLd76BR5fqkLiScC2wK+cV4Jh6MxVFZs2ugzRXjOKV0w7amfVWHKKyAH0="
}

private val mdcClient: MDCClient = createRetrofit(Config.SERVER_URL).create()
private val bobClient: BobClient = createRetrofit(Config.BOB_URL).create()
private val keyService = KeyService()
private val scanner = Scanner(System.`in`)

/**
 * Alice
 */
suspend fun main() {
    val keys = mdcClient.getKeys(ALGORITHM.name, ALICE_IDENTITY)
    println("Response: $keys")
    val sessionKey = keyService.decryptKey(keys.encryptedByAliceKey, ALICE_KEY, ALGORITHM)
    println("Session key: $sessionKey")
    println("Type in messages:")
    while (true) {
        val input = scanner.nextLine()
        if (input != "close") {
            val encryptedMessage = keyService.encryptKey(input, sessionKey, ALGORITHM)
            val messageRequest =
                EncryptedMessageRequest(encryptedMessage, keys.encryptedByBobKey, keys.encryptedIdentity)
            bobClient.sendMessage(ALGORITHM.name, messageRequest)
        } else {
            println("Closing")
            exitProcess(0)
        }
    }
}
