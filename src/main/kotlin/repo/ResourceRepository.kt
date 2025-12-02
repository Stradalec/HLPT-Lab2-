package repositories
import interfaces.IResourceRepository
import models.Resource
import database.*
import dao.ResourceDao
class ResourceRepository : IResourceRepository {
    private val resourceDao = ResourceDao()
    override fun findById(id: Int): Resource? {
        return Database.connection().use { inputConnection ->
            resourceDao.findById(inputConnection, id)
        }
    }

    override fun findByName(name: String): Resource? {
        return Database.connection().use { inputConnection ->
             resourceDao.findByName(inputConnection, name)
        }
    }

    override fun findByParent(parentId: Int): List<Resource> {
        return Database.connection().use { inputConnection ->
            resourceDao.findByParent(inputConnection, parentId)
        }
    }

    override fun save(resource: Resource) {
        Database.connection().use { inputConnection ->
            resourceDao.save(inputConnection, resource)
            inputConnection.commit()
        }
    }
   override fun delete(id: Int): Boolean {
        return Database.connection().use { inputConnection ->
            val result = resourceDao.delete(inputConnection, id)
            inputConnection.commit()
            result
        }
    }

    override fun findByPath(path: String): Resource? {
        return Database.connection().use { inputConnection ->
            resourceDao.findByPath(inputConnection, path)
        }
    }

}