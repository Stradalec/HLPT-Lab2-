package repositories

import interfaces.IUserRepository
import models.User

class UserRepository : IUserRepository {
    private val users = mutableMapOf<String, User>()

    override fun findByLogin(login: String): User? = users[login]

    override fun save( user: User) {
        users[user.login] = user
    }

    override fun exists(login: String): Boolean = users.containsKey(login)

    override fun getAllLogins(): Set<String> = users.keys.toSet()

    override fun remove(login: String): Boolean = users.remove(login) != null
}