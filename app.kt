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

class Resource(
    val name: String,
    val maxVolume: Int = 10,
    val parent: Resource? = null
) {
    private val children = mutableMapOf<String, Resource>()
    private val permissions = mutableMapOf<String, MutableSet<Action>>() // login -> actions

    fun addChild(resource: Resource) {
        children[resource.name] = resource
    }

    fun getChild(name: String): Resource? = children[name]

    fun findByPath(path: String): Resource? {
        val parts = path.split(".")
        var current: Resource? = this
        for (part in parts) {
            current = current?.getChild(part) ?: return null
        }
        return current
    }
}

fun main(args: Array<String>) {
    if (args.isEmpty() || args.any { it == "--help" || it == "-h" }) {
        exitProcess(ExitCode.HELP.code)
    }
    val commandHandler = CommandHandler()
    try {
        commandHandler.workWithArguments(args)
    } catch (e: Exception) {
        exitProcess(ExitCode.HELP.code)
    }
}

fun createMockData(): Pair<Map<String, UserData>, Resource> {
    val users = mapOf(
        "alice" to UserData(salt = "saltAlice", hash = "0ded4a676ee2fcd61ab5772e67ac33ef2ada6a929470cac9cb703cc9e6315c85"),
        "stradalets" to UserData(salt = "absoluteSuffering", hash = "No hash?")
    )
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

class PermissionManager : IPermissionManager {
    val permissions = mutableMapOf<String, MutableMap<String, MutableSet<Action>>>()

    override fun grantPermission(resourceName: String, user: String, action: Action) {
        val userPerms = permissions.computeIfAbsent(resourceName) { mutableMapOf() }
        val actions = userPerms.computeIfAbsent(user) { mutableSetOf() }
        actions.add(action)
    }

    override fun hasPermission(resource: Resource?, user: String, action: Action): Boolean {
        if (resource == null) return false
        val userActions = permissions[resource.name]?.get(user)
        return if (userActions != null && action in userActions) {
            true
        } else {
            hasPermission(resource.parent, user, action)
        }
    }
}

interface IPermissionManager {
    fun grantPermission(resourceName: String, user: String, action: Action)
    fun hasPermission(resource: Resource?, user: String, action: Action): Boolean
}

interface IAuthService {
    fun authorization(user: UserData?, password: String)
    fun getHash(password: String, salt: String): String
    fun bytesToHex(hash: ByteArray): String
}

class AuthService : IAuthService {
    override fun authorization(user: UserData?, password: String) {
        if (user == null) {
            exitProcess(ExitCode.ERROR_UNKNOWN_USER.code)
        }

        if (getHash(password, user.salt) != user.hash) {
            exitProcess(ExitCode.ERROR_WRONG_PASSWORD.code)
        }
    }

    override fun getHash(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val combined = salt + password
        val hashBytes = digest.digest(combined.toByteArray(StandardCharsets.UTF_8))
        return bytesToHex(hashBytes)
    }

    override fun bytesToHex(hash: ByteArray): String {
        val hexString = StringBuilder(2 * hash.size)
        for (byte in hash) {
            val hex = Integer.toHexString(0xff and byte.toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString()
    }
}
class CommandHandler(){
    val parser = ArgParser("app")

    val login by parser.option(ArgType.String, fullName = "login", description = "User login").required()
    val password by parser.option(ArgType.String, fullName = "password", description = "User password" ).required()
    val action by parser.option(ArgType.String, fullName = "action", description = "Type of action wtih file").required()
    val resource by parser.option(ArgType.String, fullName = "resource", description = "Path to resource").required()
    val volume by parser.option(ArgType.String, fullName = "volume", description = "Volume of file").required()

    fun workWithArguments(arguments: Array<String>){
        parser.parse(arguments)
        val (users, root) = createMockData()
    val authService = AuthService()
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
    val permissionManager = PermissionManager()
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