import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import interfaces.*
import models.*
import enumerators.*
import repositories.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import handlers.CommandHandler
import interfaces.*
import models.*
import enumerators.*
import repositories.*

class MutablePermissionManager(var isAllowed: Boolean = true) : IPermissionManager {
    override fun grantPermission(resourceName: String, userLogin: String, action: Action) { /* no-op */ }
    override fun hasPermission(resourceId: Int, userId: Int, action: Action): Boolean = isAllowed
}

class CommandHandlerTest {
    private lateinit var mockAuthService: IAuthService
    private lateinit var mockPermissionManager: IPermissionManager
    private lateinit var mockUsers: IUserRepository
    private lateinit var mockResources: IResourceRepository
    private lateinit var mockPermissions: IPermissionRepository

    @BeforeEach
    fun setup() {
        mockAuthService = object : IAuthService {
            override fun authorization(user: User?, password: String): Int = 0 // успех
            override fun getHash(password: String, salt: String) = "mock"
            override fun bytesToHex(hash: ByteArray) = "mock"
        }

        mockPermissionManager = MutablePermissionManager()

        mockUsers = InMemoryUserRepository()
        mockResources = InMemoryResourceRepository()
        mockPermissions = InMemoryPermissionRepository()

        val alice = User(1, "alice", "saltAlice", "hash")
        val root = Resource(1, "root", 100, null)
        val folderA = Resource(2, "A", 50, 1)
        val folderB = Resource(3, "B", 20, 2)
        val fileC = Resource(4, "C", 10, 3)
        val fileD = Resource(5, "D", 10, 1)

        mockUsers.save(alice)
        mockResources.save(root)
        mockResources.save(folderA)
        mockResources.save(folderB)
        mockResources.save(fileC)
        mockResources.save(fileD)

        mockPermissions.grant(Permission(1, 2, "R--")) 
        mockPermissions.grant(Permission(1, 3, "R--")) 
        mockPermissions.grant(Permission(1, 4, "R--")) 
    }

    private fun runAndCatch(vararg args: String): Int {
        val handler = CommandHandler(mockAuthService, mockUsers, mockResources, mockPermissions)
        return handler.execute(args as Array<String>)
    }

    @Test
    fun testInvalidVolumeFormatCausesExitCode7() {
        val code = runAndCatch(
            "--login", "alice", "--password", "123", "--action", "read", "--resource", "A", "--volume", "abc"
        )
        assertEquals(ExitCode.ERROR_INVALID_VOLUME_FORMAT.code, code)
    }

    @Test
    fun testInvalidActionCausesExitCode4() {
        val code = runAndCatch(
            "--login", "alice", "--password", "123", "--action", "dance", "--resource", "A", "--volume", "5"
        )
        assertEquals(ExitCode.ERROR_INVALID_ACTION.code, code)
    }

    @Test
    fun testResourceNotFoundCausesExitCode6() {
        val code = runAndCatch(
            "--login", "alice", "--password", "123", "--action", "read", "--resource", "Z", "--volume", "5"
        )
        assertEquals(ExitCode.ERROR_RESOURCE_NOT_FOUND.code, code)
    }

    @Test
    fun testNoPermissionCausesExitCode5() {
        (mockPermissionManager as MutablePermissionManager).isAllowed = false
        val code = runAndCatch(
            "--login", "alice", "--password", "123", "--action", "read", "--resource", "A", "--volume", "5"
        )
        assertEquals(ExitCode.ERROR_NO_PERMISSION.code, code)
    }

    @Test
    fun testVolumeTooLargeCausesExitCode8() {
        val code = runAndCatch(
            "--login", "alice", "--password", "123", "--action", "read", "--resource", "A", "--volume", "11"
        )
        assertEquals(ExitCode.ERROR_EXCEED_MAX_VOLUME.code, code)
    }

    @Test
    fun testValidArgumentsReturnSuccess0() {
        val code = runAndCatch(
            "--login", "alice", "--password", "123", "--action", "read", "--resource", "A", "--volume", "5"
        )
        assertEquals(ExitCode.SUCCESS.code, code)
    }

    @Test
    fun testActionCaseInsensitive() {
        fun run(action: String): Int = runAndCatch(
            "--login", "alice", "--password", "123", "--action", action, "--resource", "A", "--volume", "5"
        )
        assertEquals(ExitCode.SUCCESS.code, run("READ"))
        assertEquals(ExitCode.SUCCESS.code, run("Write"))
        assertEquals(ExitCode.SUCCESS.code, run("ExEcUtE"))
    }

    @Test
    fun testMissingRequiredOptionCausesHelp() {
        val handler = CommandHandler(mockAuthService, mockUsers, mockResources, mockPermissions)
        val args = arrayOf("--login", "alice", "--password", "123", "--action", "read", "--resource", "A")
        val code = handler.execute(args)
        assertEquals(ExitCode.HELP.code, code)
    }
}