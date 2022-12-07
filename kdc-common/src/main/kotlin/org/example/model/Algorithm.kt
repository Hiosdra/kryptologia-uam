package org.example.model

enum class Algorithm(cipher: String) {
    AES("AES/CBC/PKCS5Padding"),
    DESede("DESede/CBC/PKCS5Padding"),
    Blowfish("Blowfish/CBC/PKCS5Padding")
}
