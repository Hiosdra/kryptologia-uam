package `file-signer`

import java.io.File
import java.io.FileInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

private const val FILE_PATH = "/Users/oskar.drozda/Projects/kryptografia/src/main/resources/apple.png"
private const val IP = "localhost"

fun main() {
    val file = File(FILE_PATH)

    val keyPair = generateKeys()
    println("Public key: ${keyPair.public}")
    println("Private key: ${keyPair.private}")
    val signature = createSignature(keyPair.private)
    val signatureBytes = sign(file, signature)
    println("Signature: ${signatureBytes.contentToString()}")
    send(file, signatureBytes, keyPair.public)
    println("File sent!")
}

private fun generateKeys(): KeyPair {
    val keyGen = KeyPairGenerator.getInstance("DSA")
    keyGen.initialize(2048)
    val keyPair = keyGen.genKeyPair()
    return keyPair
}

private fun createSignature(privateKey: PrivateKey): Signature {
    val signature = Signature.getInstance("SHA256withDSA")
    signature.initSign(privateKey)
    return signature
}

private fun sign(file: File, signature: Signature): ByteArray {
    val fileInput = FileInputStream(file)
    val data = ByteArray(1024)
    var read = fileInput.read(data)
    while (read != -1) {
        signature.update(data, 0, read)
        read = fileInput.read(data)
    }
    val signatureBytes = signature.sign()
    return signatureBytes
}

private fun send(file: File, signatureBytes: ByteArray, publicKey: PublicKey) {
    val socket = Socket(IP, 3000)
    val outputStream = socket.getOutputStream()
    val objectOutputStream = ObjectOutputStream(outputStream)

    objectOutputStream.writeObject(signatureBytes)
    objectOutputStream.writeObject(publicKey)
    objectOutputStream.write(file.readBytes())
    objectOutputStream.flush()
    socket.close()
}
