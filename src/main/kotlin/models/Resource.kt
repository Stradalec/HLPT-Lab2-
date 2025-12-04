package com.HLPTLab7.ExplorerApp.models

class Resource (
    val id: Int,
    val name: String,
    val maxVolume: Int = 10,
    val parentId: Int? = null
)