package repositories
import interfaces.IPermissionRepository
import models.Permission

class PermissionRepository : IPermissionRepository{
    private val perms = mutableMapOf<Pair<Int, Int>, Permission>() 

    override fun findByUserAndResource(userId: Int, resourceId: Int): Permission? =
        perms[userId to resourceId]

    override fun findByResource(resourceId: Int): List<Permission> =
        perms.values.filter { it.resourceId == resourceId }

    override fun findByUser(userId: Int): List<Permission> =
        perms.values.filter { it.userId == userId }

    override fun grant(permission: Permission) {
        perms[permission.userId to permission.resourceId] = permission
    }

    override fun revoke(userId: Int, resourceId: Int) {
        perms.remove(userId to resourceId)
    }

    override fun update(permission: Permission) {
        if (perms.containsKey(permission.userId to permission.resourceId)) {
            perms[permission.userId to permission.resourceId] = permission
        }
    }
}