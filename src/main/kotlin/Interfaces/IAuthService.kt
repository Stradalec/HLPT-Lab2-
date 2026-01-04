package com.HLPTLab7.ExplorerApp.interfaces
import com.HLPTLab7.ExplorerApp.models.User

interface IAuthService {
    fun authorization(user: User?, password: String): Int
    fun getHash(password: String, salt: String): String
    fun bytesToHex(hash: ByteArray): String
}