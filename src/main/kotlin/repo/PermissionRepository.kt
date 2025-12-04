package com.HLPTLab7.ExplorerApp.repositories
import org.springframework.stereotype.Repository
import com.HLPTLab7.ExplorerApp.interfaces.IPermissionRepository
import com.HLPTLab7.ExplorerApp.models.Permission
import com.HLPTLab7.ExplorerApp.database.*
import com.HLPTLab7.ExplorerApp.dao.PermissionDao
import java.sql.Connection
import javax.sql.DataSource
@Repository
class PermissionRepository(private val permissionDao: PermissionDao, private val dataSource: DataSource) : IPermissionRepository {
    

    override fun findByUserAndResource(userId: Int, resourceId: Int): Permission? {
        return withConnection { inputConnection ->
            permissionDao.findByUserAndResource(inputConnection, userId, resourceId)
        }
    }

    override fun findByResource(resourceId: Int): List<Permission> {
        return withConnection { inputConnection ->
            permissionDao.findByResource(inputConnection, resourceId)
        }
    }

    override fun findByUser(userId: Int): List<Permission> {
        return withConnection { inputConnection ->
            permissionDao.findByUser(inputConnection, userId)
        }
    }

    override fun grant(permission: Permission) {
        withConnection { inputConnection ->
            permissionDao.grant(inputConnection, permission)
            inputConnection.commit()
        }
    }

    override fun revoke(userId: Int, resourceId: Int) {
        withConnection { inputConnection ->
            permissionDao.revoke(inputConnection, userId, resourceId)
            inputConnection.commit()
        }
    }

    override fun update(permission: Permission) {
        grant(permission)
    }
    private fun <T> withConnection(block: (Connection) -> T): T {
        return dataSource.connection.use { connection ->
            block(connection).also {
                connection.commit()
            }
        }
    }
}