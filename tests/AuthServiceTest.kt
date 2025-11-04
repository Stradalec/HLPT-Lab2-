import interfaces.IAuthService
import models.AuthService
import kotlin.system.exitProcess
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import enumerators.ExitCode
import models.UserData

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class AuthServiceTest {
    private val authService = AuthService()

    @Test
    fun testCheckHashEqual() {
        val hash1 = authService.getHash("password", "salt")
        val hash2 = authService.getHash("password", "salt")
        assertEquals(hash1, hash2)
    }

    @Test
    fun testGetHashDifferentForDifferentInput1() {
        val hash1 = authService.getHash("password1", "salt")
        val hash2 = authService.getHash("password2", "salt")
        assertNotEquals(hash1, hash2)
    }

    @Test
    fun testGetHashDifferentForDifferentInput2() {
        val hash1 = authService.getHash("password", "salt1")
        val hash2 = authService.getHash("password", "salt2")
        assertNotEquals(hash1, hash2)
    }

    @Test
    fun testBytesToHex() {
        val bytes = byteArrayOf(0x0, 0xA, 0xF, 0x10, 0xFF.toByte())
        val hex = authService.bytesToHex(bytes)
        assertEquals("000a0f10ff", hex)
    }

    @Test
    fun testBytesToHexEmpty() {
        val empty = byteArrayOf()
        val hex = authService.bytesToHex(empty)
        assertEquals("", hex)
    }

    @Test
    fun testGetHashWithEmptyStrings() {
        val result = authService.getHash("", "")
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }
}
