package interfaces

import models.User
interface IUserRepository {
    fun findByLogin(login: String): User?
    fun save(user: User)
    fun exists(login: String): Boolean
    fun getAllLogins(): Set<String>
    fun remove(login: String): Boolean
}