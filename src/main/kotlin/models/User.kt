package com.HLPTLab7.ExplorerApp.models
import jakarta.persistence.*
@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @Column(nullable = false, unique = true) 
    val login: String = "",
    @Column(nullable = false) 
    val salt: String = "",
    @Column(nullable = false) 
    val hash: String = "")