package handlers

import enumerators.*

data class Command(
    val login: String,
    val password: String,
    val action: Action,
    val resourcePath: String,
    val volume: Int
)
