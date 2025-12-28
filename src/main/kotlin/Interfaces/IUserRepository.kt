package com.HLPTLab7.ExplorerApp.interfaces

import com.HLPTLab7.ExplorerApp.models.User
interface IUserRepository {
    fun findByLogin(login: String): User?
    fun findById(id: Int): User?
    fun save(user: User)
    fun exists(login: String): Boolean
    fun getAllLogins(): Set<String>
    fun remove(login: String): Boolean
}