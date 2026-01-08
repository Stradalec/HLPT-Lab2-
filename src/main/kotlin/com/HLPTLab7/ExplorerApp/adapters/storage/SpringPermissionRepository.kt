package com.HLPTLab7.ExplorerApp.adapters.storage

import com.HLPTLab7.ExplorerApp.models.Permission
import com.HLPTLab7.ExplorerApp.models.PermissionId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
@Repository
interface SpringPermissionRepository : JpaRepository<Permission, PermissionId> {
    @Query("SELECT p FROM Permission p WHERE p.id.userId = :userId AND p.id.resourceId = :resourceId")
    fun findByUserIdAndResourceId(
        @Param("userId") userId: Int,
        @Param("resourceId") resourceId: Int
    ): Permission?
    @Query("SELECT p FROM Permission p WHERE p.id.userId = :userId")
    fun findByUserId(@Param("userId") userId: Int): List<Permission>

    @Query("SELECT p FROM Permission p WHERE p.id.resourceId = :resourceId")
    fun findByResourceId(@Param("resourceId") resourceId: Int): List<Permission>
}