package org.example

import org.example.Config.ALGORITHM
import org.example.model.Algorithm
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class KeyService {
    fun generateKey(algorithm: Algorithm): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(algorithm.name)
        val keySize = when (algorithm) {
            Algorithm.AES -> 128
            Algorithm.DESede -> 168
            Algorithm.Blowfish -> 448
        }
        keyGenerator.init(keySize)
        return keyGenerator.generateKey()
    }

    fun encryptKey(keyToEncrypt: String, encryptingKey: String, algorithm: Algorithm, iv: IvParameterSpec): String {
        val encryptingKey = SecretKeyUtil.fromString(encryptingKey, algorithm)
        return encrypt(algorithm, keyToEncrypt, encryptingKey, iv)
    }

    private fun encrypt(algorithm: Algorithm, input: String, key: SecretKey, iv: IvParameterSpec): String {
        val cipher = Cipher.getInstance(algorithm.cipher)
        cipher.init(Cipher.ENCRYPT_MODE, key, iv) // iv here?
        val cipherText = cipher.doFinal(input.toByteArray())
        return Base64.getEncoder()
            .encodeToString(cipherText)
    }

    fun decryptKey(encryptedKey: String, decryptingKey: String, algorithm: Algorithm, iv: IvParameterSpec): String {
        val decryptingKey = SecretKeyUtil.fromString(decryptingKey, algorithm)
        return decrypt(algorithm, encryptedKey, decryptingKey, iv)
    }

    private fun decrypt(algorithm: Algorithm, encryptedKey: String, key: SecretKey, iv: IvParameterSpec): String {
        val cipher = Cipher.getInstance(algorithm.cipher)
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        val plainText = cipher.doFinal(
            Base64.getDecoder().decode(encryptedKey.toByteArray())
        )
        return plainText.decodeToString()
    }

    fun generateIv(): IvParameterSpec {
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        return IvParameterSpec(iv)
    }
}

fun main() {
    println(KeyService().generateKey(ALGORITHM).encodeToString())
}
