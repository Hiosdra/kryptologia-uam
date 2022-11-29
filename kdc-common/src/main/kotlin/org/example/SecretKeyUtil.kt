package org.example

import org.example.model.Algorithm
import java.util.*
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


