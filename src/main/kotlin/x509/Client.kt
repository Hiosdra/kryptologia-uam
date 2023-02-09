package x509

import java.io.ObjectInputStream
import java.net.Socket
import java.security.cert.X509Certificate

private const val IP = "localhost"

fun main() {
    val commonFunctions = CommonFunctions()

    val socket = Socket(IP, 3000)
    val inputStream = socket.getInputStream()
    val objectInputStream = ObjectInputStream(inputStream)

    val clientCert = objectInputStream.readObject() as X509Certificate
    val clientCertSha = objectInputStream.readObject() as String

    commonFunctions.saveIntoFile(clientCert, "src/main/resources/cert/client-cert-received.pem")
    println("Got certificate sha256: $clientCertSha")
    println("Calculated certificate sha256: ${commonFunctions.sha256(commonFunctions.convertToBase64PEMString(clientCert))}")
}
