package repositories

import interfaces.IUserRepository
import models.User
import database.*
import dao.UserDao
class UserRepository : IUserRepository {
    private val userDao = UserDao() 

    override fun findByLogin(login: String): User? {
        return Database.connection().use { inputConnection ->
            userDao.findByLogin(inputConnection, login)
        }
    }

    override fun findById(id: Int): User? {
        return Database.connection().use { inputConnection ->
            userDao.findById(inputConnection, id)
        }
    }

    override fun save(user: User) {
        Database.connection().use { inputConnection ->
            userDao.save(inputConnection, user)
            inputConnection.commit()
        }
    }

    override fun exists(login: String): Boolean {
        return findByLogin(login) != null
    }

    override fun getAllLogins(): Set<String>{
        return Database.connection().use { inputConnection ->
            userDao.getAllLogins(inputConnection)          
        }
    } 

    override fun remove(login: String): Boolean {
        return Database.connection().use { inputConnection ->
            val result = userDao.remove(inputConnection, login)
            inputConnection.commit()
            result
        }
    } 
}