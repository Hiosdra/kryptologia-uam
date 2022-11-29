package org.example

import org.example.model.Algorithm

object Config {
    const val SERVER_URL = "http://127.0.0.1:8080"
    const val BOB_URL = "http://127.0.0.1:7777"
    val ALGORITHM = Algorithm.Blowfish

    const val ALICE_IDENTITY = "Alice"
}
