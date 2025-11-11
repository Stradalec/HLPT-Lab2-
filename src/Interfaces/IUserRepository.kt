package interfaces

import models.UserData
interface IUserRepository {
    fun findByLogin(login: String): UserData?
    fun save(login: String, userData: UserData)
    fun exists(login: String): Boolean
    fun getAllLogins(): Set<String>
    fun remove(login: String): Boolean
}