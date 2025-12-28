package com.HLPTLab7.ExplorerApp.models

data class Permission(
    val userId: Int,
    val resourceId: Int,
    val availableActions: String
)