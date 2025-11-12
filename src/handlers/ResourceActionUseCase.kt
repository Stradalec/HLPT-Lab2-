package handlers

import interfaces.*
import models.*
import enumerators.*

class ResourceActionUseCase(
    private val authService: IAuthService,
    private val permissionManager: IPermissionManager,
    private val userRepository: IUserRepository,
    private val resourceRepository: IResourceRepository
) {
    fun execute(cmd: Command): Int {
       val user = userRepository.findByLogin(cmd.login)
        ?: return ExitCode.ERROR_UNKNOWN_USER.code
        val authExitCodeValue = authService.authorization(user, cmd.password)

        if (authExitCodeValue != 0) return authExitCodeValue
        
        val target = resourceRepository.findByPath(cmd.resourcePath)
            ?: return ExitCode.ERROR_RESOURCE_NOT_FOUND.code

        if (cmd.volume > target.maxVolume) {
            return ExitCode.ERROR_EXCEED_MAX_VOLUME.code
        }

        

        if (!permissionManager.hasPermission(target.id, user.id, cmd.action)) {
            return ExitCode.ERROR_NO_PERMISSION.code
        }

        
        

        return ExitCode.SUCCESS.code
    }
}
