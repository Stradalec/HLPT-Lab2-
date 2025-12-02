package repositories
import interfaces.IPermissionRepository
import models.Permission
import database.*
import dao.PermissionDao
class PermissionRepository : IPermissionRepository{
    val permissionDao = PermissionDao()

    override fun findByUserAndResource(userId: Int, resourceId: Int): Permission? {
        return Database.connection().use { inputConnection ->
            permissionDao.findByUserAndResource(inputConnection, userId, resourceId)
        }
    }

    override fun findByResource(resourceId: Int): List<Permission> {
        return Database.connection().use { inputConnection ->
            permissionDao.findByResource(inputConnection, resourceId)
        }
    }

    override fun findByUser(userId: Int): List<Permission> {
        return Database.connection().use { inputConnection ->
            permissionDao.findByUser(inputConnection, userId)
        }
    }

    override fun grant(permission: Permission) {
        Database.connection().use { inputConnection ->
            permissionDao.grant(inputConnection, permission)
            inputConnection.commit()
        }
    }

    override fun revoke(userId: Int, resourceId: Int) {
        Database.connection().use { inputConnection ->
            permissionDao.revoke(inputConnection, userId, resourceId)
            inputConnection.commit()
        }
    }

    override fun update(permission: Permission) {
        grant(permission)
    }
}