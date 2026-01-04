package com.HLPTLab7.ExplorerApp.repositories

import com.HLPTLab7.ExplorerApp.models.Resource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
@Repository
interface SpringResourceRepository : JpaRepository<Resource, Int> {
    fun findByName(name: String): Resource?
    fun findByParentId(parentId: Int): List<Resource>
}