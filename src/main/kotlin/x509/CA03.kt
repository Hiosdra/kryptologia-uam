package x509

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.KeyUsage
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security
import java.security.cert.X509Certificate
import java.util.*

fun main() {
    val commonFunctions = CommonFunctions();
    val rootCertificate = createRootCertificate(X500Name("CN=Root Certificate"), 365)
    val certificate = JcaX509CertificateConverter().getCertificate(rootCertificate)
    val childCertificate = generateCertificate(rootCertificate)
    commonFunctions.saveIntoFile(childCertificate, "src/main/resources/cert/userCert.pem")
}

fun generateCertificate(root: X509CertificateHolder): X509Certificate {
    // Add the Bouncy Castle provider
    Security.addProvider(BouncyCastleProvider())

    // Generate a 2048-bit RSA key pair
    val keyGen = KeyPairGenerator.getInstance("RSA", "BC")
    keyGen.initialize(2048, SecureRandom())
    val keyPair = keyGen.generateKeyPair()

    // Generate the certificate
    val serialNumber = BigInteger(64, SecureRandom())
    val notBefore = Date()
    val notAfter = Date(notBefore.time + (365L * 24 * 60 * 60 * 1000)) // 1 year validity
    val subject = X500Name("CN=Kotlin certificate,DC=org,DC=kotlin-lang")
    val builder = JcaX509v3CertificateBuilder(root.subject, serialNumber, notBefore, notAfter, subject, keyPair.public)
    builder.addExtension(org.bouncycastle.asn1.x509.Extension.keyUsage, true, KeyUsage(KeyUsage.digitalSignature))

    val signer = JcaContentSignerBuilder("SHA256WithRSAEncryption").build(keyPair.private)
    val certificate = JcaX509CertificateConverter().getCertificate(builder.build(signer))

    return certificate
}


