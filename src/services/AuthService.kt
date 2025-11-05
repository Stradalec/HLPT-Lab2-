package models
import interfaces.IAuthService
import kotlin.system.exitProcess
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import enumerators.ExitCode
class AuthService : IAuthService {
    override fun authorization(user: UserData?, password: String): Int {
        if (user == null) {
            return ExitCode.ERROR_UNKNOWN_USER.code
        }

        if (getHash(password, user.salt) != user.hash) {
            return ExitCode.ERROR_WRONG_PASSWORD.code
        }
        return 0
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