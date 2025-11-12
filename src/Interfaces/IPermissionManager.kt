package interfaces
import enumerators.Action

interface IPermissionManager {
    fun grantPermission(resourceName: String, userLogin: String, action: Action)
    fun hasPermission(resourceId: Int, userId: Int, action: Action): Boolean
}