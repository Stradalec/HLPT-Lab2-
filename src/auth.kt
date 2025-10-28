import kotlinx.cli.*
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

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
