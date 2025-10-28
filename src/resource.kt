import kotlinx.cli.*
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

interface IResource {
    fun addChild(resource: Resource)
    fun getChild(name: String): Resource?
    fun findByPath(path: String): Resource?
    fun remove(): Boolean // вопрос к корню
    fun getAll(): List<Resource>
}

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

interface IPermissionManager {
    fun grantPermission(resourceName: String, user: String, action: Action)
    fun hasPermission(resource: Resource?, user: String, action: Action): Boolean
}

class PermissionManager : IPermissionManager {
    val permissions = mutableMapOf<String, MutableMap<String, MutableSet<Action>>>()

    override fun grantPermission(resourceName: String, user: String, action: Action) {
        val userPerms = permissions.computeIfAbsent(resourceName) { mutableMapOf() }
        val actions = userPerms.computeIfAbsent(user) { mutableSetOf() }
        actions.add(action)
    }

    override fun hasPermission(resource: Resource?, user: String, action: Action): Boolean {
        if (resource == null) return false
        val userActions = permissions[resource.name]?.get(user)
        return if (userActions != null && action in userActions) {
            true
        } else {
            hasPermission(resource.parent, user, action)
        }
    }
}