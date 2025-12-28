package com.HLPTLab7.ExplorerApp.models
data class User(val id: Int, val login: String, val salt: String, val hash: String)