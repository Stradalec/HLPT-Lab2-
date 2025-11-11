package handlers

import interfaces.*
import models.*
import enumerators.*

class ResourceActionUseCase(
    private val authService: IAuthService,
    private val permissionManager: IPermissionManager,
    private val users: Map<String, UserData>,
    private val root: Resource
) {
    fun execute(cmd: Command): Int {
        val user = users[cmd.login]
        val authExitCodeValue = authService.authorization(user, cmd.password)

        if (authExitCodeValue != 0) return authExitCodeValue

        val target = root.findByPath(cmd.resourcePath)
            ?: return ExitCode.ERROR_RESOURCE_NOT_FOUND.code

        permissionManager.grantPermission("A", "alice", Action.READ)
        permissionManager.grantPermission("B", "alice", Action.WRITE)
        permissionManager.grantPermission("C", "alice", Action.EXECUTE)

        if (!permissionManager.hasPermission(target, cmd.login, cmd.action)) {
            return ExitCode.ERROR_NO_PERMISSION.code
        }

        if (cmd.volume > 10) {
            return ExitCode.ERROR_EXCEED_MAX_VOLUME.code
        }

        return ExitCode.SUCCESS.code
    }
}
