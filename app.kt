import kotlinx.cli.*
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

data class UserData(val salt: String, val hash: String)
val users = mapOf(
    "alice" to UserData(salt = "saltAlice", hash = "No hash?"), 
    "stradalets" to UserData(salt = "absoluteSuffering", hash = "No hash?")
)
fun main(args: Array<String>) {
    val parser = ArgParser("app")

    val login by parser.option(
        ArgType.String,
        fullName = "login",
        description = "User login"
    ).required()

    val password by parser.option(
        ArgType.String,
        fullName = "password",
        description = "User password"
    ).required()

    val action by parser.option(
        ArgType.String,
        fullName = "action",
        description = "Type of action wtih file"
    ).required()

    val resource by parser.option(
        ArgType.String,
        fullName = "resource",
        description = "Path to resource"
    ).required()

    val volume by parser.option(
        ArgType.String,
        fullName = "volume",
        description = "Volume of file"
    ).required()

    parser.parse(args)
    val user = users[login]
    if (user == null) {
        println("User not found")
        exitProcess(2)
    }
    // val hashedPassword = hash(password, user.salt)
    // println("Password: $hashedPassword")
    if (hash(password, user.salt) != user.hash) {
        
        println("Invalid password")
        exitProcess(2)
    }

    if (volume.toInt() > 10) {
        println("Requested volume $volume exceeds maximum allowed for resource $resource")
        exitProcess(4)
    }
    println("Login: $login")
    println("Password: $password")
    
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