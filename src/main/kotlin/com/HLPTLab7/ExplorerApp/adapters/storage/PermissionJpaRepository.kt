package com.HLPTLab7.ExplorerApp.adapters.storage

import com.HLPTLab7.ExplorerApp.interfaces.IPermissionRepository
import com.HLPTLab7.ExplorerApp.models.Permission
import org.springframework.stereotype.Repository
import com.HLPTLab7.ExplorerApp.models.PermissionId
@Repository
class PermissionJpaRepositoryAdapter(
    private val jpaRepository: SpringPermissionRepository
) : IPermissionRepository { 
    override fun findByUserAndResource(userId: Int, resourceId: Int): Permission? =
    jpaRepository.findByUserIdAndResourceId(userId, resourceId)

    override fun findByResource(resourceId: Int): List<Permission> =
        jpaRepository.findByResourceId(resourceId)

    override fun findByUser(userId: Int): List<Permission> =
        jpaRepository.findByUserId(userId)

    override fun grant(permission: Permission) {
        jpaRepository.save(permission)
    }

    override fun revoke(userId: Int, resourceId: Int) {
        val id = PermissionId(userId, resourceId)
        if (jpaRepository.existsById(id)) {
            jpaRepository.deleteById(id)
        }
    }

    override fun update(permission: Permission) {
        grant(permission)
    }
 }