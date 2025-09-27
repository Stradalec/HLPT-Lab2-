import kotlinx.cli.*
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

enum class Action { READ, WRITE, EXECUTE }
// salo
data class UserData(val salt: String, val hash: String)
val users = mapOf(
    "alice" to UserData(salt = "saltAlice", hash = "0ded4a676ee2fcd61ab5772e67ac33ef2ada6a929470cac9cb703cc9e6315c85"), 
    "stradalets" to UserData(salt = "absoluteSuffering", hash = "No hash?")
)

// класс ресурса. нечто вроде имитации файловой структуры в проводнике на компе.
// по заданию принимает имя, размер.
// имеет батьку для реализации иерархии.
// по сути представляет собой дерево. методы для поиска и получения дочерних узлов "папочек".
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

    fun grantPermission(user: String, action: Action) {
        permissions.computeIfAbsent(user) { mutableSetOf() }.add(action)
    }

    fun hasPermission(user: String, action: Action): Boolean {
    val userActions = permissions[user]
    return if (userActions != null) {
        if (action in userActions) true else parent?.hasPermission(user, action) ?: false
    } else {
        parent?.hasPermission(user, action) ?: false
    }
}

}

fun main(args: Array<String>) {
    val parser = ArgParser("app")

    val login by parser.option(ArgType.String, fullName = "login", description = "User login").required()
    val password by parser.option(ArgType.String, fullName = "password", description = "User password" ).required()
    val action by parser.option(ArgType.String, fullName = "action", description = "Type of action wtih file").required()
    val resource by parser.option(ArgType.String, fullName = "resource", description = "Path to resource").required()
    val volume by parser.option(ArgType.String, fullName = "volume", description = "Volume of file").required()

    try {
        parser.parse(args)
    } catch (e: Exception) {
        exitProcess(1)
    }

    val user = users[login]
    if (user == null) {
        println("User not found")
        exitProcess(2)
    }
    val hashedPassword = hash(password, user.salt) //Этот код выводит hash, чтобы не хранить его в открытом доступе
    //println("Password: $hashedPassword")  //Можно переназначить соль, получить новый хэш и внести для пользователя 
    if (hash(password, user.salt) != user.hash) {
        
        println("Invalid password") 
        exitProcess(2)
    }

    val root = Resource("root", 100)
    val folderA = Resource("A", 50, root)
    val folderB = Resource("B", 20, folderA)
    val fileC = Resource("C", 10, folderB)

    root.addChild(folderA)
    folderA.addChild(folderB)
    folderB.addChild(fileC)

    folderA.grantPermission("alice", Action.READ)
    folderB.grantPermission("alice", Action.WRITE)
    fileC.grantPermission("alice", Action.EXECUTE)

    val target = root.findByPath(resource)
    if (target == null) {
        println("Resource not found")
        exitProcess(3)
    }

    val act = when (action.lowercase()) {
        "read" -> Action.READ
        "write" -> Action.WRITE
        "execute" -> Action.EXECUTE
        else -> {
            println("Unknown action")
            exitProcess(4)
        }
    }

    if (!target.hasPermission(login, act)) {
        println("Access denied for $login to perform $action on $resource")
        exitProcess(5)
    }

    if (volume.toInt() > 10) {
        println("Requested volume $volume exceeds maximum allowed for resource $resource")
        exitProcess(8)
    }
    println("finally, there is some working time")
    exitProcess(0)
}


fun hash(password: String, salt: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val combined = salt + password
    val hashBytes = digest.digest(combined.toByteArray(StandardCharsets.UTF_8))
    return bytesToHex(hashBytes)
}

fun bytesToHex(hash: ByteArray): String {
    val hexString = StringBuilder(2 * hash.size)
    for (byte in hash) {
        val hex = Integer.toHexString(0xff and byte.toInt())
        if (hex.length == 1) hexString.append('0')
        hexString.append(hex)
    }
    return hexString.toString()
}