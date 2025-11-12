package handlers

import interfaces.*
import models.*
import enumerators.*
import kotlinx.cli.*
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

class CommandHandler(
    private val authService: IAuthService, 
    private val userRepository: IUserRepository,
    private val resourceRepository: IResourceRepository,
    private val permissionRepository: IPermissionRepository
){
    private val permissionManager = PermissionManager(permissionRepository, resourceRepository, userRepository)
    private val parser = CommandParser()

    fun execute(arguments: Array<String>) : Int {
        return when (val parsed = parser.parse(arguments)) {
            is CommandParser.ParseResult.Error -> parsed.exitCode
            is CommandParser.ParseResult.Ok -> {
                val useCase = ResourceActionUseCase(
                    authService,
                    permissionManager,
                    userRepository,
                    resourceRepository
                )
                useCase.execute(parsed.command)
            }
        }
}
}