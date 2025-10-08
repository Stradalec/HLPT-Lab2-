package app.tests
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import app.core.*

class ResourceTest {

  @Test
  fun `Child work`() {
    val parent = Resource("parent")
    val child = Resource("child")
    parent.addChild(child)
    
    val retrieved = parent.getChild("child")
    assertNotNull(retrieved)
    assertEquals(child, retrieved)
  }

  @Test
  fun `findByPath check existing paths`() {
    val root = Resource("root")
    val child1 = Resource("child1")
    val child2 = Resource("child2")
    root.addChild(child1)
    child1.addChild(child2)

    val found = root.findByPath("child1.child2")
    assertNotNull(found)
    assertEquals(child2, found)
  }

  @Test
  fun `findByPath check not existing path`() {
    val root = Resource("root")
    assertNull(root.findByPath("unknown.path"))
  }
}
