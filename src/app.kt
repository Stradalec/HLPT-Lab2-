import interfaces.*
import models.*
import enumerators.*
import handlers.*
import kotlinx.cli.*
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess
import repositories.*

// salo
class App {
    fun run(args: Array<String>) {
        
        if (args.isEmpty() || args.any { it == "--help" || it == "-h" }) {
            exitProcess(ExitCode.HELP.code)
        }
        val userRepository = UserRepository()           
        val resourceRepository = ResourceRepository()
        val permissionRepository = PermissionRepository()
        val handler = CommandHandler(
            AuthService(),
            userRepository,
            resourceRepository,
            permissionRepository
        )
        
        try {
            var result = handler.execute(args)
            exitProcess(result)
        } catch (e: Exception) {
            exitProcess(ExitCode.HELP.code)
        }
    }
}

fun main(args: Array<String>) {
    App().run(args)
}