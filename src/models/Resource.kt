package models
import interfaces.IResource
import enumerators.Action
class Resource (
    val name: String,
    val maxVolume: Int = 10,
    val parent: Resource? = null
) : IResource {
    private val children = mutableMapOf<String, Resource>()
    private val permissions = mutableMapOf<String, MutableSet<Action>>() // login -> actions

    override fun addChild(resource: Resource) {
        children[resource.name] = resource
    }

    override fun getChild(name: String): Resource? = children[name]

    override fun findByPath(path: String): Resource? {
        val parts = path.split(".")
        var current: Resource? = this
        for (part in parts) {
            current = current?.getChild(part) ?: return null
        }
        return current
    }

    override fun remove(): Boolean {
        for (child in children.values.toList()) {
            child.remove()
        }
        parent?.children?.remove(this.name)
        return true
    }

    override fun getAll(): List<Resource> {
        val all = mutableListOf<Resource>()
        for (child in children.values) {
            all.add(child)
            all.addAll(child.getAll())
        }
        return all
    }
}