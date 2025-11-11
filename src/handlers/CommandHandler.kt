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
    private val permissionManager: IPermissionManager,
    private val users: Map<String, UserData>,
    private val root: Resource
){
    private val parser = CommandParser()
    private val useCase = ResourceActionUseCase(authService, permissionManager, users, root)

    fun execute(arguments: Array<String>) : Int {
        return when (val parsed = parser.parse(arguments)) {
            is CommandParser.ParseResult.Error -> parsed.exitCode
            is CommandParser.ParseResult.Ok -> useCase.execute(parsed.command)
        }
}
}