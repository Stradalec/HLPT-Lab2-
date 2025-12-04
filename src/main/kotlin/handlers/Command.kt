package com.HLPTLab7.ExplorerApp.handlers

import com.HLPTLab7.ExplorerApp.enumerators.*

data class Command(
    val login: String,
    val password: String,
    val action: Action,
    val resourcePath: String,
    val volume: Int
)
