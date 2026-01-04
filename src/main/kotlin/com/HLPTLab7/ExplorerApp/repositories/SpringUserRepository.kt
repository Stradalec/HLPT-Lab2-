package com.HLPTLab7.ExplorerApp.repositories

import com.HLPTLab7.ExplorerApp.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SpringUserRepository : JpaRepository<User, Int> {

    fun findByLogin(login: String): User?

    @Query("SELECT u.login FROM User u")
    fun findAllLogins(): List<String>

    fun existsByLogin(login: String): Boolean

    fun deleteByLogin(login: String): Int
}