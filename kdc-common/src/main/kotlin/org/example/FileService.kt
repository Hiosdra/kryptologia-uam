package org.example

import org.example.model.Message
import java.io.File
import java.util.*

class FileService {

    private val BOB_PATH = "kdc-bob/src/main/resources/receive"
    private val ALICE_PATH = "kdc-alice/src/main/resource"


    fun uploadFile(fileName: String): String {
        val file = File(ALICE_PATH, fileName).inputStream().readAllBytes()
        return Base64.getEncoder().encodeToString(file)
    }

    fun saveFile(message: Message) {
        val decodedString = Base64.getDecoder().decode(message.message)
        val path = File(BOB_PATH, message.fileName)
        println("Received file, was saved in: ${path.path}")
        path.outputStream().write(decodedString)
    }
}