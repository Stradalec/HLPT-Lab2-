package models

data class Permission(
    val userLogin: String,
    val targetResource: String,
    val availableActions: String
)