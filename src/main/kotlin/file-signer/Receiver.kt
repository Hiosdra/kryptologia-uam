package `file-signer`

import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.net.ServerSocket
import java.nio.file.Files
import java.nio.file.Path
import java.security.PublicKey
import java.security.Signature

private const val FILE_PATH = "/Users/oskar.drozda/Projects/kryptografia/src/main/resources/apple-received.png"

fun main() {
    val serverSocket = ServerSocket(3000)
    while (true) {
        val socket = serverSocket.accept()
        val inputStream = socket.getInputStream()
        val objectInputStream = ObjectInputStream(inputStream)

        val signatureBytes = objectInputStream.readObject() as ByteArray
        val publicKey = objectInputStream.readObject() as PublicKey
//        receiveAndSaveFile(objectInputStream)

        println("Public key: $publicKey")
        println("Signature: ${signatureBytes.contentToString()}")

        val verified = verify(publicKey, File(FILE_PATH), signatureBytes)
        println("Signature verified: $verified")
    }
}

private fun receiveAndSaveFile(objectInputStream: ObjectInputStream) {
    val fileBytes = objectInputStream.readBytes()
    Files.write(Path.of(FILE_PATH), fileBytes)
}

private fun verify(
    publicKey: PublicKey,
    file: File,
    signatureBytes: ByteArray
): Boolean {
    val signature = initSignature(publicKey)
    val fileInput = FileInputStream(file)
    val data = ByteArray(1024)
    var read = fileInput.read(data)
    while (read != -1) {
        signature.update(data, 0, read)
        read = fileInput.read(data)
    }

    val verified = signature.verify(signatureBytes)
    return verified
}

private fun initSignature(publicKey: PublicKey): Signature {
    val signature = Signature.getInstance("SHA256withDSA")
    signature.initVerify(publicKey)
    return signature
}
