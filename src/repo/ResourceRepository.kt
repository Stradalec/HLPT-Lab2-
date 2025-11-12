package repositories
import interfaces.IResourceRepository
import models.Resource
class ResourceRepository : IResourceRepository {
    private val resources = mutableMapOf<Int,Resource>()
    private val children = mutableMapOf<Int, MutableSet<Int>>()
    override fun findById(id: Int): Resource? = resources[id]

    override fun findByName(name: String): Resource? =
        resources.values.find { it.name == name }

    override fun findByParent(parentId: Int): List<Resource> =
    children[parentId]?.map { childId -> resources[childId] }?.filterNotNull() ?: emptyList()

    override fun save(resource: Resource) {
        resources[resource.id] = resource
        resource.parentId?.also { parentId ->
            children.getOrPut(parentId) { mutableSetOf() } += resource.id
        }
    }
    override fun delete(id: Int): Boolean {
        val resource = resources.remove(id) ?: return false

        resource.parentId?.let { parentId ->
            children[parentId]?.remove(id)
        }


        val toDelete = mutableListOf<Int>()
        collectAllDescendants(id, toDelete)
        toDelete.forEach { delete(it) }

        return true
    }
    private fun collectAllDescendants(parentId: Int, result: MutableList<Int>) {
        val directChildren = children[parentId] ?: return
        for (childId in directChildren) {
            result.add(childId)
            collectAllDescendants(childId, result)
        }
    }
    

    override fun findByPath(path: String): Resource? {
        
    if (path.isEmpty()) return null
    val parts = path.split(".")
    val root = resources.values.find { it.parentId == null } ?: return null

    var current: Resource? = if (parts.first() == root.name) {
        root
    } else {
        if (resources.values.any { it.name == parts.first() }) {
            root
        } else {
            return null
        }
    }

    for (part in parts) {
        if (current == null) return null
        val childrenAtLevel = findByParent(current.id) 
        val child = childrenAtLevel.find { it.name == part }
            ?: return null 
        current = child
    }
    return current
}

    fun getAll(): List<Resource> = resources.values.toList()

}