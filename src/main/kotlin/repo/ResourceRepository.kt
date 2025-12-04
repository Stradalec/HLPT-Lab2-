package com.HLPTLab7.ExplorerApp.repositories
import com.HLPTLab7.ExplorerApp.interfaces.IResourceRepository
import com.HLPTLab7.ExplorerApp.models.Resource
import com.HLPTLab7.ExplorerApp.database.*
import com.HLPTLab7.ExplorerApp.dao.ResourceDao
import org.springframework.stereotype.Repository
import javax.sql.DataSource
import java.sql.Connection
@Repository
class ResourceRepository(private val resourceDao: ResourceDao, private val dataSource: DataSource) : IResourceRepository  {
    override fun findById(id: Int): Resource? {
        return withConnection { inputConnection ->
            resourceDao.findById(inputConnection, id)
        }
    }

    override fun findByName(name: String): Resource? {
        return withConnection{ inputConnection ->
             resourceDao.findByName(inputConnection, name)
        }
    }

    override fun findByParent(parentId: Int): List<Resource> {
        return withConnection{ inputConnection ->
            resourceDao.findByParent(inputConnection, parentId)
        }
    }

    override fun save(resource: Resource) {
        withConnection { inputConnection ->
            resourceDao.save(inputConnection, resource)
            inputConnection.commit()
        }
    }
   override fun delete(id: Int): Boolean {
        return withConnection { inputConnection ->
            val result = resourceDao.delete(inputConnection, id)
            inputConnection.commit()
            result
        }
    }

    override fun findByPath(path: String): Resource? {
        return withConnection { inputConnection ->
            resourceDao.findByPath(inputConnection, path)
        }
    }
    private fun <T> withConnection(block: (Connection) -> T): T {
        return dataSource.connection.use { connection ->
            block(connection).also {
                connection.commit()
            }
        }
    }

}