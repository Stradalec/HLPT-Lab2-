package com.HLPTLab7.ExplorerApp.repositories

import com.HLPTLab7.ExplorerApp.interfaces.IUserRepository
import com.HLPTLab7.ExplorerApp.models.User
import org.springframework.stereotype.Repository

@Repository
class UserJpaRepositoryAdapter(
    private val jpaRepository: SpringUserRepository
) : IUserRepository {

    override fun findByLogin(login: String): User? =
        jpaRepository.findByLogin(login)

    override fun findById(id: Int): User? =
        jpaRepository.findById(id).orElse(null)

    override fun save(user: User) {
        jpaRepository.save(user)
    }

    override fun exists(login: String): Boolean =
        jpaRepository.existsByLogin(login)

    override fun getAllLogins(): Set<String> =
        jpaRepository.findAllLogins().toSet()

    override fun remove(login: String): Boolean =
        jpaRepository.deleteByLogin(login) > 0
}