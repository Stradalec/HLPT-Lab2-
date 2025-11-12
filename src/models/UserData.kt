package models
data class UserData(val login: String, val salt: String, val hash: String)