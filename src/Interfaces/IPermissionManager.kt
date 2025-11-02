package interfaces
import models.Resource
import enumerators.Action

interface IPermissionManager {
    fun grantPermission(resourceName: String, user: String, action: Action)
    fun hasPermission(resource: Resource?, user: String, action: Action): Boolean
}