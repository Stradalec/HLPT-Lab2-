package interfaces
import models.Resource
interface IResource {
    fun addChild(resource: Resource)
    fun getChild(name: String): Resource?
    fun findByPath(path: String): Resource?
    fun remove(): Boolean 
    fun getAll(): List<Resource>
}