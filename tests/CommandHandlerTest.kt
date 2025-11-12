import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import handlers.CommandHandler
import interfaces.*
import models.*
import enumerators.*
import kotlinx.cli.*
import repositories.*



class MutablePermissionManager(var isAllowed: Boolean = true) : IPermissionManager {
    override fun grantPermission(resourceName: String, user: String, action: Action) { /* no-op */ }
    override fun hasPermission(resource: Resource?, user: String, action: Action) = isAllowed
}

class CommandHandlerTest {
    private lateinit var mockAuthService: IAuthService
    private lateinit var mockPermissionManager: IPermissionManager
    private lateinit var mockUsers: UserRepository
    private lateinit var mockRoot: Resource

    @BeforeEach
    fun setup() {
        mockAuthService = object : IAuthService {
            override fun authorization(user: UserData?, password: String) : Int { return 0}
            override fun getHash(password: String, salt: String) = "mock"
            override fun bytesToHex(hash: ByteArray) = "mock"
        }

        mockPermissionManager = MutablePermissionManager()

        val (u, r) = createMockData()
        mockUsers = u
        mockRoot = r
    }

    private fun runAndCatch(vararg args: String): Int {
        val handler = CommandHandler(mockAuthService, mockPermissionManager, mockUsers, mockRoot)
        val executeResult = { handler.execute(args as Array<String>) }

        return executeResult()
    }

    @Test
    fun testInvalidVolumeFormatCausesExitCode7() {
        val code = runAndCatch(
            "--login","alice","--password","123","--action","read","--resource","A","--volume","abc"
        )

        assertEquals(ExitCode.ERROR_INVALID_VOLUME_FORMAT.code, code)
    }

    @Test
    fun testInvalidActionCausesExitCode4() {
        val code = runAndCatch(
            "--login","alice","--password","123","--action","dance","--resource","A","--volume","5"
        )

        assertEquals(ExitCode.ERROR_INVALID_ACTION.code, code)
    }

    @Test
    fun testResourceNotFoundCausesExitCode6() {
        val code = runAndCatch(
            "--login","alice","--password","123","--action","read","--resource","Z","--volume","5"
        )

        assertEquals(ExitCode.ERROR_RESOURCE_NOT_FOUND.code, code)
    }

    @Test
    fun testNoPermissionCausesExitCode5() { 
        (mockPermissionManager as MutablePermissionManager).isAllowed = false
        val code = runAndCatch(
            "--login","alice","--password","123","--action","read","--resource","A","--volume","5"
        )

        assertEquals(ExitCode.ERROR_NO_PERMISSION.code, code)
    }

    @Test
    fun testVolumeTooLargeCausesExitCode8() {
        val code = runAndCatch(
            "--login","alice","--password","123","--action","read","--resource","A","--volume","11"
        )

        assertEquals(ExitCode.ERROR_EXCEED_MAX_VOLUME.code, code)
    }

    @Test
    fun testValidArgumentsReturnSuccess0() {
        val code = runAndCatch(
            "--login","alice","--password","123","--action","read","--resource","A","--volume","5"
        )

        assertEquals(ExitCode.SUCCESS.code, code)
    }

    @Test
    fun testActionCaseInsensitive() {
        fun run(action: String): Int = runAndCatch(
            "--login","alice", "--password", "123", "--action", action, "--resource", "A", "--volume", "5"
        )

        assertEquals(ExitCode.SUCCESS.code, run("READ"))
        assertEquals(ExitCode.SUCCESS.code, run("Write"))
        assertEquals(ExitCode.SUCCESS.code, run("ExEcUtE"))
    }

    @Test
    fun testMissingRequiredOptionCausesCliError() {
        val handler = CommandHandler(mockAuthService, mockPermissionManager, mockUsers, mockRoot)
        // скип --volume
        val args = arrayOf("--login","alice","--password","123","--action","read","--resource","A")
        assertThrows(Exception::class.java) {handler.execute(args)}
    }
}
