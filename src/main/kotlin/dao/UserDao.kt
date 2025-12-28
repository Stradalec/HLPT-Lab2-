package com.HLPTLab7.ExplorerApp.dao
import com.HLPTLab7.ExplorerApp.models.User
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import org.springframework.stereotype.Repository
@Repository 
class UserDao {
    fun findByLogin(connection: Connection, login: String): User? {
        val sql = "SELECT id, login, salt, hash FROM users WHERE login = ?"
        return connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setString(1, login)
            preparedStatement.executeQuery().use { result ->
                if (result.next()) {
                    User(
                        id = result.getInt("id"),
                        login = result.getString("login"),
                        salt = result.getString("salt"),
                        hash = result.getString("hash")
                    )
                } else {
                    null
                }
                
            }
        }
        
    }

    fun findById(connection: Connection, id: Int): User? {
        val sql = "SELECT id, login, salt, hash FROM users WHERE id = ?"
        return connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setInt(1, id)
            preparedStatement.executeQuery().use { result ->
                if (result.next()) {
                    User(
                        id = result.getInt("id"),
                        login = result.getString("login"),
                        salt = result.getString("salt"),
                        hash = result.getString("hash")
                    )
                } else {
                    null
                }
            }
        }
    }

    fun save(connection: Connection, user: User) {
        val sql = "MERGE INTO users(id, login, salt, hash) KEY(id) VALUES (?, ?, ?, ?)"
        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setInt(1, user.id)
            preparedStatement.setString(2, user.login)
            preparedStatement.setString(3, user.salt)
            preparedStatement.setString(4, user.hash)
            preparedStatement.executeUpdate()
        }
    }

    fun getAllLogins(connection: Connection): Set<String>{
        val sql = "SELECT login FROM users"
        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.executeQuery().use { result ->
                val logins = mutableSetOf<String>()
                while(result.next()) {
                    logins += result.getString("login")
                }
                return logins
            }
            
        }
    }
    fun remove(connection: Connection, login: String): Boolean {
        val user = findByLogin(connection, login) ?: return false
        val sql = "DELETE FROM users WHERE login = ?"
        connection.prepareStatement(sql).use { preparedStatement ->          
            preparedStatement.setString(1, login)
            return preparedStatement.executeUpdate() > 0
        }
    }
}