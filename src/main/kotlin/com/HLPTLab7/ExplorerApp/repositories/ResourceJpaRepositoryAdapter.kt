package com.HLPTLab7.ExplorerApp.repositories

import com.HLPTLab7.ExplorerApp.interfaces.IResourceRepository
import com.HLPTLab7.ExplorerApp.models.Resource
import org.springframework.stereotype.Repository
@Repository
class ResourceJpaRepositoryAdapter(
    private val jpaRepository: SpringResourceRepository
) : IResourceRepository {
     override fun findById(id: Int): Resource? =
        jpaRepository.findById(id).orElse(null)

    override fun findByName(name: String): Resource? =
        jpaRepository.findByName(name)

    override fun findByParent(parentId: Int): List<Resource> =
        jpaRepository.findByParentId(parentId)

    override fun save(resource: Resource) {
        jpaRepository.save(resource)
    }

    override fun delete(id: Int): Boolean {
        if (jpaRepository.existsById(id)) {
            jpaRepository.deleteById(id)
            return true
        }
        return false
    }
override fun findByPath(path: String): Resource? {
    val parts = path.trim('.').split('.')
    if (path.isEmpty()) return null

    val root = jpaRepository.findAll().find { it.parentId == null } ?: return null

    var current: Resource? = null

    for (part in parts) {
        current = when {
            current == null -> {
                jpaRepository.findByParentId(root.id).find { it.name == part }
            }
            else -> {
                jpaRepository.findByParentId(current.id).find { it.name == part }
            }
        } ?: return null
    }

    return current
}
}