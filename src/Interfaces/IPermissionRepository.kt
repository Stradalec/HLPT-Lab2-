package interfaces
import models.Permission

interface IPermissionRepository {
    fun findByUserAndResource(userId: Int, resourceId: Int): Permission?
    fun findByResource(resourceId: Int): List<Permission>
    fun findByUser(userId: Int): List<Permission>
    fun grant(permission: Permission)
    fun revoke(userId: Int, resourceId: Int)
    fun update(permission: Permission)
}