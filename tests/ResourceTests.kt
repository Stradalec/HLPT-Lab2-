import models.Resource
import interfaces.IResource
import enumerators.Action
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

public class ResourceTests {
    @Test
    fun testAddAndCheckResource() {
        val root = Resource("root")
        val child = Resource("child")
        root.addChild(child)
        assertEquals(child, root.getChild("child"))
        assertNull(root.getChild("nonexistent"))
    }

    @Test
    fun testFindResourceByPath() {
        val root = Resource("root")
        val folderA = Resource("A")
        val folderB = Resource("B")
        root.addChild(folderA)
        folderA.addChild(folderB)

        assertEquals(folderB, root.findByPath("A.B"))
        assertNull(root.findByPath("A.C"))
        assertNull(root.findByPath("Not.Exist"))
    }

    @Test
    fun testResourceRemove() {
        val root = Resource("root")
        val doomedChild = Resource("child", parent = root)

        root.addChild(doomedChild)

        val checkRemove = doomedChild.remove()

        assertTrue(checkRemove)
        assertNull(root.getChild("child"))
    }

    @Test
    fun testGetAll() {
        val root = Resource("root")
        val childOne = Resource("first")
        val childTwo = Resource("second")
        val farChild = Resource("third")

        root.addChild(childOne)
        root.addChild(childTwo)
        childOne.addChild(farChild)

        val allResources = root.getAll()
        assertTrue(allResources.containsAll(listOf(childOne, childTwo, farChild)))
        assertEquals(3, allResources.size)
    }
}