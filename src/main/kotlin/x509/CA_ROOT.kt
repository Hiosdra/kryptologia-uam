package x509

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.OperatorCreationException
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security
import java.util.Date

fun main() {
    val commonFunctions = CommonFunctions()
    val rootCertificate = createRootCertificate(X500Name("CN=Root Certificate"), 365)
    val certificate = JcaX509CertificateConverter().getCertificate(rootCertificate)
    commonFunctions.saveIntoFile(certificate, "src/main/resources/cert/rootCert.pem")
    println(certificate)
}

fun createRootCertificate(subjectDN: X500Name, validityInDays: Int): X509CertificateHolder {
    Security.addProvider(BouncyCastleProvider())

    val random = SecureRandom()
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC")
    keyPairGenerator.initialize(2048, random)
    val keyPair = keyPairGenerator.generateKeyPair()
    val subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.public.encoded)

    val notBefore = Date()
    val notAfter = Date(notBefore.time + (validityInDays * 24L * 60L * 60L * 1000L))

    val serialNumber = BigInteger(64, random)

    val certificateBuilder =
        X509v3CertificateBuilder(subjectDN, serialNumber, notBefore, notAfter, subjectDN, subjectPublicKeyInfo)

    val contentSigner: ContentSigner
    try {
        contentSigner = JcaContentSignerBuilder("SHA256WithRSAEncryption").build(keyPair.private)
    } catch (e: OperatorCreationException) {
        throw IllegalStateException("Failed to create content signer: " + e.message, e)
    }

    val certificateHolder = certificateBuilder.build(contentSigner)
    return certificateHolder
}
