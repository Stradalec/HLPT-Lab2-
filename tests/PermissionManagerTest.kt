import models.Permission
import models.Resource
import models.User
import interfaces.IPermissionManager
import models.PermissionManager
import interfaces.IPermissionRepository
import interfaces.IResourceRepository
import interfaces.IUserRepository
import enumerators.Action
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*



class PermissionManagerTests {

    @Test
    fun testAddAndCheckPermission() {
        val userRepo = InMemoryUserRepository()
        val resourceRepo = InMemoryResourceRepository()
        val permissionRepo = InMemoryPermissionRepository()

        val user = User(1, "user", "salt", "hash")
        val resource = Resource(10, "res", 10, null)

        userRepo.save(user)
        resourceRepo.save(resource)

        val permissionManager = PermissionManager(permissionRepo, resourceRepo, userRepo)

        permissionManager.grantPermission("res", "user", Action.READ)

        assertTrue(permissionManager.hasPermission(10, 1, Action.READ))
        assertFalse(permissionManager.hasPermission(10, 1, Action.WRITE))
    }

    @Test
    fun testPermissionTransmission() {
        val userRepo = InMemoryUserRepository()
        val resourceRepo = InMemoryResourceRepository()
        val permissionRepo = InMemoryPermissionRepository()

        val user = User(1, "user", "salt", "hash")
        val parent = Resource(1, "Parent", 10, null)
        val child = Resource(2, "Child", 10, 1)

        userRepo.save(user)
        resourceRepo.save(parent)
        resourceRepo.save(child)

        val permissionManager = PermissionManager(permissionRepo, resourceRepo, userRepo)

        permissionManager.grantPermission("Parent", "user", Action.READ)

        assertTrue(permissionManager.hasPermission(2, 1, Action.READ))
    }

    @Test
    fun testNoPermissionReturnsFalse() {
        val userRepo = InMemoryUserRepository()
        val resourceRepo = InMemoryResourceRepository()
        val permissionRepo = InMemoryPermissionRepository()

        val user = User(1, "user", "salt", "hash")
        val resource = Resource(10, "res", 10, null)

        userRepo.save(user)
        resourceRepo.save(resource)

        val permissionManager = PermissionManager(permissionRepo, resourceRepo, userRepo)

        assertFalse(permissionManager.hasPermission(10, 1, Action.READ))
    }
}