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
    val parts = path.trim('/').split('/')
    if (parts.isEmpty()) return null

    var current = jpaRepository.findByName(parts[0])?.takeIf { it.parentId == null }
    if (current == null) return null

    for (index in 1 until parts.size) {
        val nextName = parts[index]
        val parentId = current?.parentId ?: return null
        current = jpaRepository.findByParentId(parentId)
            .find { it.name == nextName }
            ?: return null
    }
    return current
}
}