package org.example

import org.example.model.Algorithm
import java.util.Base64
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

fun SecretKey.encodeToString(): String {
    return Base64.getEncoder().encodeToString(this.encoded)
}

object SecretKeyUtil {
    fun fromString(encodedKey: String, algorithm: Algorithm): SecretKey {
        val decodedKey = Base64.getDecoder().decode(encodedKey)
        return SecretKeySpec(decodedKey, 0, decodedKey.size, algorithm.name)
    }
}

fun ByteArray.asBase64(): String {
    return Base64.getEncoder().encodeToString(this)
}

fun String.fromBase64(): ByteArray {
    return Base64.getDecoder().decode(this)
}
