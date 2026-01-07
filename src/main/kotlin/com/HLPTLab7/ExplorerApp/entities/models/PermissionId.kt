package com.HLPTLab7.ExplorerApp.models
import jakarta.persistence.*
import java.io.Serializable
import java.util.Objects

@Embeddable
class PermissionId(
    @Column(name = "user_id")
    var userId: Int = 0,

    @Column(name = "resource_id")
    var resourceId: Int = 0
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PermissionId) return false
        return userId == other.userId && resourceId == other.resourceId
    }

    override fun hashCode() = Objects.hash(userId, resourceId)
}