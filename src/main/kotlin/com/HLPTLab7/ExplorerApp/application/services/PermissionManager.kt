package com.HLPTLab7.ExplorerApp.models
import com.HLPTLab7.ExplorerApp.interfaces.IPermissionRepository
import com.HLPTLab7.ExplorerApp.interfaces.IResourceRepository
import com.HLPTLab7.ExplorerApp.interfaces.IUserRepository
import com.HLPTLab7.ExplorerApp.interfaces.IPermissionManager
import com.HLPTLab7.ExplorerApp.enumerators.Action
import org.springframework.stereotype.Service
@Service
class PermissionManager (private val permissionRepo: IPermissionRepository, private val resourceRepo: IResourceRepository, private val userRepo: IUserRepository) : IPermissionManager  {

    override fun grantPermission(resourceName: String, userLogin: String, action: Action) {
        val resource = resourceRepo.findByName(resourceName) ?: return
        val user = userRepo.findByLogin(userLogin) ?: return
        val userPerms = permissionRepo.findByUserAndResource(user!!.id, resource.id)
        val actions = updateActions(userPerms?.availableActions ?: "---", action)
        permissionRepo.grant(Permission(id = PermissionId(userId = user.id, resourceId = resource.id), actions))
    }

    override fun hasPermission(resourceId: Int, userId: Int, action: Action): Boolean {
    val resource = resourceRepo.findById(resourceId) ?: return false
    val foundPermission = permissionRepo.findByUserAndResource(userId, resourceId)
    if (foundPermission != null && hasAction(foundPermission.availableActions, action)) {
        return true
    }

    return resource.parentId?.let { parentId ->
        hasPermission(parentId, userId, action)
    } ?: false
}

    private fun hasAction(available: String, action: Action): Boolean = when (action) {
        Action.READ -> available.getOrNull(0) == 'R'
        Action.WRITE -> available.getOrNull(1) == 'W'
        Action.EXECUTE -> available.getOrNull(2) == 'E'
    }

    private fun updateActions(current: String, action: Action): String {
        val chars = current.toMutableList()
        while (chars.size < 3) chars.add('-')
        chars[action.ordinal] = when (action) {
            Action.READ -> 'R'
            Action.WRITE -> 'W'
            Action.EXECUTE -> 'E'
        }
        return chars.joinToString("")
    }
}