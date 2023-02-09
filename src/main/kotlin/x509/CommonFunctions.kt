package x509

import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.PEMWriter
import java.io.File
import java.io.PrintWriter
import java.io.StringReader
import java.io.StringWriter
import java.security.MessageDigest
import java.security.cert.X509Certificate


class CommonFunctions {
    fun convertToBase64PEMString(x509Cert: X509Certificate): String {
        val sw = StringWriter()
        PEMWriter(sw).use { pw -> pw.writeObject(x509Cert) }
        return sw.toString()
    }

    fun saveIntoFile(x509Cert: X509Certificate, path: String) {
        val certificate = convertToBase64PEMString(x509Cert)
        PrintWriter(File(path)).use {
            it.println(certificate)
        }
        println(certificate)
    }

    fun readFile(path: String): X509CertificateHolder {
        val file = File(path)
        val reader = StringReader(file.readText())
        val pr = PEMParser(reader)
        return pr.readObject() as X509CertificateHolder
    }

    fun sha256(base: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(base.toByteArray(charset("UTF-8")))
        val hexString = StringBuilder()
        for (i in hash.indices) {
            val hex = Integer.toHexString(0xff and hash[i].toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString()
    }
}
