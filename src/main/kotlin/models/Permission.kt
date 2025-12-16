package com.HLPTLab7.ExplorerApp.models
import jakarta.persistence.*

@Entity
@Table(name = "permissions")
class Permission(
    @EmbeddedId
    val id: PermissionId = PermissionId(),

    @Column(name = "available_actions", nullable = false)
    val availableActions: String
)