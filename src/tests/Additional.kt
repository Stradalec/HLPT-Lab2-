import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import handlers.CommandHandler
import interfaces.*
import models.*
import enumerators.*
import repositories.*

class InMemoryPermissionRepository : IPermissionRepository {
    private val permissions = mutableListOf<Permission>()

    override fun findByUserAndResource(userId: Int, resourceId: Int): Permission? {
        return permissions.find { it.userId == userId && it.resourceId == resourceId }
    }

    override fun findByResource(resourceId: Int): List<Permission> {
        return permissions.filter { it.resourceId == resourceId }
    }

    override fun findByUser(userId: Int): List<Permission> {
        return permissions.filter { it.userId == userId }
    }

    override fun grant(permission: Permission) {
        permissions.add(permission)
    }

    override fun revoke(userId: Int, resourceId: Int) {
        permissions.removeAll { it.userId == userId && it.resourceId == resourceId }
    }

    override fun update(permission: Permission) {
        revoke(permission.userId, permission.resourceId)
        grant(permission)
    }
}

class InMemoryResourceRepository : IResourceRepository {
    private val resources = mutableMapOf<Int, Resource>()

    override fun findById(id: Int): Resource? = resources[id]
    override fun findByName(name: String): Resource? = resources.values.find { it.name == name }
    override fun findByParent(parentId: Int): List<Resource> = resources.values.filter { it.parentId == parentId }
    override fun save(resource: Resource) { resources[resource.id] = resource }
    override fun delete(id: Int): Boolean = resources.remove(id) != null
    override fun findByPath(path: String): Resource? = null
}

class InMemoryUserRepository : IUserRepository {
    private val users = mutableMapOf<String, User>()

    override fun findByLogin(login: String): User? = users[login]
    override fun findById(id: Int): User? = users.values.find { it.id == id }
    override fun save(user: User) { users[user.login] = user }
    override fun exists(login: String): Boolean = users.containsKey(login)
    override fun getAllLogins(): Set<String> = users.keys
    override fun remove(login: String): Boolean = users.remove(login) != null
}