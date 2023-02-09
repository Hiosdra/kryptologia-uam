//package x509
//
//import org.bouncycastle.asn1.x500.X500Name
//import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
//import org.bouncycastle.cert.X509v3CertificateBuilder
//import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
//import org.bouncycastle.jce.provider.BouncyCastleProvider
//import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
//import java.math.BigInteger
//import java.security.KeyPair
//import java.security.KeyPairGenerator
//import java.security.Security
//import java.time.Instant
//import java.util.*
//
//fun main() {
//    // Add Bouncy Castle security provider
//    Security.addProvider(BouncyCastleProvider())
//
//    // Generate key pair
//    val keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC")
//    keyPairGenerator.initialize(2048)
//    val keyPair: KeyPair = keyPairGenerator.generateKeyPair()
//
//    // Create certificate
//    val serialNumber = BigInteger.valueOf(System.currentTimeMillis())
//    val issuer = X500Name("CN=MyCA")
//    val subject = X500Name("CN=MyCA")
//    val notBefore = Date()
//    val notAfter = Date(Instant.now().toEpochMilli() + (1000L * 60 * 60 * 24 * 365))
//    val subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.public.encoded)
//
//    val certBuilder = X509v3CertificateBuilder(issuer, serialNumber, notBefore, notAfter, subject, subjectPublicKeyInfo)
//
//    val contentSigner = JcaContentSignerBuilder("SHA256WithRSAEncryption").build(keyPair.private)
//    val cert = JcaX509CertificateConverter().getCertificate(certBuilder.build(contentSigner))
//
//    // Print certificate
//    println(cert)
//}
