package repositories

import interfaces.IUserRepository
import models.UserData

class UserRepository : IUserRepository {
    private val users = mutableMapOf<String, UserData>()

    override fun findByLogin(login: String): UserData? = users[login]

    override fun save( userData: UserData) {
        users[userData.login] = userData
    }

    override fun exists(login: String): Boolean = users.containsKey(login)

    override fun getAllLogins(): Set<String> = users.keys.toSet()

    override fun remove(login: String): Boolean = users.remove(login) != null
}