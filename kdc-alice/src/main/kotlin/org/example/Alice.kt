package org.example

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.example.Config.ALGORITHM
import org.example.Config.ALICE_IDENTITY
import org.example.model.Algorithm
import org.example.model.EncryptedMessageRequest
import org.example.model.KeyResponse
import org.example.model.Message
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
private val fileService = FileService()
private val scanner = Scanner(System.`in`)
private val mapper = jacksonObjectMapper()


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
        val msg: Message

        if (input.equals("close")) {
            println("Closing")
            exitProcess(0)
        } else if (input.contains("/")) {
            val fileName = input.replace("/", "")
            val message = fileService.uploadFile(fileName)

            msg = Message(message, true, fileName)
        } else {
            msg = Message(input, false, "")
        }

        sendMessage(keys, mapper.writeValueAsString(msg), sessionKey)
    }
}

suspend fun sendMessage(keys: KeyResponse, message: String?, sessionKey: String) {
    val encryptedMessage = keyService.encryptKey(message.toString(), sessionKey, ALGORITHM)
    val messageRequest =
        EncryptedMessageRequest(encryptedMessage, keys.encryptedByBobKey, keys.encryptedIdentity)
    bobClient.sendMessage(ALGORITHM.name, messageRequest)
}
