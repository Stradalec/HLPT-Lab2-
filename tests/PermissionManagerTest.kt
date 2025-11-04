import models.Resource
import interfaces.IResource
import models.PermissionManager
import interfaces.IPermissionManager
import enumerators.Action
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
public class PermissionManagerTests {
    @Test
    fun testAddAndCheckPermission() {
        val permissionManager = PermissionManager()
        val resource = Resource("Крутой ресурс с крутым названием")
        val user = "Крутой пользователь"

        permissionManager.grantPermission(resource.name,user, Action.READ)

        assertTrue(permissionManager.hasPermission(resource, user, Action.READ))
        assertFalse(permissionManager.hasPermission(resource, user, Action.WRITE))
    }

    @Test
    fun testPermissionTransmission() {
        val permissionManager = PermissionManager()

        val parent = Resource("Parent")
        val child = Resource("Child", parent = parent)

        permissionManager.grantPermission(parent.name, "user", Action.READ)

        assertTrue(permissionManager.hasPermission(child, "user", Action.READ))
    }

    @Test
    fun testNoPermissionReturnsFalse() {
        val permissionManager = PermissionManager()
        val resource = Resource("Res")

        assertFalse(permissionManager.hasPermission(resource, "unknown_user", Action.READ))
        assertFalse(permissionManager.hasPermission(resource, "user", Action.WRITE))
    }

    @Test
    fun testHasPermissionForUnknownUserAndAction() {
        val manager = PermissionManager()
        val resource = Resource("Res")

        manager.grantPermission(resource.name, "bro", Action.READ)
        assertFalse(manager.hasPermission(resource, "notABro", Action.WRITE))
    }

    @Test
    fun testHasPermissionWithNullResourceReturnsFalse() {
        val manager = PermissionManager()
        val result = manager.hasPermission(null, "someone", Action.READ)
        assertFalse(result)
    }
}