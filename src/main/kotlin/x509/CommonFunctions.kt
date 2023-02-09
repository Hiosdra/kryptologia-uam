package x509

import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.PEMWriter
import java.io.File
import java.io.PrintWriter
import java.io.StringReader
import java.io.StringWriter
import java.security.cert.X509Certificate

class CommonFunctions {
    private fun convertToBase64PEMString(x509Cert: X509Certificate): String {
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
}
