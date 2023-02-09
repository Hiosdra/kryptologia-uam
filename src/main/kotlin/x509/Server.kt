package x509

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.net.Socket


fun main() {
    val commonFunctions = CommonFunctions()
    val keyPair = generateKeys()
    val rootCertHolder = createRootCertificate(X500Name("CN=Root Certificate"), 365, keyPair)
    val rootCert = JcaX509CertificateConverter().getCertificate(rootCertHolder)
    commonFunctions.saveIntoFile(rootCert, "src/main/resources/cert/rootCert.pem")
    println("Root certificate created.")

    val serverSocket = ServerSocket(3000)

    var socket: Socket? = null
    while (serverSocket.accept().also { socket = it } != null) {
        val socket = socket!!
        val outputStream = socket.getOutputStream()
        val objectOutputStream = ObjectOutputStream(outputStream)

        val clientCert = generateCertificate(rootCertHolder, keyPair)
        commonFunctions.saveIntoFile(clientCert, "src/main/resources/cert/userCert.pem")
        objectOutputStream.writeObject(clientCert)
        val clientCertSha = commonFunctions.sha256(commonFunctions.convertToBase64PEMString(clientCert))
        println("Client cert sha256: $clientCertSha")
        objectOutputStream.writeObject(clientCertSha)
        objectOutputStream.flush()
        socket.close()
    }


}
