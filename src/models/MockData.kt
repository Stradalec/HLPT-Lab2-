package models
import repositories.*

fun createMockData(): Pair<UserRepository, Resource> {
    val userRepository = UserRepository().apply{
        save(UserData(login = "alice", salt = "saltAlice", hash = "0ded4a676ee2fcd61ab5772e67ac33ef2ada6a929470cac9cb703cc9e6315c85"));
        save(UserData(login = "stradalets", salt = "absoluteSuffering", hash = "No hash?"))
    } // солевая алиса
    val root = Resource("root", 100)
    val folderA = Resource("A", 50, root)
    val folderB = Resource("B", 20, folderA)
    val fileC = Resource("C", 10, folderB)
    val fileD = Resource("D", 10, root)

    root.addChild(folderA)
    root.addChild(fileD)
    folderA.addChild(folderB)
    folderB.addChild(fileC)
    return userRepository to root
}