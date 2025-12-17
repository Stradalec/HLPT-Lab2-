package com.HLPTLab7.ExplorerApp.models
import jakarta.persistence.*
@Entity
@Table(name = "resources")
class Resource (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false)
    val name: String = "",

    @Column(name = "max_volume", nullable = false)
    val maxVolume: Int = 10,

    @Column(name = "parent_id")
    val parentId: Int? = null
)