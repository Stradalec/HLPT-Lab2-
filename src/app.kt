import kotlinx.cli.*
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

enum class ExitCode(val code: Int) {
    SUCCESS(0),
    HELP(1),
    ERROR_WRONG_PASSWORD(2),
    ERROR_UNKNOWN_USER(3),
    ERROR_INVALID_ACTION(4),
    ERROR_NO_PERMISSION(5),
    ERROR_RESOURCE_NOT_FOUND(6),
    ERROR_INVALID_VOLUME_FORMAT(7),
    ERROR_EXCEED_MAX_VOLUME(8)
}

enum class Action { READ, WRITE, EXECUTE }
// salo
data class UserData(val salt: String, val hash: String)



fun createMockData(): Pair<Map<String, UserData>, Resource> {
    val users = mapOf(
        "alice" to UserData(salt = "saltAlice", hash = "0ded4a676ee2fcd61ab5772e67ac33ef2ada6a929470cac9cb703cc9e6315c85"),
        "stradalets" to UserData(salt = "absoluteSuffering", hash = "No hash?")
    ) // солевая алиса
    val root = Resource("root", 100)
    val folderA = Resource("A", 50, root)
    val folderB = Resource("B", 20, folderA)
    val fileC = Resource("C", 10, folderB)
    val fileD = Resource("D", 10, root)

    root.addChild(folderA)
    root.addChild(fileD)
    folderA.addChild(folderB)
    folderB.addChild(fileC)
    return users to root
}


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
//    if (args.isEmpty() || args.any { it == "--help" || it == "-h" }) {
//        exitProcess(ExitCode.HELP.code)
//    }
//    val commandHandler = CommandHandler()
//    try {
//        commandHandler.workWithArguments(args)
//    } catch (e: Exception) {
//        exitProcess(ExitCode.HELP.code)
//    }
    App().run(args)
}