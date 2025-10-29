package interfaces
import models.Resource
import models.Action

interface IPermissionManager {
    fun grantPermission(resourceName: String, user: String, action: Action)
    fun hasPermission(resource: Resource?, user: String, action: Action): Boolean
}