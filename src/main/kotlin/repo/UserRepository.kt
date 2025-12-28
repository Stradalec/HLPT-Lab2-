package com.HLPTLab7.ExplorerApp.repositories

import com.HLPTLab7.ExplorerApp.interfaces.IUserRepository
import com.HLPTLab7.ExplorerApp.models.User
import com.HLPTLab7.ExplorerApp.database.*
import com.HLPTLab7.ExplorerApp.dao.UserDao
import org.springframework.stereotype.Repository
import javax.sql.DataSource
import java.sql.Connection
@Repository
class UserRepository(private val userDao: UserDao, private val dataSource: DataSource) : IUserRepository { 

    override fun findByLogin(login: String): User? {
        return withConnection { inputConnection ->
            userDao.findByLogin(inputConnection, login)
        }
    }

    override fun findById(id: Int): User? {
        return withConnection { inputConnection ->
            userDao.findById(inputConnection, id)
        }
    }

    override fun save(user: User) {
        withConnection { inputConnection ->
            userDao.save(inputConnection, user)
            inputConnection.commit()
        }
    }

    override fun exists(login: String): Boolean {
        return findByLogin(login) != null
    }

    override fun getAllLogins(): Set<String>{
        return withConnection { inputConnection ->
            userDao.getAllLogins(inputConnection)          
        }
    } 

    override fun remove(login: String): Boolean {
        return withConnection { inputConnection ->
            val result = userDao.remove(inputConnection, login)
            inputConnection.commit()
            result
        }
    }
    private fun <T> withConnection(block: (Connection) -> T): T {
        return dataSource.connection.use { connection ->
            block(connection).also {
                connection.commit()
            }
        }
    }
}