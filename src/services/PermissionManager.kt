package models
import interfaces.IPermissionManager
import enumerators.Action
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