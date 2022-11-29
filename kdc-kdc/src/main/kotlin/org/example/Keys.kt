package org.example

sealed interface Keys {
    val AliceKey: String
    val BobKey: String
}

internal object AESKeys : Keys {
    override val AliceKey = "AKoClfhi7fMdQkieciQLbA=="
    override val BobKey = "h1bQ81EmUf9fjeHOtnpJCA=="
}

internal object DESKeys : Keys {
    override val AliceKey = "SmJKHMun03C6v4xAbqvIirXZHzukDRUH"
    override val BobKey = "a14LJbaGv5cTATQ0OCwg1uo3SpuKmzTT"
}

internal object BlowfishKeys : Keys {
    override val AliceKey = "aZNgXIXDvKrrsOhNZnYLd76BR5fqkLiScC2wK+cV4Jh6MxVFZs2ugzRXjOKV0w7amfVWHKKyAH0="
    override val BobKey = "lIrJ/gSJN3d4b7qZE+VNeFojVGZoG2N3M6sAe6tadzG7p+XS4Qdz5fNUDUQ75XS+3u907EeBKWE="
}
