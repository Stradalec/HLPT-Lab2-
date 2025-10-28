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
    val parser = ArgParser("app")

    val login by parser.option(ArgType.String, fullName = "login", description = "User login").required()
    val password by parser.option(ArgType.String, fullName = "password", description = "User password" ).required()
    val action by parser.option(ArgType.String, fullName = "action", description = "Type of action wtih file").required()
    val resource by parser.option(ArgType.String, fullName = "resource", description = "Path to resource").required()
    val volume by parser.option(ArgType.String, fullName = "volume", description = "Volume of file").required()

    fun execute(arguments: Array<String>){
        parser.parse(arguments)
//        val (users, root) = createMockData()
//        val authService = AuthService()
        val user = users[login]
        authService.authorization(user, password)

        if(volume.toIntOrNull() == null){
            exitProcess(ExitCode.ERROR_INVALID_VOLUME_FORMAT.code)
        }
        val target = root.findByPath(resource)
        val act = when (action.lowercase()) {
            "read" -> Action.READ
            "write" -> Action.WRITE
            "execute" -> Action.EXECUTE
            else -> {
                exitProcess(ExitCode.ERROR_INVALID_ACTION.code)
            }
        }
        if (target == null) {
            exitProcess(ExitCode.ERROR_RESOURCE_NOT_FOUND.code)
        }
//        val permissionManager = PermissionManager()
        permissionManager.grantPermission("A", "alice", Action.READ)
        permissionManager.grantPermission("B", "alice", Action.WRITE)
        permissionManager.grantPermission("C", "alice", Action.EXECUTE)

        if (!permissionManager.hasPermission(target, login, act)) {
            exitProcess(ExitCode.ERROR_NO_PERMISSION.code)
        }

        if (volume.toInt() > 10) {
            exitProcess(ExitCode.ERROR_EXCEED_MAX_VOLUME.code)
        }
        exitProcess(ExitCode.SUCCESS.code)
    }
}
