package models

data class Permission(
    val userId: Int,
    val targetResourceid: Int,
    val availableActions: String
)