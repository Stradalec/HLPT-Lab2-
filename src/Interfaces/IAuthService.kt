package interfaces
import models.UserData

interface IAuthService {
    fun authorization(user: UserData?, password: String): Int
    fun getHash(password: String, salt: String): String
    fun bytesToHex(hash: ByteArray): String
}