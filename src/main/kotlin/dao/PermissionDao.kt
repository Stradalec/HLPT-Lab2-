package com.HLPTLab7.ExplorerApp.dao
import com.HLPTLab7.ExplorerApp.models.Permission
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import org.springframework.stereotype.Repository
@Repository
class PermissionDao {
    fun findByUserAndResource(connection: Connection, userId: Int, resourceId: Int): Permission? {
        val sql = "SELECT userId, resourceId, availableActions FROM permissions WHERE userId = ? AND resourceId = ?"
        return connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setInt(1, userId)
            preparedStatement.setInt(2, resourceId)
            preparedStatement.executeQuery().use { result ->
                if (result.next()) {
                    Permission(
                        userId = result.getInt("userId"),
                        resourceId = result.getInt("resourceId"),
                        availableActions = result.getString("availableActions")
                    )
                } else {
                    null
                }
            }
        }
    }
    fun findByResource(connection: Connection, resourceId: Int): List<Permission> {
        val sql = "SELECT userId, resourceId, availableActions FROM permissions WHERE resourceId = ?"
        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setInt(1, resourceId)
            preparedStatement.executeQuery().use { result ->
                val list = mutableListOf<Permission>()
                while (result.next()) {
                    list.add(
                        Permission(
                            userId = result.getInt("userId"),
                            resourceId = result.getInt("resourceId"),
                            availableActions = result.getString("availableActions")
                        )
                    )
                }
                return list
            }
        }
    }
    fun findByUser(connection: Connection, userId: Int): List<Permission> {
        val sql = "SELECT userId, resourceId, availableActions FROM permissions WHERE userId = ?"
        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setInt(1, userId)
            preparedStatement.executeQuery().use { result ->
                val list = mutableListOf<Permission>()
                while (result.next()) {
                    list.add(
                        Permission(
                            userId = result.getInt("userId"),
                            resourceId = result.getInt("resourceId"),
                            availableActions = result.getString("availableActions")
                        )
                    )
                }
                return list
            }
        }
    }

    fun grant(connection: Connection, permission: Permission) {
        val sql = "MERGE INTO permissions(userId, resourceId, availableActions) KEY(userId, resourceId) VALUES (?, ?, ?)"
        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setInt(1, permission.userId)
            preparedStatement.setInt(2, permission.resourceId)
            preparedStatement.setString(3, permission.availableActions)
            preparedStatement.executeUpdate()
        }
    }

    fun revoke(connection: Connection, userId: Int, resourceId: Int) {
        val sql = "DELETE FROM permissions WHERE userId = ? AND resourceId = ?"
        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setInt(1, userId)
            preparedStatement.setInt(2, resourceId)
            preparedStatement.executeUpdate()
        }
    }
}