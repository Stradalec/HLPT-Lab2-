/* package models
import repositories.*

fun createMockData():  Triple<UserRepository, ResourceRepository, PermissionRepository> {
    val userRepository = UserRepository().apply{
        save(User(id = 1, login = "alice", salt = "saltAlice", hash = "0ded4a676ee2fcd61ab5772e67ac33ef2ada6a929470cac9cb703cc9e6315c85"));
        save(User(id = 2, login = "stradalets", salt = "absoluteSuffering", hash = "No hash?"))
    } // солевая алиса
    val root = Resource(id = 1, name = "root", maxVolume = 100, parentId = null)
    val folderA = Resource(id = 2, name = "A", maxVolume = 50, parentId = 1)
    val folderB = Resource(id = 3, name = "B", maxVolume = 20, parentId = 2)
    val fileC = Resource(id = 4, name = "C", maxVolume = 10, parentId = 3)
    val fileD = Resource(id = 5, name = "D", maxVolume = 10, parentId = 1)
    
    val resourceRepo = ResourceRepository()
    val permissionRepo = PermissionRepository()
    resourceRepo.save(root)
    resourceRepo.save(folderA)
    resourceRepo.save(folderB)
    resourceRepo.save(fileC)
    resourceRepo.save(fileD)
    val alice = userRepository.findByLogin("alice")!!
    permissionRepo.grant(Permission(alice.id, 2, "R--")) 
    permissionRepo.grant(Permission(alice.id, 3, "R--")) 
    permissionRepo.grant(Permission(alice.id, 4, "R--")) 
    return Triple(userRepository, resourceRepo, permissionRepo)
} */