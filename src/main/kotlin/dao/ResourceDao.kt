package com.HLPTLab7.ExplorerApp.dao
import com.HLPTLab7.ExplorerApp.models.Resource
import com.HLPTLab7.ExplorerApp.database.*
import java.sql.PreparedStatement
import java.sql.ResultSet
import org.springframework.stereotype.Repository
import java.sql.Connection
@Repository
class ResourceDao {
    fun findById(connection: Connection, id: Int): Resource? {
        val sql = "SELECT id, name, maxVolume, parentId FROM resources WHERE id = ?"
        return connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setInt(1, id)
            preparedStatement.executeQuery().use { result ->
                if (result.next()) {
                    Resource(
                        id = result.getInt("id"),
                        name = result.getString("name"),
                        maxVolume = result.getInt("maxVolume"),
                        parentId = if (result.wasNull()) null else result.getInt("parentId")
                    )
                } else {
                    null
                }
            }
        }
    }

    fun findByName(connection: Connection, name: String): Resource? {
        val sql = "SELECT id, name, maxVolume, parentId FROM resources WHERE name = ?"
        return connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setString(1, name)
            preparedStatement.executeQuery().use { result ->
                if (result.next()) {
                    Resource(
                        id = result.getInt("id"),
                        name = result.getString("name"),
                        maxVolume = result.getInt("maxVolume"),
                        parentId = if (result.wasNull()) null else result.getInt("parentId")
                    )
                } else {
                    null
                }
            }
        } 
    }

    fun findByParent(connection: Connection, parentId: Int): List<Resource> {
        val sql = "SELECT id, name, maxVolume, parentId FROM resources WHERE parentId = ?"
        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setInt(1, parentId)
            preparedStatement.executeQuery().use { result ->
                val mainResult = mutableListOf<Resource>()
                while (result.next()) {
                    mainResult.add(
                        Resource(
                            id = result.getInt("id"),
                            name = result.getString("name"),
                            maxVolume = result.getInt("maxVolume"),
                            parentId = if (result.wasNull()) null else result.getInt("parentId")
                        )
                    )
                }
                return mainResult
            }
        }
    }

    fun save(connection: Connection, resource: Resource) {
        val sql = "MERGE INTO resources(id, name, maxVolume, parentId) KEY(id) VALUES (?, ?, ?, ?)"
        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setInt(1, resource.id)
            preparedStatement.setString(2, resource.name)
            preparedStatement.setInt(3, resource.maxVolume)
            preparedStatement.setObject(4, resource.parentId)
            preparedStatement.executeUpdate()
        }
    }
    fun delete(connection: Connection, id: Int): Boolean {
        val sql = "DELETE FROM resources WHERE id = ?"
        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setInt(1, id)
            val rowsAffected = preparedStatement.executeUpdate()
            return rowsAffected > 0
        }
    }
    fun findByPath(connection: Connection, path: String): Resource? {
        if (path.isEmpty()) return null
        val parts = path.split(".")

        val root = findRoot(connection) ?: return null

        var current: Resource? = if (parts.first() == root.name) {
            root
        } else {
            if (resourcesExistWithName(connection, parts.first())) {
                root
            } else {
                return null
            }
        }

        for (part in parts) {
            val child = findByParent(connection, current!!.id).find { it.name == part }
                ?: return null
            current = child
        }

        return current
    }


    private fun findRoot(connection: Connection): Resource? {
        val sql = "SELECT id, name, maxVolume, parentId FROM resources WHERE parentId IS NULL"
        return connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.executeQuery().use { result ->
                if (result.next()) {
                    Resource(
                        id = result.getInt("id"),
                        name = result.getString("name"),
                        maxVolume = result.getInt("maxVolume"),
                        parentId = if (result.wasNull()) null else result.getInt("parentId")
                    )
                } else {
                    null
                }
            }
        }
    }


    private fun resourcesExistWithName(connection: Connection, name: String): Boolean {
        val sql = "SELECT 1 FROM resources WHERE name = ? LIMIT 1"
        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setString(1, name)
            preparedStatement.executeQuery().use { result ->
                return result.next()
            }
        }
    }
}