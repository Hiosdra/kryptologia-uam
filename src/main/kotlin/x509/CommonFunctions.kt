package x509

import org.bouncycastle.openssl.PEMWriter
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.security.cert.X509Certificate

class CommonFunctions {
     fun convertToBase64PEMString(x509Cert: X509Certificate): String {
        val sw = StringWriter()
        PEMWriter(sw).use { pw -> pw.writeObject(x509Cert) }
        return sw.toString()
    }

    public fun saveIntoFile(x509Cert: X509Certificate, path: String){
        val certificate = convertToBase64PEMString(x509Cert)
        PrintWriter(File(path)).use {
            it.println(certificate)
        }
        println(certificate)
    }

}