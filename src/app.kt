import interfaces.*
import models.*
import enumerators.*
import handlers.*
import kotlinx.cli.*
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess




// salo
class App {
    fun run(args: Array<String>) {
        if (args.isEmpty() || args.any { it == "--help" || it == "-h" }) {
            exitProcess(ExitCode.HELP.code)
        }

        val (users, root) = createMockData()
        val authService = AuthService()
        val permissionManager = PermissionManager()
        val handler = CommandHandler(authService, permissionManager, users, root)

        try {
            handler.execute(args)
        } catch (e: Exception) {
            exitProcess(ExitCode.HELP.code)
        }
    }
}

fun main(args: Array<String>) {
    App().run(args)
}