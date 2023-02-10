package x509

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.KeyUsage
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.math.BigInteger
import java.security.KeyPair
import java.security.SecureRandom
import java.security.Security
import java.security.cert.X509Certificate
import java.util.Date


fun generateCertificate(root: X509CertificateHolder, keyPair: KeyPair): X509Certificate {
    Security.addProvider(BouncyCastleProvider())

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


