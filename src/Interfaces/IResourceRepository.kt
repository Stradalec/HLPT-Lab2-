package interfaces
import models.Resource

interface IResourceRepository {
    fun findById(id: Int): Resource?
    fun findByName(name: String): Resource?
    fun findByParent(parentId: Int): List<Resource>
    fun save(resource: Resource)
    fun delete(id: Int): Boolean
    fun findByPath(path: String): Resource?
}